/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.pddl;

/**
 *
 * @author Martin Cerny
 */
public class PDDLParameter extends PDDLTypedObject{

    public PDDLParameter(String name, PDDLType type) {
        super(name, type);
    }

    public PDDLParameter(String name) {
        super(name);
    }

    @Override
    public String getNameForPDDL() {
        return "?" + getName();
    }
    
    
}
