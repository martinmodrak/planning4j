/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.javaaiplanningapi.impl;

import cz.cuni.amis.planning.javaaiplanningapi.IDomainProvider;
import cz.cuni.amis.planning.javaaiplanningapi.PlanningIOException;
import java.io.IOException;
import java.io.StringWriter;

/**
 *
 * @author Martin Cerny
 */
public abstract class AbstractWriterBasedDomainProvider implements IDomainProvider{

    @Override
    public String getDomainAsString() {
        StringWriter stringWriter = new StringWriter();
        try {
            writeDomain(stringWriter);
            return stringWriter.toString();
        } catch(IOException ex){
            throw new PlanningIOException(ex);
        }
    }
    
}
