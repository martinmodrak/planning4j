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

import cz.cuni.amis.planning4j.IDomainProvider;
import cz.cuni.amis.planning4j.IPlanner;
import cz.cuni.amis.planning4j.IProblemProvider;
import cz.cuni.amis.planning4j.PlanningException;
import cz.cuni.amis.planning4j.PlanningIOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * An implementation of {@link IPlanner} that writes the domain and problem to
 * a file and delegates the planning to {@link IExternalPlannerExecutor}.
 * 
 * @author Martin Cerny
 */
public class ExternalPlanner implements IPlanner{
    
    
    private IExternalPlannerExecutor externalPlannerExecutor;
    private File domainTempFile;
    private File problemTempFile;

    /**
     * Creates a temp file and wraps the checked IOException.
     * @param prefix
     * @param suffix
     * @param tempDirectory
     * @throws PlanningIOException if the creation of temp file failed
     * @return 
     */
    private static File silentCreateTempFile(String prefix, String suffix, File tempDirectory){
        try {
            return File.createTempFile(prefix, suffix, tempDirectory);            
        } catch (IOException ex){
            throw new PlanningIOException(ex);
        }
    }
    
    /**
     * Create a planner with the specified executor. Input files are created in default temp directory.
     * @param externalPlannerExecutor 
     */
    public ExternalPlanner(IExternalPlannerExecutor externalPlannerExecutor) {
        this(externalPlannerExecutor, new File(System.getProperty("java.io.tmpdir")));
    }
    
    /**
     * Create a planner with the specified executor. Input files are created in specified temp directory.
     * @param externalPlannerExecutor 
     */
    public ExternalPlanner(IExternalPlannerExecutor externalPlannerExecutor, File tempDirectory) {
        this(externalPlannerExecutor, silentCreateTempFile("domain_", ".pddl", tempDirectory), silentCreateTempFile("problem_", ".pddl", tempDirectory));
    }

    /**
     * Create a planner with the specified executor. Location of input files is fully specified.
     * @param externalPlannerExecutor 
     */
    public ExternalPlanner(IExternalPlannerExecutor externalPlannerExecutor, File domainTempFile, File problemTempFile) {
        this.externalPlannerExecutor = externalPlannerExecutor;
        this.domainTempFile = domainTempFile;
        this.problemTempFile = problemTempFile;
    }

        
    /**
     * Writes the domain and problem to file and delegates the planning to {@link #externalPlannerExecutor}.
     * @param domainProvider
     * @param problemProvider
     * @return planning result
     * @throws PlanningException if the planner execution failed or the domain and problem files could not be generated
     */
    @Override
    public IExternalPlanningResult plan(IDomainProvider domainProvider, IProblemProvider problemProvider) {
        try {
            long start_time = System.currentTimeMillis();
            FileWriter domainFileWriter = new FileWriter(domainTempFile);
            domainProvider.writeDomain(domainFileWriter);
            domainFileWriter.close();
            FileWriter problemFileWriter = new FileWriter(problemTempFile);
            problemProvider.writeProblem(problemFileWriter);
            problemFileWriter.close();
            long io_time = System.currentTimeMillis() - start_time;
            return externalPlannerExecutor.executePlanner(domainTempFile, problemTempFile, io_time);
        } catch(IOException ex){
            throw new PlanningIOException(ex);
        }
        
    }
    
    
}
