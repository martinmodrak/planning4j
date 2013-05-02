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

import cz.cuni.amis.planning4j.IPDDLObjectProblemProvider;
import cz.cuni.amis.planning4j.IPDDLWriterProblemProvider;
import cz.cuni.amis.planning4j.impl.AbstractProblemTranslator;
import cz.cuni.amis.planning4j.pddl.PDDLObjectInstance;
import cz.cuni.amis.planning4j.pddl.PDDLProblem;
import java.io.IOException;
import java.io.Writer;

/**
 * Translator that writes PDDL objects {@link PDDLProblem} to a writer in PDDL format.
 * @author Martin Cerny
 */
public class PDDLObjectToWriterProblemTranslator extends AbstractProblemTranslator<IPDDLObjectProblemProvider, IPDDLWriterProblemProvider> {

    public PDDLObjectToWriterProblemTranslator() {
        super(IPDDLObjectProblemProvider.class, IPDDLWriterProblemProvider.class);
    }

    
    
    @Override
    public IPDDLWriterProblemProvider translateProblem(IPDDLObjectProblemProvider problem) {
        return new TranslatedProblemProvider(problem);        
    }

    private class TranslatedProblemProvider implements IPDDLWriterProblemProvider {

        private IPDDLObjectProblemProvider problemProvider;

        public TranslatedProblemProvider(IPDDLObjectProblemProvider problemProvider) {
            this.problemProvider = problemProvider;
        }

        
        

        @Override
        public void writeProblem(Writer writer) throws IOException {
            writer.write("(define (problem " + getProblem().getProblemName() + ") (:domain " + getProblem().getDomainName() + ")\n");
            if (!getProblem().getObjects().isEmpty()){
                writer.write("\t(:objects \n");
                for(PDDLObjectInstance object : getProblem().getObjects()){
                    writer.write("\t\t" + object.getStringForPDDL() + "\n");                    
                }
                writer.write("\t)\n");
            }
            if (!getProblem().getInitialLiterals().isEmpty()) {
                writer.write("\t(:init \n");
                for (String literal : getProblem().getInitialLiterals()) {
                    writer.write("\t\t(" + literal + ")\n");
                }
                if(getProblem().isMinimizeActionCosts()){
                    writer.write("\t(= (total-cost) 0)\n");
                }
                
                writer.write("\t)\n");
            }
            writer.write("\t(:goal (" + getProblem().getGoalConditionAsString() + "))\n");
            if(getProblem().isMinimizeActionCosts()){
                writer.write("\t(:metric minimize (total-cost))\n");
            }
            writer.write(")\n");
        }

        protected PDDLProblem getProblem() {
            return problemProvider.getPDDLProblem();
        }
    }
}
