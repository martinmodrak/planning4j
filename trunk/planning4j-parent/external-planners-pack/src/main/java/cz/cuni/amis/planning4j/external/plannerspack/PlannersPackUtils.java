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

import cz.cuni.amis.planning4j.PlanningException;
import cz.cuni.amis.planning4j.external.ExternalPlanner;
import cz.cuni.amis.planning4j.external.impl.itsimple.*;
import java.io.File;

/**
 * An utility class to access planners in this package.
 * Note that the getXXX classes don't prepare the planners for execution. If you want to extract
 * the planners (those that are contained), you still need to do it with {@link PlannerListManager#extractAndPreparePlanner(cz.cuni.amis.planning4j.external.impl.itsimple.ItSimplePlannerInformation) }
 * Once you obtain the {@link ItSimplePlannerInformation} object, you can run the planner with {@link ExternalPlanner}
 * and {@link ItSimplePlannerExecutor}.
 * @author Martin Cerny
 */
public class PlannersPackUtils {
    
    private static final PlannerPackListManager plannerPackListManager = new PlannerPackListManager();
    private static final SimplePlannerListManager installedPlannerListManager = new SimplePlannerListManager(XMLUtilities.readPlannerListFromStream(PlannersPackUtils.class.getResourceAsStream("/planners/installedPlanners.xml")));

    /**
     * Gets planner list manager for planners that are part of this package in binary from
     * @return 
     */
    public static PlannerListManager getPlannerListManager(){
        return plannerPackListManager;        
    }
    
    /**
     * Gets planner list manager for planners that are supported, but need to be installed externally
     * @return 
     */
    public static SimplePlannerListManager getInstalledPlannerListManager(){
        return installedPlannerListManager;                
    }
    
    public static ItSimplePlannerInformation getBlackBox(){
        return getPlannerListManager().getPlannerByName("Blackbox");
    }
    
    public static ItSimplePlannerInformation getMetricFF(){
        return getPlannerListManager().getPlannerByName("Metric-FF");        
    }
    
    public static ItSimplePlannerInformation getSGPlan6(){
        return getPlannerListManager().getPlannerByName("SGPlan 6");
    }
    
    /**
     * Return template for all Fast downward planner. It is then neccessary
     * to specify which planner by adding additional arguments to planner settings.
     * Note that fast downward is not bundled in this pack - it needs to be already installed.
     */
    public static ItSimplePlannerInformation getFastDownwardTemplate(){
        return installedPlannerListManager.getPlannerByName("Fast Downward");
    }

    /**
     * Gets a specific IPC configuration of Fast Downward.
     * Note that fast downward is not bundled in this pack - it needs to be already installed. 
     * @param configuratioName
     * @return 
     */
    public static ItSimplePlannerInformation getFastDownwardIPCConfiguration(String configuratioName){
        ItSimplePlannerInformation info = getFastDownwardTemplate();
        if(info == null){
            throw new PlanningException("Fast downward not found");
        }
        info.getSettings().addAdditionalArgument(new PlannerArgument("ipc",configuratioName));
        return info;
    }

    /**
     * Gets the LAMA-2011 planner as competed in IPC 2011.
     * Note that fast downward is not bundled in this pack - it needs to be already installed.
     * @return 
     */
    public static ItSimplePlannerInformation getLAMA2011() {
        return getFastDownwardIPCConfiguration("seq-sat-lama-2011");
    }

    /**
     * Get's the probe planner.
     * Note that PROBE is not bundled in this pack - it needs to be already installed on your system.
     * @return 
     */
    public static ItSimplePlannerInformation getProbe(){
        return installedPlannerListManager.getPlannerByName("PROBE");
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
                ItSimpleUtils.extractFileIfNotExists(new File(targetDirectory,"planners/cygwin1.dll"), "/planners/cygwin1.dll");
            }
        }
        
        
    }
    
    
}
