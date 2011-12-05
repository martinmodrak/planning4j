/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j.impl;

import cz.cuni.amis.planning4j.pddl.PDDLProblem;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Martin Cerny
 */
public class PDDLProblemProvider extends AbstractWriterBasedProblemProvider{
    private PDDLProblem problem;

    public PDDLProblemProvider(PDDLProblem problem) {
        this.problem = problem;
    }
    
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
