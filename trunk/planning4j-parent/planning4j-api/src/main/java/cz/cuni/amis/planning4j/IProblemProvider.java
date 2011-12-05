/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Martin Cerny
 */
public interface IProblemProvider {
    public void writeProblem(Writer writer) throws IOException;
    public String getProblemAsString();
}
