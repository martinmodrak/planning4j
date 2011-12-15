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
package cz.cuni.amis.planning4j.utils;

/**
 * A collection of utility methods employed by most planners.
 * @author Martin Cerny
 */
public class PlanningUtils {
    
    /**
     * Normalizes an identifier. Some planners modify indentifier names,
     * namely turn them into upper/lower case and/or replace minuses with underscores or
     * vice-versa. Identifier returned in planning result is guaranteed to be equal
     * to a normalized form of the identifier as it was submitted to the planner.
     * The identifier is returned in uppercase and minuses are translated to underscores.
     * @param identifier
     * @return 
     */
    public static String normalizeIdentifier(String identifier){
        return identifier.toUpperCase().replace('-', '_');
    }
}
