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
package cz.cuni.amis.planning4j;

/**
 * An interface for planners that are able to plan asynchronously.
 * @author Martin Cerny
 */
public interface IAsyncPlanner<DOMAIN_TYPE extends IDomainProvider, PROBLEM_TYPE extends IProblemProvider> extends IPlanner<DOMAIN_TYPE, PROBLEM_TYPE> {
    /**
     * Returns a future, that will contain the planning result, after the planning is completed.
     * @param domainProvider
     * @param problemProvider
     * @return a plan future
     */
    public IPlanFuture planAsync(DOMAIN_TYPE domainProvider, PROBLEM_TYPE problemProvider);
}
