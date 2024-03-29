/*
 * Copyright (C) 2011 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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
package cz.cuni.amis.planning4j.pddl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An object encapsulation a PDDL problem.
 * @author Martin Cerny
 */
public class PDDLProblem {
    String problemName;
    String domainName;
    
    List<PDDLObjectInstance> objects;
    List<String> initialLiterals;
    List<String> goalConditions;
    private boolean minimizeActionCosts = false;

    public PDDLProblem(String problemName, String domainName) {
        this.problemName = problemName;
        this.domainName = domainName;
        initialLiterals = Collections.EMPTY_LIST;       
        objects = new ArrayList<PDDLObjectInstance>();
    }

    public void setGoalCondition(String goalCondition) {
        this.goalConditions = Collections.singletonList(goalCondition);
    }

    public void setInitialLiterals(List<String> initialLiterals) {
        this.initialLiterals = initialLiterals;
    }

    public void setInitialLiterals(String ... initialLiterals) {
        this.initialLiterals = Arrays.asList(initialLiterals);
    }

    public boolean isMinimizeActionCosts() {
        return minimizeActionCosts;
    }

    public void setMinimizeActionCosts(boolean minimizeActionCosts) {
        this.minimizeActionCosts = minimizeActionCosts;
    }
    

    public String getDomainName() {
        return domainName;
    }

    public String getGoalConditionAsString() {
        if(goalConditions.isEmpty()){
            return "true";
        } else if (goalConditions.size() == 1){
            return goalConditions.get(0);
        } else {
            return PDDLOperators.makeAnd(goalConditions);
        }
    }

    public List<String> getInitialLiterals() {
        return initialLiterals;
    }

    public String getProblemName() {
        return problemName;
    }

    public List<String> getGoalConditions() {
        return goalConditions;
    }

    public void setGoalConditions(List<String> goalConditions) {
        this.goalConditions = goalConditions;
    }

    public List<PDDLObjectInstance> getObjects() {
        return objects;
    }   
    
    public void addObject(PDDLObjectInstance object){
        objects.add(object);
    }
    
    
    
    
    
}
