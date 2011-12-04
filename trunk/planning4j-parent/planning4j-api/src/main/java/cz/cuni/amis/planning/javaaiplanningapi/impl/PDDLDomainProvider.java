/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.javaaiplanningapi.impl;

import cz.cuni.amis.planning.pddl.PDDLAction;
import cz.cuni.amis.planning.pddl.PDDLConstant;
import cz.cuni.amis.planning.pddl.PDDLDomain;
import cz.cuni.amis.planning.pddl.PDDLParameter;
import cz.cuni.amis.planning.pddl.PDDLPredicate;
import cz.cuni.amis.planning.pddl.PDDLRequirement;
import cz.cuni.amis.planning.pddl.PDDLType;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 *
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
        String precondition = action.getPrecondition();
        if(precondition != null && !precondition.isEmpty()){
            writer.write("\t\t:precondition (" + precondition + ")\n");
        }
        writer.write("\t\t:effect (" + action.getEffect() + ")\n");
        writer.write("\t)\n");
    }
    
    protected void writeActions(Writer writer)throws IOException{
        for(PDDLAction action: domain.getActions()){
            writeOneAction(writer, action);
        }
    }

    
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
