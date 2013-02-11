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

package cz.cuni.amis.planning4j.translators.problem;

import cz.cuni.amis.planning4j.IPDDLFileProblemProvider;
import cz.cuni.amis.planning4j.IPDDLStringProblemProvider;
import cz.cuni.amis.planning4j.IPDDLWriterProblemProvider;
import cz.cuni.amis.planning4j.impl.ChainProblemTranslator;

/**
 *
 * @author Martin Cerny
 */
public class PDDLStringToFileProblemTranslator extends ChainProblemTranslator<IPDDLStringProblemProvider, IPDDLWriterProblemProvider, IPDDLFileProblemProvider> {

    public PDDLStringToFileProblemTranslator() {
        super(new PDDLStringToWriterProblemTranslator(), new PDDLWriterToFileProblemTranslator(), IPDDLStringProblemProvider.class, IPDDLFileProblemProvider.class);
    }

}
