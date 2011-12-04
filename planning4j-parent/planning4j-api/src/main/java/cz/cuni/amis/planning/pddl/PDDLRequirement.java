/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.pddl;

/**
 *
 * @author Martin Cerny
 */
public enum PDDLRequirement {
    //TODO full list of requirements
    STRIPS("strips"), EQUAILTY("equality"), TYPING("typing"), CONDITIONAL_EFFECTS("conditional-effects");
    /**
     * Name in pddl (without leading colon)
     */
    String pddlName;

    private PDDLRequirement(String pddlName) {
        this.pddlName = pddlName;
    }

    public String getPddlName() {
        return pddlName;
    }
    
}
