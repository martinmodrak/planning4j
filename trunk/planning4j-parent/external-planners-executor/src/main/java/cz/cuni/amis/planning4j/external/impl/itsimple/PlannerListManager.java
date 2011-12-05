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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jdom.Element;

/**
 *
 * @author Matheus, Martin Cerny
 */
public class PlannerListManager {

    private Element plannersXml;
    
    public PlannerListManager(Element plannersXml) {
        this.plannersXml = plannersXml;
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
      
    public List<Element> suggestPlanners(PDDLRequirement ... requirements){
        return suggestPlanners(EnumSet.copyOf(Arrays.asList(requirements)));
    }
    
    /**
     * This method select the planners that can deal with the given domain based on its requirements and runs on
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
     * This method verify if planner requirements contains domain requirements
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
     * This method verify if planner can be run on operating system
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
     * This method get the operating system name on which the itSIMPLE running
     * @return
     */
    private String getOperatingSystem() {

        String operatingSystem = System.getProperty("os.name").toLowerCase();

        if (operatingSystem.indexOf("linux") == 0) {
            operatingSystem = "linux";
        } else if (operatingSystem.indexOf("windows") == 0) {
            operatingSystem = "windows";
        } else if (operatingSystem.indexOf("mac") == 0) {
            operatingSystem = "mac";
        }

        return operatingSystem;
    }
}
