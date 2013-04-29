/*
 * Copyright (C) 2011 Martin Cerny
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
package cz.cuni.amis.planning4j.external;

import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.impl.AbstractAsyncPlanner;

/**
 * An implementation of {@link IPlanner} that writes the domain and problem to
 * a file and delegates the planning to {@link IExternalPlannerExecutor}.
 * 
 * @author Martin Cerny
 */
public class ExternalPlanner extends AbstractAsyncPlanner<IPDDLFileDomainProvider, IPDDLFileProblemProvider>{
    
    
    private IExternalPlannerExecutor externalPlannerExecutor;
    
    /**
     * Create a planner with the specified executor. Input files are created in default temp directory.
     * @param externalPlannerExecutor 
     */
    public ExternalPlanner(IExternalPlannerExecutor externalPlannerExecutor) {
        super(IPDDLFileDomainProvider.class,  IPDDLFileProblemProvider.class);
        this.externalPlannerExecutor = externalPlannerExecutor;
    }
    

    @Override
    public IExternalPlanningResult plan(IPDDLFileDomainProvider domainProvider, IPDDLFileProblemProvider problemProvider) {
        //this conversion is safe, since the super implementation just calls planAsync
        return (IExternalPlanningResult)super.plan(domainProvider, problemProvider);
    }

    
        
    /**
     * Writes the domain and problem to file and delegates the planning to {@link #externalPlannerExecutor}.
     * @param domainProvider
     * @param problemProvider
     * @return planning result
     * @throws PlanningException if the planner execution failed or the domain and problem files could not be generated
     */
    @Override
    public IPlanFuture<IExternalPlanningResult> planAsync(IPDDLFileDomainProvider domainProvider, IPDDLFileProblemProvider problemProvider) {
        return externalPlannerExecutor.executePlanner(domainProvider.getDomainFile(), problemProvider.getProblemFile(), 0);        
    }

    @Override
    public String toString() {
        return "ExternalPlanner{" + "executor=" + externalPlannerExecutor + '}';
    }

    @Override
    public String getName() {
        return externalPlannerExecutor.getPlannerName();
    }
    

    
}
