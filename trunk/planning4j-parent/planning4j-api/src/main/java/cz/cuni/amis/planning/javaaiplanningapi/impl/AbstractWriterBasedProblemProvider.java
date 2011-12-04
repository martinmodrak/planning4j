/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.javaaiplanningapi.impl;

import cz.cuni.amis.planning.javaaiplanningapi.IProblemProvider;
import cz.cuni.amis.planning.javaaiplanningapi.PlanningIOException;
import java.io.IOException;
import java.io.StringWriter;

/**
 *
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
    
}
