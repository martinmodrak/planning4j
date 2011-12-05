/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j;

import cz.cuni.amis.planning4j.ActionDescription;
import cz.cuni.amis.planning4j.PlanningStatistics;
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
