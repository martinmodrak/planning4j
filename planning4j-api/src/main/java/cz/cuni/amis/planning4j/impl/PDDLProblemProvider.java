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

import cz.cuni.amis.planning4j.pddl.PDDLProblem;
import java.io.IOException;
import java.io.Writer;

/**
 * A problem provider, that gets PDDL from {@link PDDLProblem} object.
 * @author Martin Cerny
 */

public class PDDLProblemProvider extends AbstractWriterBasedProblemProvider{
    private PDDLProblem problem;

    public PDDLProblemProvider(PDDLProblem problem) {
        this.problem = problem;
    }
    
    @Override
    public void writeProblem(Writer writer) throws IOException {
        writer.write("(define (problem " + problem.getProblemName() + ") (:domain " + problem.getDomainName() + ")\n");
        if(!problem.getInitialLiterals().isEmpty()){
            writer.write("\t(:init \n");
            for(String literal : problem.getInitialLiterals()){
                writer.write("\t\t(" + literal + ")\n");
            }
            writer.write("\t)\n");
        }
        writer.write("\t(:goal (" + problem.getGoalCondition() + "))\n");
        writer.write(")\n");
    }
    
}
