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

import cz.cuni.amis.planning4j.utils.PlanningUtils;

/**
 * General interface for any planner. The implementation must ensure, that identifiers of actions and objects in the planning
 * result were normalized with {@link PlanningUtils#normalizeIdentifier(java.lang.String) }
 * @author Martin Cerny
 */
public interface IPlanner {
    /**
     * Does the planning and returns a result.
     * @param domainProvider
     * @param problemProvider
     * @return the result
     * @throws PlanningException if the planner could not be executed (ie. wrong domain specification, IO error,...)
     */
    public IPlanningResult plan(IDomainProvider domainProvider, IProblemProvider problemProvider);
}
