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
package cz.cuni.amis.planning4j.external;

import java.io.File;

/**
 * An interface for all classes, that run external planners that
 * take domain and problem files as input.
 * @author Martin Cerny
 */
public interface IExternalPlannerExecutor {

    /**
     * A name of a logger that classes calling external planners should use
     */
    public static final String LOGGER_NAME = "ExternalPlanners.ExecPlanner";
    
    
    /**
     * Executes the planner on specified domain and problem
     * @param chosenPlanner
     * @param domainFile
     * @param problemFile
     * @param timeSpentInIO time spent so far in writing the domain and problem file, implementations should add that to the time
     * they spent working with the planner
     * @return an xml representation of the plan
     */
    IExternalPlanningResult executePlanner(File domainFile, File problemFile, long timeSpentInIO);
}
