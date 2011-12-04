/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.pddl;

/**
 *
 * @author Martin Cerny
 */
public class PDDLConstant extends PDDLTypedObject{

    public PDDLConstant(String name, PDDLType type) {
        super(name, type);
    }

    public PDDLConstant(String name) {
        super(name);
    }
    
}
