/*
 * Copyright (C) 2011 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.amis.planning4j.external.impl;

import cz.cuni.amis.planning4j.external.IExternalPlanningResult;
import cz.cuni.amis.planning4j.ActionDescription;
import cz.cuni.amis.planning4j.PlanningStatistics;
import java.util.List;

/**
 * An implementation of {@link IExternalPlanningResult}
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
     * Statistics about the planning as provided by the planner
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
