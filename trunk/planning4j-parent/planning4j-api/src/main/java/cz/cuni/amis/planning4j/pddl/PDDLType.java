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
 * An object represnenting a type in PDDL (either user defined or buil-in)
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
