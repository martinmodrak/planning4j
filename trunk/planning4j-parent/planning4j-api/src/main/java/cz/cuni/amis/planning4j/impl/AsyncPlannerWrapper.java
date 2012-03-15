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
public class AsyncPlannerWrapper implements IAsyncPlanner {
    IPlanner planner;

    public AsyncPlannerWrapper(IPlanner planner) {
        this.planner = planner;
    }

    
    
    @Override
    public IPlanningResult plan(IDomainProvider domainProvider, IProblemProvider problemProvider) {
        return planner.plan(domainProvider, problemProvider);
    }

    @Override
    public IPlanFuture planAsync(IDomainProvider domainProvider, IProblemProvider problemProvider) {
        PlanFuture future = new PlanFuture();
        PlanningOperation  operation = new PlanningOperation(future, domainProvider, problemProvider);
        new Thread(operation,"AsyncPlanningWrapper").start();
        return future;
    }
    
    
    
    private class PlanningOperation implements Runnable{

        private PlanFuture future;
        
        private IDomainProvider domainProvider;
        private IProblemProvider problemProvider;

        public PlanningOperation(PlanFuture future, IDomainProvider domainProvider, IProblemProvider problemProvider) {
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
