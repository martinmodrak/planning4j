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
package cz.cuni.amis.planning4j.external.impl;

import cz.cuni.amis.planning4j.IPlanFuture;
import cz.cuni.amis.planning4j.IPlanningResult;
import cz.cuni.amis.planning4j.external.IExternalPlannerExecutor;
import cz.cuni.amis.planning4j.external.IExternalPlanningProcess;
import cz.cuni.amis.planning4j.external.IExternalPlanningResult;
import cz.cuni.amis.planning4j.impl.PlanFuture;
import java.io.File;

/**
 * An external planner executor that delegates the execution to {@link IExternalPlanningProcess}
 * @author Martin Cerny
 */
public abstract class AbstractExternalPlannerExecutor implements IExternalPlannerExecutor{
    protected abstract IExternalPlanningProcess createProcess(File domainFile, File problemFile, long timeSpentInIO);

    @Override
    public IPlanFuture<IExternalPlanningResult> executePlanner(File domainFile, File problemFile, long timeSpentInIO) {
        final IExternalPlanningProcess planningProcess = createProcess(domainFile, problemFile, timeSpentInIO);
        final PlanFuture<IExternalPlanningResult> future = new ExternalPlannerPlanFuture(planningProcess);
        final ExternalPlannerShutdownHook shutdownHook = new ExternalPlannerShutdownHook(planningProcess);
        
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                future.setResult(planningProcess.executePlanner());                
                Runtime.getRuntime().removeShutdownHook(shutdownHook);
            }
        }, "ExternalPlanner").start();
        
        return future;
    }
    
    protected class ExternalPlannerPlanFuture extends PlanFuture<IExternalPlanningResult> {
        private IExternalPlanningProcess planningProcess;

        public ExternalPlannerPlanFuture(IExternalPlanningProcess planningProcess) {
            this.planningProcess = planningProcess;
        }

        @Override
        protected boolean cancelComputation(boolean mayInterruptIfRunning) {
            if(mayInterruptIfRunning){
                planningProcess.cancel();
                return true;
            } else {
                return super.cancelComputation(mayInterruptIfRunning);
            }
           
        }
        
    }
    
    protected class ExternalPlannerShutdownHook extends Thread {
        private IExternalPlanningProcess planningProcess;

        public ExternalPlannerShutdownHook(IExternalPlanningProcess planningProcess) {
            this.planningProcess = planningProcess;
        }

        
        
        @Override
        public void run() {
            planningProcess.cancel();
        }
        
    }
}
