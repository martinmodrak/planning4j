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

/**
 * Abstract ancestor of all PDDL objects, that exhibit types.
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
