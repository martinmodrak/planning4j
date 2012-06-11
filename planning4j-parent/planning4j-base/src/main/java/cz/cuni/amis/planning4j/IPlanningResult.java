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
package cz.cuni.amis.planning4j;

import java.util.List;

/**
 * Interface for results of planning.
 * Implementations must guarantee that identifiers of the plan are normalized with {@link PlanningUtils#normalizeIdentifier(java.lang.String) }
 * @author Martin Cerny
 */
public interface IPlanningResult {

    List<ActionDescription> getPlan();

    /**
     * Returns statistics of the planning process, might be null, if planning was not succesful.
     * @return 
     */
    PlanningStatistics getPlanningStatistics();

    /**
     * True if the plan is OK, false if the planner executed correctly, but did
     * not find any solution. Other problems (planner not found, I/O error, ...) are signalled by an exception.
     * @return 
     */
    boolean isSuccess();
    
}
