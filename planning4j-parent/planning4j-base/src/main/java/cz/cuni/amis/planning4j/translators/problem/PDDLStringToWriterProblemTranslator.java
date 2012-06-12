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
import cz.cuni.amis.planning4j.impl.AbstractProblemTranslator;
import java.io.IOException;
import java.io.Writer;

/**
 * A simple translator that writes a string in PDDL to a writer.
 * @author Martin Cerny
 */
public class PDDLStringToWriterProblemTranslator extends AbstractProblemTranslator<IPDDLStringProblemProvider, IPDDLWriterProblemProvider> {

    public PDDLStringToWriterProblemTranslator() {
        super(IPDDLStringProblemProvider.class, IPDDLWriterProblemProvider.class);
    }

    @Override
    public IPDDLWriterProblemProvider translateProblem(IPDDLStringProblemProvider domain) {
        return new TranslatedProblemProvider(domain);
    }
    
    

    private class TranslatedProblemProvider implements IPDDLWriterProblemProvider {
        private IPDDLStringProblemProvider domainProvider;

        public TranslatedProblemProvider(IPDDLStringProblemProvider domainProvider) {
            this.domainProvider = domainProvider;
        }

        
        
        @Override
        public void writeProblem(Writer writer) throws IOException {
            writer.write(domainProvider.getProblemAsPDDLString());            
        }
        
        
    }
}
