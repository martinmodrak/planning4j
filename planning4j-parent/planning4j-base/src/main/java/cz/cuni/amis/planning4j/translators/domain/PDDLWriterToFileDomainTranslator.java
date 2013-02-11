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

package cz.cuni.amis.planning4j.translators.domain;

import cz.cuni.amis.planning4j.IPDDLFileDomainProvider;
import cz.cuni.amis.planning4j.IPDDLWriterDomainProvider;
import cz.cuni.amis.planning4j.PlanningIOException;
import cz.cuni.amis.planning4j.impl.AbstractDomainTranslator;
import cz.cuni.amis.planning4j.impl.PDDLFileDomainProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * A simple translator that stores data written to a writer in a string to be passed to the planner.
 * @author Martin Cerny
 */
public class PDDLWriterToFileDomainTranslator extends AbstractDomainTranslator<IPDDLWriterDomainProvider, IPDDLFileDomainProvider> {
    public PDDLWriterToFileDomainTranslator(){
        super(IPDDLWriterDomainProvider.class, IPDDLFileDomainProvider.class);
    }

    @Override
    public IPDDLFileDomainProvider translateDomain(IPDDLWriterDomainProvider domain) {
        FileWriter writer = null;
        try {
            File tmpFile = File.createTempFile("domain", ".pddl");
            writer = new FileWriter(tmpFile);
            domain.writeDomain(writer);
            writer.close();
            return new PDDLFileDomainProvider(tmpFile);
        } catch(IOException ex){
            try {
                if(writer != null){
                    writer.close();                
                }
            } catch (IOException ignored){
                //ignore
            }            
            throw new PlanningIOException(ex);
        }

    }
    
    
}
