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
 * Authors:	Matheus Haddad,
 *              Tiago S. Vaquero, Martin Cerny
 *			.
 **/
package cz.cuni.amis.planning4j.external.impl.itsimple;

import cz.cuni.amis.planning4j.pddl.PDDLRequirement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.jdom.Element;

/**
 * A class that works with XML definition of planners in ItSimple formats
 * and allows to select a suitable planner for current platform.
 * @author Matheus, Martin Cerny
 */
public class PlannerListManager {

    private Element plannersXml;
    
    public PlannerListManager(Element plannersXml) {
        this.plannersXml = plannersXml;
    }

    /**
     * Uses current working directory to call {@link #extractAndPreparePlanner(java.io.File, org.jdom.Element) }
     * @param selectedPlanner 
     */
    public void extractAndPreparePlanner(Element selectedPlanner){
        extractAndPreparePlanner(new File("."), selectedPlanner);
    }
    
    /**
     * If the planner does not exist in specified location, it is extracted from the planners pack,
     * then {@link #preparePlanner(java.io.File, org.jdom.Element) } is called.
     * @param targetDirectory the directory where the binary will be stored
     * @param selectedPlanner 
     * @throws ItSimplePlanningException if there is an IO error
     */
    public void extractAndPreparePlanner(File targetDirectory, Element selectedPlanner) {

        File binaryFile = ItSimpleUtils.getPlannerExecutableFile(targetDirectory, selectedPlanner);
        
        if(!binaryFile.exists()){        
            final String plannerRelativeFileName = ItSimpleUtils.getPlannerRelativeFileName(selectedPlanner);
            String plannerResourcePath = "/" + plannerRelativeFileName;
            InputStream inputStream = getClass().getResourceAsStream(plannerResourcePath);
            if(inputStream == null){
                throw new ItSimplePlanningException("Could not find planner resource on classpath. Resource path:" + plannerResourcePath);
            }
            if(!binaryFile.getParentFile().exists() && !binaryFile.getParentFile().mkdirs()){
                throw new ItSimplePlanningException("Could not create parent dirs for planner " + binaryFile);                
            }
            try {
                FileOutputStream outputfilestream = new FileOutputStream(binaryFile);
                IOUtils.copy(inputStream, outputfilestream);
                outputfilestream.close();
            } catch (IOException ex){
                throw new ItSimplePlanningException("Could not extract planner to " + binaryFile, ex);
            }
        }
        
        preparePlanner(targetDirectory, selectedPlanner);        
        
    }

    /**
     * Uses current working directory to call {@link #extractAndPreparePlanner(java.io.File, org.jdom.Element) }
     * @param selectedPlanner 
     */
    public void preparePlanner(Element selectedPlanner){
        preparePlanner(new File("."), selectedPlanner);        
    }
        
    /**
     * Prepares a planner for execution.
     * As of now, it only tries to set executable permission on the planner file on linux
     * systems.
     * @param selectedPlanner 
     * @param plannersDirectory directory where the planners were unpacked
     */
    public void preparePlanner(File plannersDirectory, Element selectedPlanner){
        if(ItSimpleUtils.getOperatingSystem().equals("linux")){
        File plannerFile = ItSimpleUtils.getPlannerExecutableFile(plannersDirectory, selectedPlanner);
        try {
            Runtime.getRuntime().exec(new String[] { "chmod", "+x", plannerFile.getAbsolutePath()});
        } catch (IOException ex) {
            Logger.getLogger(PlannerListManager.class.getName()).log(Level.SEVERE, "Could not set planner file permissions", ex);
        }
            
        }
    }
    
    /**
     * Get a planner for current platform by its name in XML.
     * @param name
     * @return The XML element for planner or null if no such planner was found
     */
    public Element getPlannerByName(String name){
        for(Element planner : getPlannersList()){
            if(planner.getChild("name").getText().equals(name) && runOnOperatingSystem(planner)){
                return planner;
            }
        }
        return null;
    }
      
    /**
     * @see #suggestPlanners(java.util.Set) 
     * @param requirements
     * @return 
     */
    public List<Element> suggestPlanners(PDDLRequirement ... requirements){
        return suggestPlanners(EnumSet.copyOf(Arrays.asList(requirements)));
    }
    
    /**
     * This method selects the planners that can deal with the given domain based on its requirements and runs on
     * current OS.
     * @return
     */
    public List<Element> suggestPlanners(Set<PDDLRequirement> requirements) {

        List<Element> suggestedPlanners = new ArrayList<Element>();

        List<Element> planners = getPlannersList();

        for(Element planner : planners){
            if (this.containsRequirements(planner, requirements) && this.runOnOperatingSystem(planner)) {
                suggestedPlanners.add(planner);
            } 
        }
        return suggestedPlanners;

    }

    protected List<Element> getPlannersList() {
        return plannersXml.getChild("planners").getChildren("planner");
    }

    /**
     * This method verifies if planner requirements contains domain requirements
     * @param plannerRequirements, list if DOM Element List Planner Requirements
     * @param planners, list of DOM Element Planner Requirements
     * @return
     */
    public boolean containsRequirements(Element plannerElement, Set<PDDLRequirement> domainRequirements) {

        List<Element> plannerRequirements = plannerElement.getChild("requirements").getChildren();

        for(PDDLRequirement domainRequirement : domainRequirements){
            
            boolean plannerContainsRequirement = false;
            for(Element plannerRequirement : plannerRequirements){
                if(plannerRequirement.getName().equals(domainRequirement.getPddlName())){
                    plannerContainsRequirement = true;
                    break;
                }
            }

            if(!plannerContainsRequirement){
                return false;
            }

        }

        return true;
    }

    /**
     * This method verifies if planner can be run on operating system
     * @param planner, DOM Planner Element
     * @return
     */
    private boolean runOnOperatingSystem(Element plannerElement) {

        boolean _runOnOperatingSystem = false;
        String thisOperatingSystem = this.getOperatingSystem();

        List operatingSystems = plannerElement.getChild("platform").getChildren();
        Iterator soIterator = operatingSystems.iterator();
        while (soIterator.hasNext()) {
            Element plataform = (Element) soIterator.next();
            _runOnOperatingSystem = _runOnOperatingSystem || (plataform.getName().equals(thisOperatingSystem));
        }

        return _runOnOperatingSystem;
    }

    /**
     * This method gets the operating system name on which the program is running
     * @return
     */
    private String getOperatingSystem() {
        return ItSimpleUtils.getOperatingSystem();
    }
}
