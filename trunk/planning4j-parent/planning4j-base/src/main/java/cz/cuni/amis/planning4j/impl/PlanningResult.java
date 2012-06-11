/*
 * Copyright (C) 2012 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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
package cz.cuni.amis.planning4j.impl;

import cz.cuni.amis.planning4j.ActionDescription;
import cz.cuni.amis.planning4j.IPlanningResult;
import cz.cuni.amis.planning4j.PlanningStatistics;
import java.util.Collections;
import java.util.List;

/**
 * A simple implementation of a planning result.
 * @author Martin Cerny
 */
public class PlanningResult implements IPlanningResult {
    private boolean success;

    private List<ActionDescription> plan;

    /**
     * Statistics about the planning as provided by the planner
     */
    private PlanningStatistics planningStatistics;

    public PlanningResult(boolean success, List<ActionDescription> plan, PlanningStatistics planningStatistics) {
        this.success = success;
        this.plan = plan;
        this.planningStatistics = planningStatistics;
    }


    /**
     * A simple ready to use instance of a result to be returned, if no such plan exists.
     */
    public static final PlanningResult FAILED_RESULT = new PlanningResult(false, Collections.EMPTY_LIST, null);
    
    
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

    
}
