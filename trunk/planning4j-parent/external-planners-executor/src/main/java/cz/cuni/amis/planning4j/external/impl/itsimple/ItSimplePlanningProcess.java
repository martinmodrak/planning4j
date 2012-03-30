/*** 
 * This file is part of Java API for AI planning
 * 
 * This file is based on code from project itSIMPLE.
 *
 * Java API for AI planning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. Other licenses might be available
 * upon written agreement.
 * 
 * Java API for AI planning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Java API for AI planning.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Authors:	Tiago S. Vaquero, 
 *		 	Victor Romero, Martin Cerny.
 **/
package cz.cuni.amis.planning4j.external.impl.itsimple;

import cz.cuni.amis.planning4j.PlanningException;
import cz.cuni.amis.planning4j.ActionDescription;
import cz.cuni.amis.planning4j.external.impl.ExternalPlanningResult;
import cz.cuni.amis.planning4j.external.IExternalPlannerExecutor;
import cz.cuni.amis.planning4j.external.IExternalPlanningResult;
import cz.cuni.amis.planning4j.PlanningStatistics;
import cz.cuni.amis.planning4j.external.IExternalPlanningProcess;
import cz.cuni.amis.planning4j.utils.PlanningUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

/**
 * Executes a plan using external planners specified with ItSimple XML format.
 * Instance of this class should not be reused for multiplre runs.
 * Identifiers of the returned plan are treated with {@link PlanningUtils#normalizeIdentifier(java.lang.String) }
 * @author Martin Cerny
 */
public class ItSimplePlanningProcess implements IExternalPlanningProcess {

    private static final Logger logger = Logger.getLogger(IExternalPlannerExecutor.LOGGER_NAME);

    protected File plannerExecutableFile;

    private ItSimplePlannerInformation chosenPlanner;

    private File plannerBinariesDirectory;

    private File workingDirectory;

    File domainFile;

    File problemFile;

    long timeInIO;

    Process process = null;

    private boolean cancelled = false;

    public ItSimplePlanningProcess(ItSimplePlannerInformation chosenPlanner, File plannerBinariesDirectory, File workingDirectory, File domainFile, File problemFile, long timeInIO) {
        this.chosenPlanner = chosenPlanner;
        this.plannerBinariesDirectory = plannerBinariesDirectory;
        this.workingDirectory = workingDirectory;

        this.domainFile = domainFile;
        this.problemFile = problemFile;
        this.timeInIO = timeInIO;
    }

    /**
     * Parses output and fills plan with information about actio and statistics with
     * statistical information
     * @param output
     * @param plan
     * @param statistics 
     */
    private void getPlanAndStatistics(List<String> output, List<String> plan, List<String> statistics) {
        //Separate statistics and plan (get plan)
        if (output != null) {
            for (String line : output) {
                if (!line.trim().equals("")) {
                    //get plan
                    if (line.trim().startsWith(";")) {
                        statistics.add(line.trim().substring(1).trim());
                    } else {
                        // if it is not a standard action then check if is still an action or a statistic
                        if (!(line.indexOf(":") > -1)) {
                            boolean isAnAction = false;

                            //check if the string can still be an action (e.g. 1 (action p1 p2 ... pn) )
                            if ((line.indexOf("(") > -1) && (line.indexOf(")") > -1)) {
                                //check if the first element on the string is the plan index
                                StringTokenizer st = new StringTokenizer(line.trim());
                                String firstItem = "index";
                                if (st.hasMoreTokens()) {
                                    firstItem = st.nextToken();
                                    try {
                                        double theIndex = Double.parseDouble(firstItem);
                                        isAnAction = true;
                                    } catch (Exception e) {
                                        isAnAction = false;
                                    }
                                }

                                //if it is an action the include the ":" for standarlization
                                if (isAnAction) {
                                    String actionBody = "";
                                    while (st.hasMoreTokens()) {
                                        // for each parameter, create a node
                                        actionBody += st.nextToken() + " ";
                                    }
                                    line = firstItem + ": " + actionBody;
                                }

                            }

                            if (isAnAction) {
                                plan.add(line.trim());
                            } else {
                                statistics.add(line.trim());
                            }


                        } else {//When it is really an action
                            plan.add(line.trim());
                        }

                    }
                }
            }
        }
    }

//    protected List<String> getPlan(List<String> Output) {
//        //Separate statistics and plan (get plan)
//        List<String> plan = new ArrayList<String>();
//        if (Output != null) {
//            for (Iterator<?> iter = Output.iterator(); iter.hasNext();) {
//                String element = (String) iter.next();
//                //System.out.println(element);
//                if (!element.trim().equals("")) {
//                    //get plan
//                    if (!element.trim().startsWith(";")) {
//                        plan.add(element.trim());
//                    }
//                }
//            }
//        }
//        return plan;
//    }
    /**
     * Guess whether line might be an action
     * @param line
     * @return 
     */
    protected boolean isLineAction(String line) {
        if (isLineStatistics(line)) {
            //it is surely a statistic
            return false;
        }

        if (line.contains(":")) {
            return true;
        }

        if (line.contains("(") && line.contains(")")) {
            return true;
        }

        return false;
    }

    /**
     * Guess whether line might be a statistic
     * @param line
     * @return 
     */
    protected boolean isLineStatistics(String line) {
        return line.trim().startsWith(";");
    }

    private static enum EConsoleParseState {

        BEGIN,
        COUNTING_TO_PLAN_START,
        READING_PLAN,
        END

    }

    /**
     * Runs the planner and returns the console output
     */
    protected UnprocessedPlanningResult runPlanner(File domain, File problem) {



        //1.Get main planner's parameters and arguments
        ItSimplePlannerSettings settings = chosenPlanner.getSettings();

        String plannerRelativeFile = settings.getExecutableFilePath();
        plannerExecutableFile = new File(plannerBinariesDirectory, plannerRelativeFile);

        if (!plannerExecutableFile.exists()) {
            String toolMessage = "Could not find selected planner '" + plannerRelativeFile + "' in directory " + plannerBinariesDirectory.getAbsolutePath();
            throw new PlanningException(toolMessage);
        }



        List<String> commandArguments = new ArrayList<String>();

        //1.0 Get planner execution file
        commandArguments.add(plannerExecutableFile.getAbsolutePath());

        //1.1 Get domain arguments
        if (!settings.getDomainArgumentName().trim().isEmpty()) {
            commandArguments.add(settings.getDomainArgumentName());
        }
        commandArguments.add(domain.getAbsolutePath()); //domain path

        //1.2 Get problem arguments
        if (!settings.getProblemArgumentName().trim().isEmpty()) {
            commandArguments.add(settings.getProblemArgumentName());
        }
        commandArguments.add(problem.getAbsolutePath()); //problem path

        //1.3 Get additional arguments
        for (PlannerArgument argument : settings.getAdditionalArguments()) {
            //System.out.println(argument.getChildText("name"));
            if (!argument.getName().trim().equals("")) {
                commandArguments.add(argument.getName());
            }
            //if there is a value for the argument then add to the command
            if (!argument.getValue().trim().equals("")) {
                commandArguments.add(argument.getValue());
            }

        }

        //1.4 Get output arguments
        if (settings.isHasOutputFile() && settings.isOutputFileNeedsArgument()) {
            commandArguments.add(settings.getOutputFileArgumentName());
            commandArguments.add(settings.getOutputFile()); //problem path            
        }

        synchronized (this) {
            if (cancelled) {
                return null;
            }
            logger.info("\n>> Calling planner " + chosenPlanner.getName() + " in directory: " + workingDirectory.getAbsolutePath());
            logger.fine("Planner arguments:" + commandArguments);
            //Call the planner
            try {
                ProcessBuilder builder = new ProcessBuilder(commandArguments);
                builder.directory(workingDirectory);
                process = builder.start();
            } catch (Exception e) {
                String message = "Error while running the planner " + chosenPlanner.getName() + ". ";
                throw new PlanningException(message, e);
            }
        }

        try {
            boolean plannerFoundNoSolution = false;


            Scanner sc = new Scanner(process.getInputStream());
            //Get the planner answer exposed in the console
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Planner console output:");
            }

            StringBuilder consoleOutputBuilder = new StringBuilder();
            List<String> unprocessedPlan = new ArrayList<String>();
            List<String> unprocessedStatistics = new ArrayList<String>();

            //Needed only when parsing plan from console, but need to be initialized here
            EConsoleParseState consoleParseState;
            int numLinesBeforePlan = settings.getConsoleOutputStartsAfterNLines();
            if (settings.getConsoleOutputPlanStartIdentifier() == null || settings.getConsoleOutputPlanStartIdentifier().isEmpty()) {
                consoleParseState = EConsoleParseState.COUNTING_TO_PLAN_START;
            } else {
                consoleParseState = EConsoleParseState.BEGIN;
            }


            while (sc.hasNextLine()) {

                String line = sc.nextLine();
                consoleOutputBuilder.append(line).append("\n");

                if (settings.getNoPlanFoundSignalType() == ENoPlanFoundSignalType.OUTPUT_TEXT) {
                    if (line.contains(settings.getNoPlanFoundOutputText())) {
                        plannerFoundNoSolution = true;
                    }
                }

                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(line);
                }

                if (!settings.isHasOutputFile()) {
                    if (!line.trim().isEmpty()) {
                        //the plan is part of console output
                        switch (consoleParseState) {
                            case BEGIN: {
                                if (line.contains(settings.getConsoleOutputPlanStartIdentifier())) {
                                    if (numLinesBeforePlan > 0) {
                                        consoleParseState = EConsoleParseState.COUNTING_TO_PLAN_START;
                                        numLinesBeforePlan--;
                                    } else {
                                        //the plan starts on the same line
                                        int indexPlanStart = line.indexOf(settings.getConsoleOutputPlanStartIdentifier());
                                        String firstLine = line.substring(indexPlanStart + settings.getConsoleOutputPlanStartIdentifier().length());
                                        if (!isLineAction(firstLine)) {
                                            unprocessedStatistics.add(firstLine);
                                        } else {
                                            unprocessedPlan.add(firstLine);
                                        }
                                    }
                                }
                                break;
                            }
                            case COUNTING_TO_PLAN_START: {
                                if (numLinesBeforePlan > 0) {
                                    numLinesBeforePlan--;
                                    break;
                                } else {
                                    consoleParseState = EConsoleParseState.READING_PLAN;
                                }
                                //intentional fallthrough!!!
                            }
                            case READING_PLAN: {
                                if (line.contains(settings.getConsoleOutputPlanEndIdentifier())) {
                                    consoleParseState = EConsoleParseState.END;
                                } else {
                                    if (!isLineAction(line)) {
                                        unprocessedStatistics.add(line);
                                    } else {
                                        unprocessedPlan.add(line);
                                    }
                                }
                                break;
                            }
                            case END: {
                                if (isLineStatistics(line)) {
                                    unprocessedStatistics.add(line);
                                }
                                break;
                            }

                        }
                    }
                }

            }
            sc.close();

            if (cancelled) {
                return null;
            }

            //Need to clean the stream, otherwise, it would block the process from terminating
            String errorOuput;
            try {
                errorOuput = IOUtils.toString(process.getErrorStream());
            } catch (IOException ex) {
                errorOuput = "Could not get error stream: " + ex.getMessage();
            }


            try {
                if (cancelled) {
                    return null;
                }

                process.waitFor();
                logger.info("\n>> Planner " + chosenPlanner.getName() + " finished execution\n ");
            } catch (InterruptedException ex) {
                if (cancelled) {
                    return null;
                }
                Logger.getLogger(ItSimplePlanningProcess.class.getName()).log(Level.INFO, "Waiting for planner execution interrupted", ex);
                destroyProcess(process, plannerExecutableFile);
                return null;
            }

            process.destroy();

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Planner console output end.");
            }

            if (cancelled) {
                return null;
            }


            if (process.exitValue() != 0) {
                if (settings.noPlanFoundSignalType == ENoPlanFoundSignalType.ERROR_CODE && settings.noPlanFoundErrorCode == process.exitValue()) {
                    plannerFoundNoSolution = true;
                } else {
                    throw new PlanningException("Planner terminated with an error - exit code: " + process.exitValue() + ". Planner output:\n " + consoleOutputBuilder.toString() + "\nError output:\n" + errorOuput);
                }
            }




            if (settings.isHasOutputFile()) {  //The planner does provide an output file

                String solutionFile = "solution.soln";
                if (!settings.getOutputFile().trim().isEmpty()) {
                    solutionFile = settings.getOutputFile();
                }

                if (settings.getOutputFileAutomaticIncrementSuffix() != null) {
                    //Find the existing file with the highest increment index
                    int i = 1;
                    while (true) {
                        String candidateSolutionFileName = solutionFile + settings.getOutputFileAutomaticIncrementSuffix().replace("#", Integer.toString(i));
                        File candidateSolutionFile = new File(workingDirectory, candidateSolutionFileName);
                        if (candidateSolutionFile.exists()) {
                            solutionFile = candidateSolutionFileName;
                            i++;
                        } else {
                            break;
                        }
                    }
                }

                //Get the planner answer exposed in the solution Output File
                File outputFile = new File(workingDirectory, solutionFile);

                if (outputFile.exists()) {
                    //Get output
                    try {
                        for (String line : FileUtils.readLines(outputFile)) {
                            if(line.trim().isEmpty()){
                                continue;
                            }
                            if (!isLineAction(line)) {
                                unprocessedStatistics.add(line);
                            } else {
                                unprocessedPlan.add(line);
                            }
                        }
                    } catch (IOException ex) {
                        throw new PlanningException("Could not read planner output", ex);
                    }

                    //remove output solution file (only if the plan create it)
                    outputFile.delete();
                    //TODO check permission
                } else {
                    //if the planner signalled before that it found nothing, the file may  not exits and it's OK
                    if (!plannerFoundNoSolution) {
                        throw new PlanningException("Could not find the planner output solution file! \n");
                    }
                    //System.out.println(toolMessage);
                }

            }

            if (cancelled) {
                return null;
            }

            if(settings.getNoPlanFoundSignalType() == ENoPlanFoundSignalType.EMPTY_PLAN){
                plannerFoundNoSolution = unprocessedPlan.isEmpty();
            }
            
            return new UnprocessedPlanningResult(unprocessedPlan, unprocessedStatistics, consoleOutputBuilder.toString(), !plannerFoundNoSolution);

        } finally {
            // delete additional generated files
            for (String generatedFileName : settings.getAdditionalGeneratedFiles()) {
                File file = new File(workingDirectory, generatedFileName);
                if (file.exists()) {
                    // delete the file
                    file.delete();
                }
            }
        }
    }

    /**
     * This method parses the lines of a plan in text format to a XML structure
     * @param plan
     * @return the plan XML structure
     */
    private List<ActionDescription> parsePlanToActionDescription(List<String> plan) {
        List<ActionDescription> result = new ArrayList<ActionDescription>();


        for (int lineIndex = 0; lineIndex < plan.size(); lineIndex++) {
            String line = plan.get(lineIndex);
            
            ActionDescription action = new ActionDescription();

            //System.out.println(line);

            String actionInstance = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
            StringTokenizer st = new StringTokenizer(actionInstance);

            // the first token is the action name
            String actionName = st.nextToken();

            action.setName(PlanningUtils.normalizeIdentifier(actionName));

            // the other tokens are the parameters
            List<String> parameterValues = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                String parameterStr = st.nextToken();
                parameterValues.add(PlanningUtils.normalizeIdentifier(parameterStr));
            }
            action.setParameters(parameterValues);
            
            int colonIndex = line.indexOf(':');
            String startTimeStr;
            
            // set the startTime name
            if(colonIndex > 0){
                 startTimeStr = line.substring(0, colonIndex);
            } else {
                startTimeStr = new StringTokenizer(line).nextToken();
            }
            
            if(!startTimeStr.isEmpty()){
                action.setStartTime(Double.parseDouble(startTimeStr));                
            }


            // set the action duration
            String durationStr = "1";
            if (line.indexOf('[') > - 1) {
                durationStr = line.substring(line.indexOf('[') + 1, line.lastIndexOf(']'));
            }
            action.setDuration(Double.parseDouble(durationStr));
            action.setNotes("");

            result.add(action);


        }

        return result;
    }

    private PlanningStatistics parseStatistics(List<String> statistic) {

        PlanningStatistics planningStatistics = new PlanningStatistics();

        for (String line : statistic) {

            String keyword;
            String value;
            if (line.indexOf(' ') > -1) {
                // there is a value
                keyword = line.substring(0, line.indexOf(' ')).trim();
                value = line.substring(line.indexOf(' '), line.length()).trim();
            } else {
                keyword = line;
                value = "";
            }

            if (value.isEmpty()) {
                continue;
            }

            if (keyword.equals("Time")) {
                planningStatistics.setTime(Double.parseDouble(value));
            } else if (keyword.equals("ParsingTime")) {
                planningStatistics.setParsingTime(Double.parseDouble(value));
            } else if (keyword.equals("NrActions")) {
                planningStatistics.setNumberOfActions(Integer.parseInt(value));
            } else if (keyword.equals("MakeSpan")) {
                planningStatistics.setMakeSpan(Double.parseDouble(value));
            } else if (keyword.equals("MetricValue")) {
                planningStatistics.setMetricValue(Double.parseDouble(value));
            } else if (keyword.equals("PlanningTechnique")) {
                planningStatistics.setPlanningTechnique(value);
            } else {
                planningStatistics.addAdditionalStat(keyword + " " + value);
            }
        }
        return planningStatistics;

    }

    /**
     * Runs the planner with parameters specified in constructor.
     * @return an xml representation of the plan
     */
    @Override
    public IExternalPlanningResult executePlanner() {
        //set initial time
        long start_time = System.currentTimeMillis() - timeInIO;



        if (cancelled) {
            return null;
        }
        UnprocessedPlanningResult unprocessedResult = runPlanner(domainFile, problemFile);


        if (cancelled) {
            return null;
        }


        PlanningStatistics stats = parseStatistics(unprocessedResult.getStatistics());

        //6. set the plan
        //Element planNode = xmlPlan.getChild("plan");
        List<ActionDescription> actionDescriptions = parsePlanToActionDescription(unprocessedResult.getPlan());


        long time = System.currentTimeMillis() - start_time;

        if (cancelled) {
            return null;
        }

        return new ExternalPlanningResult(unprocessedResult.isPlanningSuccesful(), actionDescriptions, unprocessedResult.getConsoleOuptut(), stats, time);
    }

    @Override
    public synchronized void cancel() {
        if (!cancelled) {
            cancelled = true;
            if (process != null) {
                //plannerExecutableFile is set always before the process is set
                destroyProcess(process, plannerExecutableFile);
            }
        }
    }

    public void destroyProcess(Process process, File plannerRunFile) {
        if (process != null) {
            InputStream is = process.getInputStream();
            InputStream es = process.getErrorStream();
            process.destroy();

            //should read and discard all output
            try {
                IOUtils.copy(is, new NullOutputStream());
                IOUtils.copy(es, new NullOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(ItSimplePlanningProcess.class.getName()).log(Level.FINE, "Error consuming output:" + ex.getMessage(), ex);
            }

//            String operatingSystem = System.getProperty("os.name").toLowerCase();
//            if (operatingSystem.indexOf("linux") == 0) {
//                //kill process in linux with comand 'killall -9 <process_name>'
//                //System.out.println("Kill" );
//
//                if (plannerRunFile != null && plannerRunFile.exists()) {
//
//                    //System.out.println(plannerRunFile.getName());
//                    String filename = plannerRunFile.getName();
//                    if (!filename.trim().equals("")) {
//                        String[] command = new String[3];
//                        command[0] = "killall";
//                        command[1] = "-9";
//                        command[2] = filename;
//
//                        try {
//                            Runtime.getRuntime().exec(command);
//                        } catch (IOException ex) {
//                            Logger.getLogger(ItSimplePlanningProcess.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//
//                    }
//
//
//                }
//            }



        }
    }

    protected static class UnprocessedPlanningResult {

        List<String> plan;

        List<String> statistics;

        String consoleOuptut;

        boolean planningSuccesful;

        public UnprocessedPlanningResult(List<String> plan, List<String> statistics, String consoleOuptut, boolean planningSuccesful) {
            this.plan = plan;
            this.statistics = statistics;
            this.consoleOuptut = consoleOuptut;
            this.planningSuccesful = planningSuccesful;
        }

        public List<String> getPlan() {
            return plan;
        }

        public List<String> getStatistics() {
            return statistics;
        }

        public String getConsoleOuptut() {
            return consoleOuptut;
        }

        public boolean isPlanningSuccesful() {
            return planningSuccesful;
        }
    }
}
