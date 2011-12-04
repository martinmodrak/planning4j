/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.external.impl;

import cz.cuni.amis.planning.external.IExternalPlanningResult;
import cz.cuni.amis.planning.javaaiplanningapi.ActionDescription;
import cz.cuni.amis.planning.javaaiplanningapi.PlanningStatistics;
import java.util.List;

/**
 *
 * @author Martin Cerny
 */
public class ExternalPlanningResult implements IExternalPlanningResult {
    private boolean success;
    private List<ActionDescription> plan;
    private String consoleOutput;
    
    /**
     * Time in miliseconds that the whole execution of the planner took
     */    
    private long time;
    
    /**
     * Statistics about the planning as provided by tha planner
     */
    private PlanningStatistics planningStatistics;

    public ExternalPlanningResult(boolean success, List<ActionDescription> plan, String consoleOutput, PlanningStatistics planningStatistics, long time) {
        this.success = success;
        this.plan = plan;
        this.consoleOutput = consoleOutput;
        this.planningStatistics = planningStatistics;
        this.time  = time;
    }

    @Override
    public String getConsoleOutput() {
        return consoleOutput;
    }

    @Override
    public List<ActionDescription> getPlan() {
        return plan;
    }

    @Override
    public PlanningStatistics getPlanningStatistics() {
        return planningStatistics;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public long getTime() {
        return time;
    }
    
    
}
