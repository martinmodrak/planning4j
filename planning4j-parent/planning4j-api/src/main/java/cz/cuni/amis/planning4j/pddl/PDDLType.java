/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j.pddl;

/**
 *
 * @author Martin Cerny
 */
public class PDDLType {
    String typeName;
    PDDLType ancestor = null;

    public static PDDLType OBJECT_TYPE = new PDDLType("object", null);
    
    public PDDLType(String typeName) {
        this.typeName = typeName;
    }
    
    public PDDLType(String typeName, PDDLType ancestor) {
        this.typeName = typeName;
        this.ancestor = ancestor;
    }

    public PDDLType getAncestor() {
        return ancestor;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PDDLType other = (PDDLType) obj;
        if ((this.typeName == null) ? (other.typeName != null) : !this.typeName.equals(other.typeName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.typeName != null ? this.typeName.hashCode() : 0);
        return hash;
    }

    
    
}
