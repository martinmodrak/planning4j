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

package cz.cuni.amis.planning4j.impl;

import cz.cuni.amis.planning4j.IProblemProvider;
import cz.cuni.amis.planning4j.IProblemTranslator;

/**
 * A problem translator stub that keeps track of the classes for translation
 * @author Martin Cerny
 */
public abstract class AbstractProblemTranslator<SOURCE extends IProblemProvider, DESTINATION extends IProblemProvider> implements IProblemTranslator<SOURCE, DESTINATION> {
    private Class<SOURCE> sourceProblemClass;
    private Class<DESTINATION> destinationProblemClass;

    public AbstractProblemTranslator(Class<SOURCE> sourceProblemClass, Class<DESTINATION> destinationProblemClass) {
        this.sourceProblemClass = sourceProblemClass;
        this.destinationProblemClass = destinationProblemClass;
    }    
    
    @Override
    public Class<DESTINATION> getDestinationProblemClass() {
        return destinationProblemClass;
    }

    @Override
    public Class<SOURCE> getSourceProblemClass() {
        return sourceProblemClass;
    }
    
    
}
