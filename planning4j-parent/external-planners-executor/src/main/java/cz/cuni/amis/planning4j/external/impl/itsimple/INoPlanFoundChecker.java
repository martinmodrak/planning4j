/*
 * Copyright (C) 2013 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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

import java.util.List;

/**
 * An interface for a method for checking, whether planner found a solution.
 * All of the methods return true, if no plan found signal was recognized.
 * @author Martin Cerny
 */
public interface INoPlanFoundChecker {
    public boolean processOutputLine(String outputLine);
    public boolean processExitCode(int exitCode);
    public boolean processUnprocessedPlan(List<String> unprocessedPlan);
    
}
