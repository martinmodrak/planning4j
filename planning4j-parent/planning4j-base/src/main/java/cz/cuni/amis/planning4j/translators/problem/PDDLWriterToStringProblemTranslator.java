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

package cz.cuni.amis.planning4j.translators.problem;

import cz.cuni.amis.planning4j.IPDDLStringProblemProvider;
import cz.cuni.amis.planning4j.IPDDLWriterProblemProvider;
import cz.cuni.amis.planning4j.PlanningIOException;
import cz.cuni.amis.planning4j.impl.AbstractProblemTranslator;
import cz.cuni.amis.planning4j.impl.PDDLStringProblemProvider;
import java.io.IOException;
import java.io.StringWriter;

/**
 *
 * @author Martin Cerny
 */
public class PDDLWriterToStringProblemTranslator extends AbstractProblemTranslator<IPDDLWriterProblemProvider, IPDDLStringProblemProvider> {
    public PDDLWriterToStringProblemTranslator(){
        super(IPDDLWriterProblemProvider.class, IPDDLStringProblemProvider.class);
    }

    @Override
    public IPDDLStringProblemProvider translateProblem(IPDDLWriterProblemProvider domain) {
        StringWriter stringWriter = new StringWriter();
        try {
            domain.writeProblem(stringWriter);
            return new PDDLStringProblemProvider(stringWriter.toString());
        } catch(IOException ex){
            throw new PlanningIOException(ex);
        }

    }
    
    
}
