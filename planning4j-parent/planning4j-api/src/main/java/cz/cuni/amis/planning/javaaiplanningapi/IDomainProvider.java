/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.javaaiplanningapi;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Martin Cerny
 */
public interface IDomainProvider {
    public void writeDomain(Writer writer) throws IOException;
    public String getDomainAsString();
}
