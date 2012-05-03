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

package cz.cuni.amis.planning4j.sicstus;

import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.impl.AbstractAsyncPlanner;
import cz.cuni.amis.planning4j.impl.PlanFuture;
import cz.cuni.amis.planning4j.impl.PlanningResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import se.sics.jasper.Jasper;
import se.sics.jasper.Prolog;
import se.sics.jasper.Query;
import se.sics.jasper.SICStus;
import se.sics.jasper.SPException;

/**
 * This class is not thread safe, only single planner execution should run at a time.
 * @author Martin Cerny
 */
public abstract class AbstractSicstusPlanner extends AbstractAsyncPlanner {
    protected final Prolog prolog;
    private File plannerInputFile;
    
    private static File createTempFileFromStream(InputStream s){
        try {
            File tempFile = File.createTempFile("Sicstus", ".sav");
            FileOutputStream output = new FileOutputStream(tempFile);            
            IOUtils.copy(s, output);
            output.close();
            return tempFile;
        } catch (IOException ex) {
            throw new PlanningException("Error preparing save file");
        }
        
    }
    
    public AbstractSicstusPlanner(InputStream plannerInput){
        this(createTempFileFromStream(plannerInput));
        plannerInputFile.deleteOnExit();
    }
    
    public AbstractSicstusPlanner(File plannerInput) {        
        this.plannerInputFile = plannerInput;
        try {
            prolog = Jasper.newProlog(new String[]{}, null, plannerInput.getAbsolutePath());
        } catch(Exception ex){
            throw new PlanningException("Could not initialize SICStus", ex);
        }        
    }

    @Override
    public IPlanFuture planAsync(IDomainProvider domainProvider, IProblemProvider problemProvider) {
        Map variableMap = new HashMap();
        File tmpFileForOutput;
        try {
             tmpFileForOutput = File.createTempFile("sicstus_plan_", "out");
        } catch (IOException ex) {
             throw new PlanningIOException("Could not create temp file to capture output", ex);
        }
        try {
            prolog.query("tell('" + tmpFileForOutput.getAbsolutePath().replace("\\", "/") + "').", null);
        } catch (Exception ex) {
            throw new PlanningException("Prolog output redirection failed: " + ex.getMessage(), ex);
        }
        Query q;
        synchronized(prolog){
             q = preparePrologQuery(domainProvider, problemProvider, variableMap);
        }
        SicstusPlanningFuture planningFuture = new SicstusPlanningFuture(q);
        
        SicstusPlanningProcess process = new SicstusPlanningProcess(planningFuture, q, variableMap, tmpFileForOutput);
        new Thread(process, "SicstusPlanning").start();        
        return planningFuture;
    }

       
    protected abstract Query preparePrologQuery(IDomainProvider domainProvider, IProblemProvider problemProvider, Map variableMap); 
    
    protected abstract IPlanningResult parseResultFromBoundVariables(Map variableMap, String plannerOutput);
    
    
        
    protected class SicstusPlanningProcess implements Runnable {
        private final PlanFuture planFuture;
        private final Query query;
        private Map variableMap;
        private File tmpFileForOutput;

        public SicstusPlanningProcess(PlanFuture planFuture, Query query, Map variableMap, File tmpFileForOutput) {
            this.planFuture = planFuture;
            this.query = query;
            this.variableMap = variableMap;
            this.tmpFileForOutput = tmpFileForOutput;
        }




        @Override
        public void run() {
            try {
                boolean success;
                synchronized(planFuture){
                    success = query.nextSolution();
                }
                prolog.query("flush_output,told.", null);                
                if(!success){
                    synchronized(planFuture){
                        if(!planFuture.isCancelled()){
                            planFuture.setResult(PlanningResult.FAILED_RESULT);
                        }
                    }
                } else {
                    
                    IPlanningResult result = parseResultFromBoundVariables(variableMap, FileUtils.readFileToString(tmpFileForOutput));                    
                    synchronized(planFuture){
                        if(!planFuture.isCancelled()){
                            planFuture.setResult(result);
                        }
                    }
                }
            } catch (Exception ex) {
                planFuture.computationException(ex);
            } finally{
                tmpFileForOutput.deleteOnExit();
            }
            
        }
        
        
    }
    
    protected class SicstusPlanningFuture extends PlanFuture<IPlanningResult> {
        private final Query prologQuery;

        public SicstusPlanningFuture(Query prologQuery) {
            this.prologQuery = prologQuery;
        }
        
        @Override
        protected boolean cancelComputation(boolean mayInterruptIfRunning) {
            if(mayInterruptIfRunning){
                try {  
                    synchronized(prolog){
                        prologQuery.close();
                    }
                } catch (Exception ex) {
                    throw new PlanningException("Error closing query", ex);
                }
                return true;
            }
            return super.cancelComputation(mayInterruptIfRunning);
        }
        
    }
    
}
