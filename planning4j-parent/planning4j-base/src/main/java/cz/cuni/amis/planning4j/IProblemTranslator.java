/*
 * Copyright (C) 2012 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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

package cz.cuni.amis.planning4j;

/**
 * An interface for translators between problem representations. Translators should be
 * advertised with Java SPI to be automatically discovered by methods in {@link Planning4JUtils}
 * @author Martin Cerny
 */
public interface IProblemTranslator<SOURCE extends IProblemProvider, DESTINATION extends IProblemProvider> {
    public Class<SOURCE> getSourceProblemClass();
    public Class<DESTINATION> getDestinationProblemClass();   
    
    /**
     * Translates the problem. The resulting problem may (and may not) reflect
     * changes made in the original problem after the translation has been done i.e. the returned
     * object might be a kind of proxy to the original problem.
    * @param problem
    * @return 
    */    
    public DESTINATION translateProblem(SOURCE problem);
}
