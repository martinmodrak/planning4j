/*
 * Copyright (C) 2012 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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
package cz.cuni.amis.planning4j.external.impl.itsimple;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cernm6am
 */
public class ItSimplePlannerSettings {
    String executableFilePath;
    
    boolean hasOutputFile;
    String outputFile;
    
    /**
     * Contains # to indicate incrementing number
     */
    String outputFileAutomaticIncrementSuffix;
    
    boolean outputFileNeedsArgument;
    String outputFileArgumentName;
    List<String> additionalGeneratedFiles;
    
    String consoleOutputPlanStartIdentifier;
    int consoleOutputStartsAfterNLines;
    String consoleOutputPlanEndIdentifier;
    
    String domainArgumentName;
    String problemArgumentName;
    
    List<PlannerArgument> additionalArguments;

    /**
     * Ways to check for the no solution found situation. All of the results are ORed.
     */
    List<INoPlanFoundChecker> noPlanFoundCheckers;

    public ItSimplePlannerSettings() {
        additionalArguments = new ArrayList<PlannerArgument>();                
        additionalGeneratedFiles = new ArrayList<String>();
        noPlanFoundCheckers = new ArrayList<INoPlanFoundChecker>();
    }
    

    
    
    public List<PlannerArgument> getAdditionalArguments() {
        return additionalArguments;
    }

    public List<String> getAdditionalGeneratedFiles() {
        return additionalGeneratedFiles;
    }

    public String getConsoleOutputPlanEndIdentifier() {
        return consoleOutputPlanEndIdentifier;
    }

    public String getConsoleOutputPlanStartIdentifier() {
        return consoleOutputPlanStartIdentifier;
    }

    public int getConsoleOutputStartsAfterNLines() {
        return consoleOutputStartsAfterNLines;
    }

    public String getDomainArgumentName() {
        return domainArgumentName;
    }


    public String getExecutableFilePath() {
        return executableFilePath;
    }

    public boolean isHasOutputFile() {
        return hasOutputFile;
    }


    public String getOutputFile() {
        return outputFile;
    }

    public String getOutputFileArgumentName() {
        return outputFileArgumentName;
    }

    public boolean isOutputFileNeedsArgument() {
        return outputFileNeedsArgument;
    }

    public String getProblemArgumentName() {
        return problemArgumentName;
    }

    public List<INoPlanFoundChecker> getNoPlanFoundCheckers() {
        return noPlanFoundCheckers;
    }
    
    

    public void setConsoleOutputPlanEndIdentifier(String consoleOutputPlanEndIdentifier) {
        this.consoleOutputPlanEndIdentifier = consoleOutputPlanEndIdentifier;
    }

    public void setConsoleOutputPlanStartIdentifier(String consoleOutputPlanStartIdentifier) {
        this.consoleOutputPlanStartIdentifier = consoleOutputPlanStartIdentifier;
    }

    public void setConsoleOutputStartsAfterNLines(int consoleOutputStartsAfterNLines) {
        this.consoleOutputStartsAfterNLines = consoleOutputStartsAfterNLines;
    }

    public void setDomainArgumentName(String domainArgumentName) {
        this.domainArgumentName = domainArgumentName;
    }

    public String getOutputFileAutomaticIncrementSuffix() {
        return outputFileAutomaticIncrementSuffix;
    }

    public void setOutputFileAutomaticIncrementSuffix(String fileNameAutomaticIncrementSuffix) {
        this.outputFileAutomaticIncrementSuffix = fileNameAutomaticIncrementSuffix;
    }

    

    public void setExecutableFilePath(String filePath) {
        this.executableFilePath = filePath;
    }

    public void setHasOutputFile(boolean hasOutputFile) {
        this.hasOutputFile = hasOutputFile;
    }


    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public void setOutputFileArgumentName(String outputFileArgumentName) {
        this.outputFileArgumentName = outputFileArgumentName;
    }

    public void setOutputFileNeedsArgument(boolean outputFileNeedsArgument) {
        this.outputFileNeedsArgument = outputFileNeedsArgument;
    }

    public void setProblemArgumentName(String problemArgumentName) {
        this.problemArgumentName = problemArgumentName;
    }

    public void addAdditionalArgument(PlannerArgument argument){
        additionalArguments.add(argument);
    }
    
    public void addAdditionalGeneratedFile(String file){
        additionalGeneratedFiles.add(file);
    }
    
    public void addNoPlanFoundChecker(INoPlanFoundChecker noPlanFoundChecker){
        noPlanFoundCheckers.add(noPlanFoundChecker);
    }
}
