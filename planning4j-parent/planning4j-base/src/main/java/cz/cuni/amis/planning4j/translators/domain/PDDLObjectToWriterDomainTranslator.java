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

import cz.cuni.amis.planning4j.IPDDLObjectDomainProvider;
import cz.cuni.amis.planning4j.IPDDLWriterDomainProvider;
import cz.cuni.amis.planning4j.impl.AbstractDomainTranslator;
import cz.cuni.amis.planning4j.pddl.*;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Translator that writes PDDL objects {@link PDDLDomain} to a writer in PDDL format.
 * @author Martin Cerny
 */
public class PDDLObjectToWriterDomainTranslator extends AbstractDomainTranslator<IPDDLObjectDomainProvider, IPDDLWriterDomainProvider> {

    public PDDLObjectToWriterDomainTranslator() {
        super(IPDDLObjectDomainProvider.class, IPDDLWriterDomainProvider.class);
    }

    
    
    @Override
    public IPDDLWriterDomainProvider translateDomain(IPDDLObjectDomainProvider domain) {
        return new TranslatedDomainProvider(domain);
    }

    private class TranslatedDomainProvider implements IPDDLWriterDomainProvider{
        
        private IPDDLObjectDomainProvider domainProvider;

        public TranslatedDomainProvider(IPDDLObjectDomainProvider domainProvider) {
            this.domainProvider = domainProvider;
        }
        
        
        
        protected void writeRequirements(Writer writer) throws IOException {
            writer.write("\t(:requirements");
            for(PDDLRequirement req : getDomain().getRequirements()){
                writer.write(" :" + req.getPddlName()); 
            }
            writer.write(")\n");
        }

        protected PDDLDomain getDomain() {
            return domainProvider.getPDDLDomain();
        }

        protected void writeOneType(Writer writer, PDDLType type) throws IOException
        {
            String ancestorString;
            if(type.getAncestor() == null){
                ancestorString = "";
            } else {
                ancestorString = " - " + type.getAncestor().getTypeName();
            }
            writer.write(" " + type.getTypeName() + ancestorString);
        }

        protected void writeTypes(Writer writer) throws IOException{
            writer.write("\t(:types ");
            for(PDDLType type : getDomain().getTypes()){
                writeOneType(writer, type);
            }
            writer.write(")\n");
        }


        protected void writeOneConstant(Writer writer, PDDLObjectInstance constant) throws IOException {
            writer.write("\t\t" + constant.getStringForPDDL() + "\n");       
        }

        protected void writeConstants(Writer writer) throws IOException {
            writer.write("\t(:constants \n");
            for(PDDLObjectInstance constant: getDomain().getConstants()){
                writeOneConstant(writer, constant);
            }
            writer.write("\t)\n");
        }

        protected void writeParameters(Writer writer, List<PDDLParameter> parameters) throws IOException {
            for(PDDLParameter parameter : parameters){
                writer.write(" " + parameter.getStringForPDDL());
            }
        }

        protected void writeOnePredicate(Writer writer, PDDLPredicate predicate) throws IOException {
            writer.write("\t\t(" + predicate.getName());
            writeParameters(writer, predicate.getParameters());
            writer.write(")\n");
        }

        protected void writePredicates(Writer writer)throws IOException{
            writer.write("\t(:predicates \n");
            for(PDDLPredicate predicate: getDomain().getPredicates()){
                writeOnePredicate(writer, predicate);
            }
            writer.write("\t)\n");
        }

        protected void writeOneAction(Writer writer, PDDLAction action) throws IOException {
            writer.write("\t(:action " + action.getName() + "\n");
            List<PDDLParameter> parameters = action.getParameters();
            writer.write("\t\t:parameters (");
            writeParameters(writer, parameters);
            writer.write(")\n");
            String precondition = action.getPreconditionAsString();
            if(precondition != null && !precondition.isEmpty()){
                writer.write("\t\t:precondition (" + precondition + ")\n");
            }
            writer.write("\t\t:effect (" + action.getEffectAsString() + ")\n");
            writer.write("\t)\n");
        }

        protected void writeActions(Writer writer)throws IOException{
            for(PDDLAction action: getDomain().getActions()){
                writeOneAction(writer, action);
            }
        }


        
        
        @Override
        public void writeDomain(Writer writer) throws IOException{
            writer.write("(define (domain " + getDomain().getDomainName() + ")\n");
            writeRequirements(writer);
            if(!getDomain().getTypes().isEmpty()){
                writeTypes(writer);
            }
            if(!getDomain().getConstants().isEmpty()){
                writeConstants(writer);
            }
            if(!getDomain().getPredicates().isEmpty()){
                writePredicates(writer);
            }
            if(!getDomain().getActions().isEmpty()){
                writeActions(writer);
            }        
            writer.write(")");
        }
    
        
    }
}
