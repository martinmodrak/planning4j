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
package cz.cuni.amis.planning4j.impl;

import cz.cuni.amis.planning4j.IProblemProvider;
import cz.cuni.amis.planning4j.PlanningIOException;
import cz.cuni.amis.planning4j.pddl.PDDLProblem;
import java.io.IOException;
import java.io.StringWriter;

/**
 * A simple implementation of {@link IProblemProvider} that delegates everything
 * to a writer implementation. Writer based implementations will likely perform better
 * with external planners.
 * @author Martin Cerny
 */

public abstract class AbstractWriterBasedProblemProvider implements IProblemProvider{

    @Override
    public String getProblemAsString() {
        StringWriter stringWriter = new StringWriter();
        try {
            writeProblem(stringWriter);
            return stringWriter.toString();
        } catch(IOException ex){
            throw new PlanningIOException(ex);
        }
    }

    @Override
    public PDDLProblem getProblemAsPDDL() {
        return null;
    }
    
    
    
}
