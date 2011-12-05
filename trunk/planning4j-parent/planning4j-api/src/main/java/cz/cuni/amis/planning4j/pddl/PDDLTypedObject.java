/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j.pddl;

/**
 *
 * @author Martin Cerny
 */
public abstract class PDDLTypedObject {
    String name;
    PDDLType type;

    public PDDLTypedObject(String name) {
        this(name, PDDLType.OBJECT_TYPE);
    }
    
    
    public PDDLTypedObject(String name, PDDLType type) {
        if(type == null){
            throw new NullPointerException("Type cannot be null");
        }
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public PDDLType getType() {
        return type;
    }

    /**
     * Name with a neccessarry prefix or other stuff needed to represent this kind of typed object in pddl
     * @return 
     */
    public String getNameForPDDL(){
        return getName();
    }
        
    
    public String getStringForPDDL(){
        return getNameForPDDL() + " - " + getType().getTypeName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PDDLTypedObject other = (PDDLTypedObject) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
    
    
}
