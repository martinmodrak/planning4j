/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j.pddl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Martin Cerny
 */
public class PDDLDomain {
    
    String domainName;
    Set<PDDLRequirement> requirements;
    List<PDDLType> types = new ArrayList<PDDLType>();
    List<PDDLConstant> constants = new ArrayList<PDDLConstant>();
    List<PDDLPredicate> predicates = new ArrayList<PDDLPredicate>();
    List<PDDLAction> actions = new ArrayList<PDDLAction>();

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
    
    public void addConstant(PDDLConstant constant){
        constants.add(constant);
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

    public List<PDDLConstant> getConstants() {
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
    
    
}
