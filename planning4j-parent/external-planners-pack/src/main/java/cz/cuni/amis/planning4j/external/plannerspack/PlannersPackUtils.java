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
package cz.cuni.amis.planning4j.external.plannerspack;

import cz.cuni.amis.planning4j.external.impl.itsimple.*;
import java.io.File;

/**
 * An utility class to access planners in this package.
 * @author Martin Cerny
 */
public class PlannersPackUtils {

    /**
     * Gets planner list manager for planners that are part of this package in binary from
     * @return 
     */
    public static PlannerListManager getPlannerListManager(){
        return new PlannerPackListManager();        
    }
    
    /**
     * Gets planner list manager for planners that are supported, but need to be installed externally
     * @return 
     */
    public static SimplePlannerListManager getInstalledPlannerListManager(){
        return new SimplePlannerListManager(XMLUtilities.readPlannerListFromStream(PlannersPackUtils.class.getResourceAsStream("/planners/installedPlanners.xml")));        
        
    }

    private static class PlannerPackListManager extends PlannerListManager {
        public PlannerPackListManager(){
            super(XMLUtilities.readPlannerListFromStream(PlannersPackUtils.class.getResourceAsStream("/planners/itPlanners.xml")));
        }

        @Override
        public void extractAndPreparePlanner(File targetDirectory, ItSimplePlannerInformation selectedPlanner) {
            super.extractAndPreparePlanner(targetDirectory, selectedPlanner);
            if(ItSimpleUtils.getOperatingSystem() == EPlannerPlatform.WINDOWS){
                //On windows, some planners need cygwin...
                extractFileIfNotExists(new File(targetDirectory,"planners/cygwin1.dll"), "/planners/cygwin1.dll");
            }
        }
        
        
    }
    
    
}