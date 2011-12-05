/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j.external;

import cz.cuni.amis.planning4j.IPlanningResult;

/**
 *
 * @author Martin Cerny
 */
public interface IExternalPlanningResult extends IPlanningResult {

    String getConsoleOutput();

    long getTime();
    
}
