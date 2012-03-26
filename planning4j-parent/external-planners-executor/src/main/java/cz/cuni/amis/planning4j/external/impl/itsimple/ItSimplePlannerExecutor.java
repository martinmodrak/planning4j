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
package cz.cuni.amis.planning4j.external.impl.itsimple;

import cz.cuni.amis.planning4j.external.IExternalPlanningProcess;
import cz.cuni.amis.planning4j.external.impl.AbstractExternalPlannerExecutor;
import java.io.File;
import org.jdom.Element;

/**
 * <p>
 * Executes a plan using external planners specified with ItSimple XML format.
 * Identifiers of the returned plan are treated with {@link PlanningUtils#normalizeIdentifier(java.lang.String) }.
 * </p>
 * 
 * <p>
 * Known limitations: since on Linux, some planners don't die with process.destroy(), planning processes are
 * destroyed by process name (using killall system call) upon cancellation of the planning process. This means, that if you
 * cancel one planning process, all processes of the same planner the current user has access to are terminated.
 * </p>
 * @author Martin Cerny
 */
public class ItSimplePlannerExecutor extends AbstractExternalPlannerExecutor{

   private ItSimplePlannerInformation chosenPlanner;

    private File plannerBinariesDirectory;

    private File workingDirectory;

    public ItSimplePlannerExecutor(ItSimplePlannerInformation chosenPlanner) {
        this(chosenPlanner, new File("."));
    }

    public ItSimplePlannerExecutor(ItSimplePlannerInformation chosenPlanner, File plannerBinariesDirectory) {
        this(chosenPlanner, plannerBinariesDirectory, new File(System.getProperty("java.io.tmpdir")));
    }

    public ItSimplePlannerExecutor(ItSimplePlannerInformation chosenPlanner, File plannerBinariesDirectory, File workingDirectory) {
        if(chosenPlanner == null){
            throw new NullPointerException("Chosen planner cannot be null");
        }
        this.chosenPlanner = chosenPlanner;
        this.plannerBinariesDirectory = plannerBinariesDirectory;
        this.workingDirectory = workingDirectory;
    }
    
    @Override
    protected IExternalPlanningProcess createProcess(File domainFile, File problemFile, long timeSpentInIO) {
        return new ItSimplePlanningProcess(chosenPlanner, plannerBinariesDirectory, workingDirectory, domainFile, problemFile, timeSpentInIO);
    }
    
}
