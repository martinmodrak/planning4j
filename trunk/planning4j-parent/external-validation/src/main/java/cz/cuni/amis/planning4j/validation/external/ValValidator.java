/*
 * Copyright (C) 2013 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.cuni.amis.planning4j.validation.external;

import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.external.impl.itsimple.ItSimpleUtils;
import cz.cuni.amis.planning4j.impl.AbstractValidator;
import cz.cuni.amis.planning4j.impl.ValidationResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * A validator that uses external executable validator VAL 
 * (<a href='http://www.dcs.kcl.ac.uk/staff/andrew/planning/index.php?option=com_content&view=article&id=70&Itemid=77'>VAL Homepage</a>)
 * to validate plans. 
 * Currently the package contains VAL version 4.2.08.
 * @author Martin Cerny
 */
public class ValValidator extends AbstractValidator<IPDDLFileDomainProvider, IPDDLFileProblemProvider> {

    private final Logger logger = Logger.getLogger(ValValidator.class);

    protected File validatorDirectory;
    
    /**
     * Folders in resources and extracted output that correspond to validator
     * binaries for specific platforms
     */
    private static final String LINUX_FOLDER_NAME = "validate-unix";
    private static final String WIN32_FOLDER_NAME = "validate-win32";
    
    private static final String LINUX_EXECUTABLE_NAME = "validate";
    private static final String WIN32_EXECUTABLE_NAME = "validate.exe";
    
    private static final String[] LINUX_AUXILIARY_FILES = new String[]{};
    private static final String[] WINDOWS_AUXILIARY_FILES = new String[]{"cyggcc_s-1.dll", "cygstdc++-6.dll","cygwin1.dll"};
    
    public ValValidator() {
        this(new File("."));
    }
    
    public ValValidator(File validatorDirectory){
        super(IPDDLFileDomainProvider.class, IPDDLFileProblemProvider.class);
        this.validatorDirectory = validatorDirectory;
    }

    
    protected static String getValidatorFolderName() throws ValidationException {
        switch(ItSimpleUtils.getOperatingSystem()){
            case LINUX: 
                return LINUX_FOLDER_NAME;
            case WINDOWS : 
                return WIN32_FOLDER_NAME;
            default: 
                throw new ValidationException("VAL validator is not supported on platform " + ItSimpleUtils.getOperatingSystem());
        }
    }

    /**
     * Returns validator executable name 
     * @return
     * @throws ValidationException 
     */
    protected static String getValidatorExecutableName() throws ValidationException {
        switch(ItSimpleUtils.getOperatingSystem()){
            case LINUX: 
                return LINUX_EXECUTABLE_NAME;
            case WINDOWS : 
                return WIN32_EXECUTABLE_NAME;
            default: 
                throw new ValidationException("VAL validator is not supported on platform " + ItSimpleUtils.getOperatingSystem());
        }
    }

    protected static String[] getAuxiliaryFiles() throws ValidationException {
        switch(ItSimpleUtils.getOperatingSystem()){
            case LINUX: 
                return LINUX_AUXILIARY_FILES;
            case WINDOWS : 
                return WINDOWS_AUXILIARY_FILES;
            default: 
                throw new ValidationException("VAL validator is not supported on platform " + ItSimpleUtils.getOperatingSystem());
        }
    }
    
    
    public static void extractAndPrepareValidator(){
        extractAndPrepareValidator(new File("."));
    }
    
    public static void extractAndPrepareValidator(File targetDirectory) {
        String folderName = getValidatorFolderName();
        try {
            for (String fileToExtractName : getAuxiliaryFiles()) {
                File binaryFile = new File(targetDirectory, folderName + File.separatorChar + fileToExtractName);

                ItSimpleUtils.extractFileIfNotExists(binaryFile, "/" + folderName + "/" + fileToExtractName);
            }
            String validatorExecutableName = getValidatorExecutableName();
            File mainExecutable = new File(targetDirectory, folderName + File.separatorChar + validatorExecutableName);
            ItSimpleUtils.extractFileIfNotExists(mainExecutable, "/" + folderName + "/" + validatorExecutableName);            
            mainExecutable.setExecutable(true, false);
        } catch (Exception ex) {
            throw new ValidationException("Could not extract VAL binaries.", ex);
        }
    }
    
    @Override
    public IValidationResult validate(IPDDLFileDomainProvider domainProvider, IPDDLFileProblemProvider problemProvider, List<ActionDescription> plan) {
        File mainExecutable = new File(validatorDirectory,  getValidatorFolderName() + File.separatorChar + getValidatorExecutableName());
        if(!mainExecutable.exists()){
            String toolMessage = "Could not find validator executable '" + getValidatorExecutableName() + "' in directory " + validatorDirectory.getAbsolutePath();
            throw new ValidationException(toolMessage);
        }
        FileWriter planWriter = null;
        Process process = null;
        try {
            
            /**
             * Write the plan to a temp file
             */
            File planTempFile = File.createTempFile("plan", ".soln");
            planWriter = new FileWriter(planTempFile);
            for(ActionDescription action : plan){
                planWriter.write(action.getStartTime() + ": (" + action.getName() + " ");
                for(String param : action.getParameters()){
                    planWriter.write(param + " ");                    
                }
                planWriter.write(") [" + action.getDuration() + "]\n");
            }
            
            planWriter.close();
            planWriter = null;
            /**
             * Invoke the validator
             */
            ProcessBuilder processBuilder = new ProcessBuilder(mainExecutable.getAbsolutePath(), 
                    "-s", //silent mode for simple parsing - only errors are printed to the stdout
                    domainProvider.getDomainFile().getAbsolutePath(), 
                    problemProvider.getProblemFile().getAbsolutePath(),
                    planTempFile.getAbsolutePath());
        
            logger.info("Starting VAL validator.");
            if(logger.isDebugEnabled()){
                logger.debug("The command: " + processBuilder.command());
            }
            process = processBuilder.start();
            
            Scanner outputScanner = new Scanner(process.getInputStream());
            
            StringBuilder consoleOutputBuilder = new StringBuilder();
            
            boolean hasNonEmptyLines = false;
            if(logger.isTraceEnabled()){
                logger.trace("Validator output:");
            }
            while(outputScanner.hasNextLine()) {
                String line = outputScanner.nextLine();
                if(!consoleOutputBuilder.toString().isEmpty()){
                    consoleOutputBuilder.append("\n");
                }
                if(!line.trim().isEmpty()){
                    hasNonEmptyLines = true;
                }
                consoleOutputBuilder.append(line);
                if(logger.isTraceEnabled()){
                    logger.trace(line);
                }
            }
            if(logger.isTraceEnabled()){
                logger.trace("Validator output end.");
            }          
            
            try {
                //clean the error stream. Otherwise this might prevent the process from being terminated / cleared from the process table
                IOUtils.toString(process.getErrorStream());
            } catch (IOException ex) {
                logger.warn("Could not clear error stream." , ex);
            }
            
            process.waitFor();
            boolean valid = !hasNonEmptyLines; //validator is run in silent mode, so any output means plan is not valid.
            logger.info("Validation finished. Result is: " + valid);
            return new ValidationResult(valid, consoleOutputBuilder.toString());
        } catch (Exception ex){
            if(planWriter != null){
                try {
                    planWriter.close();
                } catch(Exception ignored){
                }
            }
            if(process != null){
                process.destroy();
                try {
                    //clean the streams so that the process does not hang in the process table
                    IOUtils.toString(process.getErrorStream());
                    IOUtils.toString(process.getInputStream());
                } catch(Exception ignored){
                    logger.warn("Could not clear output/error stream." , ignored);
                }
            }
            throw new ValidationException("Error during validation", ex);
        }
    }

    
}
