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
 * A class that allows to chain two translators to form a new one.
 * @author Martin Cerny
 */
public abstract class ChainProblemTranslator <SOURCE extends IProblemProvider, MIDDLE extends IProblemProvider, DESTINATION extends IProblemProvider> 
    extends AbstractProblemTranslator<SOURCE, DESTINATION> {


    private IProblemTranslator<SOURCE, MIDDLE> translator1;
    private IProblemTranslator<MIDDLE, DESTINATION> translator2;

    public ChainProblemTranslator(IProblemTranslator<SOURCE, MIDDLE> translator1, IProblemTranslator<MIDDLE, DESTINATION> translator2, Class<SOURCE> sourceProblemClass, Class<DESTINATION> destinationProblemClass) {
        super(sourceProblemClass, destinationProblemClass);
        this.translator1 = translator1;
        this.translator2 = translator2;
    }

    @Override
    public DESTINATION translateProblem(SOURCE domain) {
        return translator2.translateProblem(translator1.translateProblem(domain));
    }
    
    
}
