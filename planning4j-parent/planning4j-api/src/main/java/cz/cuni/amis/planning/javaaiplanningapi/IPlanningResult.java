/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.javaaiplanningapi;

import cz.cuni.amis.planning.javaaiplanningapi.ActionDescription;
import cz.cuni.amis.planning.javaaiplanningapi.PlanningStatistics;
import java.util.List;

/**
 *
 * @author Martin Cerny
 */
public interface IPlanningResult {

    List<ActionDescription> getPlan();

    PlanningStatistics getPlanningStatistics();

    boolean isSuccess();
    
}
