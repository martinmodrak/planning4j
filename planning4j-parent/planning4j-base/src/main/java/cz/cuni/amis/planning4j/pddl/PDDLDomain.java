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
import java.util.List;
import java.util.Set;

/**
 * An object encapsulating a whole PDDL domain
 * @author Martin Cerny
 */
public class PDDLDomain {
    
    String domainName;
    Set<PDDLRequirement> requirements;
    List<PDDLType> types = new ArrayList<PDDLType>();
    List<PDDLObjectInstance> constants = new ArrayList<PDDLObjectInstance>();
    List<PDDLPredicate> predicates = new ArrayList<PDDLPredicate>();
    List<PDDLAction> actions = new ArrayList<PDDLAction>();
    List<PDDLFunction> functions = new ArrayList<PDDLFunction>();

    public PDDLDomain(String domainName, Set<PDDLRequirement> requirements) {
        this.domainName = domainName;
        this.requirements = requirements;
    }

    
    
    public void setRequirements(Set<PDDLRequirement> requirements) {
        this.requirements = requirements;    
    }
    
    public void addType(PDDLType type){
        types.add(type);
    }
    
    public void addConstant(PDDLObjectInstance constant){
        constants.add(constant);
    }
    
    public void addFunction(PDDLFunction function){
        functions.add(function);        
    }
    
    public void addPredicate(PDDLPredicate predicate){
        predicates.add(predicate);
    }
    
    public void addAction(PDDLAction action){
        actions.add(action);
    }

    public List<PDDLAction> getActions() {
        return actions;
    }

    public List<PDDLObjectInstance> getConstants() {
        return constants;
    }

    public String getDomainName() {
        return domainName;
    }

    public List<PDDLPredicate> getPredicates() {
        return predicates;
    }

    public Set<PDDLRequirement> getRequirements() {
        return requirements;
    }

    public List<PDDLType> getTypes() {
        return types;
    }

    public List<PDDLFunction> getFunctions() {
        return functions;
    }
    
    
}
