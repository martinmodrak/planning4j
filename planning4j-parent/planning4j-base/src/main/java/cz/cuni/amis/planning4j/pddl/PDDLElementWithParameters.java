/*
 * Copyright (C) 2013 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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

import java.util.List;

/**
 *
 * @author Martin Cerny
 */
public abstract class PDDLElementWithParameters {
    protected String name;
    protected List<PDDLParameter> parameters;

    public PDDLElementWithParameters(String name, List<PDDLParameter> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public List<PDDLParameter> getParameters() {
        return parameters;
    }

    public String stringAfterSubstitution(Object... substituents) {
        if (substituents.length != getParameters().size()) {
            throw new IllegalArgumentException("Predicate " + getName() + " requires " + getParameters().size() + " parameters, got " + substituents.length);
        }
        StringBuilder sb = new StringBuilder(getName());
        if (getParameters().size() > 0) {
            for (int i = 0; i < substituents.length; i++) {
                sb.append(" ");
                if (substituents[i] instanceof String) {
                    sb.append(substituents[i]);
                } else if (substituents[i] instanceof PDDLTypedObject) {
                    sb.append(((PDDLObjectInstance) substituents[i]).getNameForPDDL());
                } else {
                    throw new IllegalArgumentException("Unsupported substituent type: " + substituents[i].getClass());
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return name + "/" + parameters.size();
    }
    
}
