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

import cz.cuni.amis.planning4j.IAsyncPlanner;
import cz.cuni.amis.planning4j.IDomainProvider;
import cz.cuni.amis.planning4j.IPlanFuture;
import cz.cuni.amis.planning4j.IPlanner;
import cz.cuni.amis.planning4j.IPlanningResult;
import cz.cuni.amis.planning4j.IProblemProvider;

/**
 * Wraps a synchronuous planner into the {@link IAsyncPlanner} interface. The asynchronous
 * execution simply puts the planner execution in another thread - the execution is not cancellable, since
 * there is no way to cooperate with the original planner.
 * @author Martin Cerny
 */
public class AsyncPlannerWrapper<DOMAIN_TYPE extends IDomainProvider, PROBLEM_TYPE extends IProblemProvider> implements IAsyncPlanner<DOMAIN_TYPE, PROBLEM_TYPE> {
    IPlanner<DOMAIN_TYPE, PROBLEM_TYPE> planner;

    public AsyncPlannerWrapper(IPlanner<DOMAIN_TYPE, PROBLEM_TYPE> planner) {
        this.planner = planner;
    }

    @Override
    public Class<DOMAIN_TYPE> getDomainClass() {
        return planner.getDomainClass();
    }

    @Override
    public Class<PROBLEM_TYPE> getProblemClass() {
        return planner.getProblemClass();
    }

    
    
    @Override
    public IPlanningResult plan(DOMAIN_TYPE domainProvider, PROBLEM_TYPE problemProvider) {
        return planner.plan(domainProvider, problemProvider);
    }

    @Override
    public IPlanFuture planAsync(DOMAIN_TYPE domainProvider, PROBLEM_TYPE problemProvider) {
        PlanFuture future = new PlanFuture();
        PlanningOperation  operation = new PlanningOperation(future, domainProvider, problemProvider);
        new Thread(operation,"AsyncPlanningWrapper").start();
        return future;
    }
    
    
    
    private class PlanningOperation implements Runnable{

        private PlanFuture future;
        
        private DOMAIN_TYPE domainProvider;
        private PROBLEM_TYPE problemProvider;

        public PlanningOperation(PlanFuture future, DOMAIN_TYPE domainProvider, PROBLEM_TYPE problemProvider) {
            this.future = future;
            this.domainProvider = domainProvider;
            this.problemProvider = problemProvider;
        }

                
        
        @Override
        public void run() {
            try {
                future.setResult(planner.plan(domainProvider, problemProvider));
            } catch (Exception ex){
                future.computationException(ex);                
            }
        }
        
    }
}
