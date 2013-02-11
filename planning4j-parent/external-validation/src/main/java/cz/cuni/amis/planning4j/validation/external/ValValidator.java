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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A validator that uses external executable validator VAL 
 * (<a href='http://www.dcs.kcl.ac.uk/staff/andrew/planning/index.php?option=com_content&view=article&id=70&Itemid=77'>VAL Homepage</a>)
 * to validate plans. 
 * Currently the package contains VAL version 4.2.08.
 * @author Martin Cerny
 */
public class ValValidator extends AbstractValidator<IPDDLFileDomainProvider, IPDDLFileProblemProvider> {


    protected File validatorDirectory;
    
    /**
     * Folders in resources and extracted output that correspond to validator
     * binaries for specific platforms
     */
    private static final String LINUX_FOLDER_NAME = "validate-unix";
    private static final String WIN32_FOLDER_NAME = "validate-win32";
    private static final String LINUX_EXECUTABLE_NAME = "validate";
    private static final String WIN32_EXECUTABLE_NAME = "validate.exe";
    
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
     * Returns validator executable name (including the validator folder).
     * @return
     * @throws ValidationException 
     */
    protected static String getValidatorExecutableName() throws ValidationException {
        switch(ItSimpleUtils.getOperatingSystem()){
            case LINUX: 
                return LINUX_FOLDER_NAME + File.pathSeparator + LINUX_EXECUTABLE_NAME;
            case WINDOWS : 
                return WIN32_FOLDER_NAME + File.pathSeparator + WIN32_EXECUTABLE_NAME;
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
            for (String fileToExtractName : ItSimpleUtils.getResourceListing(ValValidator.class, "/" + folderName)) {
                File binaryFile = new File(targetDirectory, folderName + File.pathSeparator + fileToExtractName);

                ItSimpleUtils.extractFileIfNotExists(binaryFile, "/" + folderName + "/" + fileToExtractName);
            }
            File mainExecutable = new File(targetDirectory, getValidatorExecutableName());
            mainExecutable.setExecutable(true, false);
        } catch (Exception ex) {
            throw new ValidationException("Could not extract VAL binaries.", ex);
        }
    }
    
    @Override
    public IValidationResult validate(IPDDLFileDomainProvider domainProvider, IPDDLFileProblemProvider problemProvider, List<ActionDescription> plan) {
        File mainExecutable = new File(validatorDirectory, getValidatorExecutableName());
        if(!mainExecutable.exists()){
            String toolMessage = "Could not find validator executable '" + getValidatorExecutableName() + "' in directory " + validatorDirectory.getAbsolutePath();
            throw new ValidationException(toolMessage);
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(mainExecutable.getAbsolutePath(), 
                    "-s", //silent mode for simple parsing - only errors are printed to the stdout
                    domainProvider.getDomainFile().getAbsolutePath(), 
                    problemProvider.getProblemFile().getAbsolutePath());
            
            Process process = processBuilder.start();
            
            Scanner outputScanner = new Scanner(process.getInputStream());
            
            List<String> consoleOutput = new ArrayList<String>();
            
            boolean hasNonEmptyLines = false;
            while(outputScanner.hasNextLine()) {
                String line = outputScanner.nextLine();
                if(!line.trim().isEmpty()){
                    hasNonEmptyLines = true;
                }
                consoleOutput.add(line);
            }
            process.waitFor();
            boolean valid = !hasNonEmptyLines; //validator is run in silent mode, so any output means plan is not valid.
            return new ValidationResult(valid, consoleOutput);
        } catch (Exception ex){
            throw new ValidationException("Error during validation", ex);
        }
    }

    
}
