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
package cz.cuni.amis.planning4j.external.impl.itsimple;

import cz.cuni.amis.planning4j.pddl.PDDLRequirement;
import java.util.Set;

/**
 *
 * @author Martin Cerny
 */
public class PlannerInfo {
    public enum EPlannerPlatform { WINDOWS, LINUX, MAC };
    
    String plannerName;
    String version;
    Set<EPlannerPlatform> supportedPlatforms;
    Set<PDDLRequirement> supportedRequirements;
    
    PlannerExecutionSettings plannerExecutionSettings;
}
