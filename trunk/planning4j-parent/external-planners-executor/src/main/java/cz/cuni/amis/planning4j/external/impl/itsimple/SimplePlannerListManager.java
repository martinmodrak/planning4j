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

import cz.cuni.amis.planning4j.pddl.PDDLRequirement;
import java.io.File;
import java.util.*;
import org.jdom.Element;

/**
 *
 * @author cernm6am
 */
public class SimplePlannerListManager {
    protected Element plannersXml;

    public SimplePlannerListManager(Element plannersXml) {
        this.plannersXml = plannersXml;
    }

    
    public ItSimplePlannerInformation readInformationFromXML(Element plannerElement){
        ItSimplePlannerInformation information = new ItSimplePlannerInformation();
        
        //TODO validate
        
        if(plannerElement.getChild("name") != null){
            information.setName(plannerElement.getChildText("name"));
        }
        if(plannerElement.getChild("version") != null){
            information.setVersion(plannerElement.getChildText("version"));
        }
        if(plannerElement.getChild("date") != null){
            information.setDate(plannerElement.getChildText("date"));
        }
        if(plannerElement.getChild("author") != null){
            information.setAuthor(plannerElement.getChildText("author"));
        }
        if(plannerElement.getChild("link") != null){
            information.setLink(plannerElement.getChildText("link"));
        }
        if(plannerElement.getChild("description") != null){
            information.setDescription(plannerElement.getChildText("description"));
        }
        
        final Element platformElement = plannerElement.getChild("platform");        
        EnumSet<EPlannerPlatform> platforms = EnumSet.noneOf(EPlannerPlatform.class);
        if(platformElement != null){
            for(EPlannerPlatform platform : EPlannerPlatform.values()){
                if(platformElement.getChild(platform.getXmlIdentifier()) != null){
                    platforms.add(platform);
                }
            }
        }
        information.setSupportedPlatforms(platforms);
        
        final Element requirementsElement = plannerElement.getChild("requirements");        
        Set<PDDLRequirement> requirements = EnumSet.noneOf(PDDLRequirement.class);
        if(requirementsElement != null){
            for(PDDLRequirement requirement : PDDLRequirement.values()){
                if(requirementsElement.getChild(requirement.getPddlName()) != null){
                    requirements.add(requirement);
                }
            }
        }
        information.setSupportedRequirements(requirements);        
        
        
        information.setSettings(readSettingsFromXML(plannerElement.getChild("settings")));
        
        return information;
    }        
    
    public ItSimplePlannerSettings readSettingsFromXML(Element settingsElement){
        ItSimplePlannerSettings settings = new ItSimplePlannerSettings();
        //TODO validate
        
        if(settingsElement.getChild("filePath") != null){
            settings.setExecutableFilePath(settingsElement.getChildText("filePath"));
        }
        
        final Element outputElement = settingsElement.getChild("output");
        if(outputElement != null){
            if(outputElement.getAttributeValue("hasOutputFile").equals("true")){
                settings.setHasOutputFile(true);
            } else {
                settings.setHasOutputFile(false);
            }
            Element outputFileElement = outputElement.getChild("outputFile");
            if(outputFileElement != null){
                if(outputFileElement.getChild("fileName") != null){
                    settings.setOutputFile(outputFileElement.getChildText("fileName"));
                }
                Element autoIncrElement = outputFileElement.getChild("fileNameAutomaticIncrement");
                if(autoIncrElement != null && !autoIncrElement.getText().isEmpty()){
                    settings.setOutputFileAutomaticIncrementSuffix(autoIncrElement.getText());
                } else {
                    settings.setOutputFileAutomaticIncrementSuffix(null);                    
                }
                
                Element argumentElement = outputFileElement.getChild("argument");
                if(argumentElement != null){
                    settings.setOutputFileNeedsArgument(argumentElement.getAttributeValue("needArgument").equals("true"));
                    settings.setOutputFileArgumentName(argumentElement.getAttributeValue("parameter"));
                }
                
                Element additionalFilesElement = outputFileElement.getChild("additionalGeneratedFiles");
                if(additionalFilesElement != null){
                    for(Object addFile : additionalFilesElement.getChildren("fileName")){
                        settings.addAdditionalGeneratedFile(((Element)addFile).getText());                    
                    }                            
                }
            } 
            
            Element consoleOutputElement = outputElement.getChild("consoleOutput");
            if(consoleOutputElement != null){
                
                Element planStartElement = consoleOutputElement.getChild("planStartIdentifier");
                settings.setConsoleOutputPlanStartIdentifier(planStartElement.getText());
                
                String startsAfterNLinesValue = planStartElement.getAttributeValue("startsAfterNlines", "0");
                if(!startsAfterNLinesValue.isEmpty()){
                    settings.setConsoleOutputStartsAfterNLines(Integer.parseInt(startsAfterNLinesValue));
                }
                
                settings.setConsoleOutputPlanEndIdentifier(consoleOutputElement.getChildText("planEndIdentifier"));
            }
                        
        }
        
        for(Object argument : settingsElement.getChild("arguments").getChildren()){
            Element argumentElement = (Element)argument;
            if(argumentElement.getName().equals("domain")){
                settings.setDomainArgumentName(argumentElement.getAttributeValue("parameter"));
            }
            else if(argumentElement.getName().equals("problem")){
                settings.setProblemArgumentName(argumentElement.getAttributeValue("parameter"));
            } else if(argumentElement.getName().equals("argument")) {
                if(argumentElement.getChildText("enable").equals("true")){
                    settings.addAdditionalArgument(new PlannerArgument(argumentElement.getAttributeValue("parameter"), 
                            argumentElement.getChildText("value")
                            ));                
                }
            }
        }
        
        List<Element> noPlanSignalElements = settingsElement.getChildren("noPlanFoundSignal");
        if (!noPlanSignalElements.isEmpty()) {
            for (Element noPlanSignalElement : noPlanSignalElements) {
                String type = noPlanSignalElement.getAttributeValue("type");
                if (type.equals("emptyPlan")) {
                    settings.addNoPlanFoundChecker(new EmptyPlanChecker());
                } else if (type.equals("outputText")) {
                    settings.addNoPlanFoundChecker(new OutputTextNoPlanFoundChecker(noPlanSignalElement.getText()));
                } else if (type.equals("errorCode")) {
                    settings.addNoPlanFoundChecker(new ExitCodeNoPlanFoundChecker(Integer.parseInt(noPlanSignalElement.getText())));
                }
            }

        } else {
            settings.addNoPlanFoundChecker(new EmptyPlanChecker());
        }
        
        return settings;
    }

    /**
     * This method verifies if planner requirements contains domain requirements
     * @param plannerRequirements, list if DOM Element List Planner Requirements
     * @param planners, list of DOM Element Planner Requirements
     * @return
     */
    public boolean containsRequirements(Element plannerElement, Set<PDDLRequirement> domainRequirements) {
        return containsRequirements(readInformationFromXML(plannerElement), domainRequirements);
    }
    
    public boolean containsRequirements(ItSimplePlannerInformation info, Set<PDDLRequirement> domainRequirements){
        return info.getSupportedRequirements().containsAll(domainRequirements);
    }

    /**
     * Get a planner for current platform by its name in XML.
     * @param name
     * @return The information for planner or null if no such planner was found
     */
    public ItSimplePlannerInformation getPlannerByName(String name) {
        for (ItSimplePlannerInformation plannerInfo : getPlannersList()) {
            if (plannerInfo.getName().equals(name) && runsOnOperatingSystem(plannerInfo)) {
                return plannerInfo;
            }
        }
        return null;
    }

    protected List<ItSimplePlannerInformation> getPlannersList() {
        List<ItSimplePlannerInformation> info = new ArrayList<ItSimplePlannerInformation>();
        for(Element plannerElement : (List<Element>)plannersXml.getChild("planners").getChildren("planner")){
            info.add(readInformationFromXML(plannerElement));
        }
        return info;
    }

    /**
     * Uses current working directory to call {@link #extractAndPreparePlanner(java.io.File, org.jdom.Element) }
     * @param selectedPlanner
     */
    public final void preparePlanner(Element selectedPlanner) {
        preparePlanner(new File("."), selectedPlanner);
    }

    /**
     * Prepares a planner for execution. simply translates info and calls {@link #preparePlanner(java.io.File, cz.cuni.amis.planning4j.external.impl.itsimple.ItSimplePlannerInformation) 
     * @param selectedPlanner
     * @param plannersDirectory directory where the planners were unpacked
     */
    public final void preparePlanner(File plannersDirectory, Element selectedPlanner){
        preparePlanner(plannersDirectory, readInformationFromXML(selectedPlanner));        
    }

    /**
     * Prepares a planner for execution. This implementation does nothing, should be overriden to provide further functionality
     * @param selectedPlanner
     * @param plannersDirectory directory where the planners were unpacked
     */
    public void preparePlanner(File plannersDirectory, ItSimplePlannerInformation selectedPlanner){
        
    }
    
    
    /**
     * @see #suggestPlanners(java.util.Set)
     * @param requirements
     * @return
     */
    public List<ItSimplePlannerInformation> suggestPlanners(PDDLRequirement... requirements) {
        return suggestPlanners(EnumSet.copyOf(Arrays.asList(requirements)));
    }

    /**
     * This method selects the planners that can deal with the given domain based on its requirements and runs on
     * current OS.
     * @return
     */
    public List<ItSimplePlannerInformation> suggestPlanners(Set<PDDLRequirement> requirements) {
        List<ItSimplePlannerInformation> suggestedPlanners = new ArrayList<ItSimplePlannerInformation>();
        for (ItSimplePlannerInformation plannerInfo : getPlannersList()) {
            if (this.containsRequirements(plannerInfo, requirements) && this.runsOnOperatingSystem(plannerInfo)) {
                suggestedPlanners.add(plannerInfo);
            }
        }
        return suggestedPlanners;
    }

    /**
     * This method gets the operating system name on which the program is running
     * @return
     */
    protected EPlannerPlatform getOperatingSystem() {
        return ItSimpleUtils.getOperatingSystem();
    }

    /**
     * This method verifies if planner can be run on operating system
     * @param planner
     * @return
     */
    protected boolean runsOnOperatingSystem(ItSimplePlannerInformation plannerInfo) {
        return plannerInfo.getSupportedPlatforms().contains(getOperatingSystem());        
    }
    
}
