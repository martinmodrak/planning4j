/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.external;

import cz.cuni.amis.planning.javaaiplanningapi.IPlanningResult;

/**
 *
 * @author Martin Cerny
 */
public interface IExternalPlanningResult extends IPlanningResult {

    String getConsoleOutput();

    long getTime();
    
}
