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

import cz.cuni.amis.planning4j.pddl.PDDLAction;
import cz.cuni.amis.planning4j.pddl.PDDLConstant;
import cz.cuni.amis.planning4j.pddl.PDDLDomain;
import cz.cuni.amis.planning4j.pddl.PDDLParameter;
import cz.cuni.amis.planning4j.pddl.PDDLPredicate;
import cz.cuni.amis.planning4j.pddl.PDDLRequirement;
import cz.cuni.amis.planning4j.pddl.PDDLType;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * A domain provider, that gets PDDL from {@link PDDLDomain} object.
 * @author Martin Cerny
 */
public class PDDLDomainProvider extends AbstractWriterBasedDomainProvider {
    private PDDLDomain domain;

    public PDDLDomainProvider(PDDLDomain domain) {
        this.domain = domain;
    }

    protected void writeRequirements(Writer writer) throws IOException {
        writer.write("\t(:requirements");
        for(PDDLRequirement req : domain.getRequirements()){
            writer.write(" :" + req.getPddlName()); 
        }
        writer.write(")\n");
    }
    
    protected void writeOneType(Writer writer, PDDLType type) throws IOException
    {
        if(type.getAncestor() == null){
            throw new NullPointerException("User defined types must have a non-null ancestor");
        }
        writer.write(" " + type.getTypeName() + " - " + type.getAncestor().getTypeName());
    }
    
    protected void writeTypes(Writer writer) throws IOException{
        writer.write("\t(:types ");
        for(PDDLType type : domain.getTypes()){
            writeOneType(writer, type);
        }
        writer.write(")\n");
    }
    
    
    protected void writeOneConstant(Writer writer, PDDLConstant constant) throws IOException {
        writer.write("\t\t(" + constant.getStringForPDDL() + ")\n");       
    }
    
    protected void writeConstants(Writer writer) throws IOException {
        writer.write("\t(:constants \n");
        for(PDDLConstant constant: domain.getConstants()){
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
        for(PDDLPredicate predicate: domain.getPredicates()){
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
        for(PDDLAction action: domain.getActions()){
            writeOneAction(writer, action);
        }
    }

    
    @Override
    public void writeDomain(Writer writer) throws IOException{
        writer.write("(define (domain " + domain.getDomainName() + ")\n");
        writeRequirements(writer);
        if(!domain.getTypes().isEmpty()){
            writeTypes(writer);
        }
        if(!domain.getConstants().isEmpty()){
            writeConstants(writer);
        }
        if(!domain.getPredicates().isEmpty()){
            writePredicates(writer);
        }
        if(!domain.getActions().isEmpty()){
            writeActions(writer);
        }        
        writer.write(")");
    }
    
    
}
