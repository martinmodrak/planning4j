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
import cz.cuni.amis.planning4j.IPlanningResult;
import cz.cuni.amis.planning4j.IProblemProvider;
import cz.cuni.amis.planning4j.PlanningIOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Martin Cerny
 */
public class ExternalPlanner implements IPlanner{
    
    
    private IExternalPlannerExecutor externalPlannerExecutor;
    private File domainTempFile;
    private File problemTempFile;

    private static File silentCreateTempFile(String prefix, String suffix, File tempDirectory){
        try {
            return File.createTempFile(prefix, suffix, tempDirectory);            
        } catch (IOException ex){
            throw new PlanningIOException(ex);
        }
    }
    
    public ExternalPlanner(IExternalPlannerExecutor externalPlannerExecutor) {
        this(externalPlannerExecutor, new File(System.getProperty("java.io.tmpdir")));
    }
    
    public ExternalPlanner(IExternalPlannerExecutor externalPlannerExecutor, File tempDirectory) {
        this(externalPlannerExecutor, silentCreateTempFile("domain_", ".pddl", tempDirectory), silentCreateTempFile("problem_", ".pddl", tempDirectory));
    }

    public ExternalPlanner(IExternalPlannerExecutor externalPlannerExecutor, File domainTempFile, File problemTempFile) {
        this.externalPlannerExecutor = externalPlannerExecutor;
        this.domainTempFile = domainTempFile;
        this.problemTempFile = problemTempFile;
    }

        
    @Override
    public IExternalPlanningResult plan(IDomainProvider domainProvider, IProblemProvider problemProvider) {
        try {
            FileWriter domainFileWriter = new FileWriter(domainTempFile);
            domainProvider.writeDomain(domainFileWriter);
            domainFileWriter.close();
            FileWriter problemFileWriter = new FileWriter(problemTempFile);
            problemProvider.writeProblem(problemFileWriter);
            problemFileWriter.close();
            return externalPlannerExecutor.executePlanner(domainTempFile, problemTempFile);
        } catch(IOException ex){
            throw new PlanningIOException(ex);
        }
        
    }
    
    
}
