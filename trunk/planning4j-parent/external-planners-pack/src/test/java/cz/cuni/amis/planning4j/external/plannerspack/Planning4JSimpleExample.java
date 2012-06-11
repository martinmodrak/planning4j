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
package cz.cuni.amis.planning4j.external.plannerspack;

import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.external.ExternalPlanner;
import cz.cuni.amis.planning4j.external.impl.itsimple.ItSimplePlannerExecutor;
import cz.cuni.amis.planning4j.external.impl.itsimple.ItSimplePlannerInformation;
import cz.cuni.amis.planning4j.external.impl.itsimple.PlannerListManager;
import cz.cuni.amis.planning4j.impl.PDDLObjectDomainProvider;
import cz.cuni.amis.planning4j.impl.PDDLObjectProblemProvider;
import cz.cuni.amis.planning4j.pddl.PDDLDomain;
import cz.cuni.amis.planning4j.pddl.PDDLPredicate;
import cz.cuni.amis.planning4j.pddl.PDDLProblem;
import cz.cuni.amis.planning4j.pddl.PDDLRequirement;
import cz.cuni.amis.planning4j.pddl.PDDLSimpleAction;
import cz.cuni.amis.planning4j.utils.Planning4JUtils;
import java.io.File;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author Martin Cerny
 */
public class Planning4JSimpleExample {
  public static void main(String args[]) {
   

        /**
         * Create a simple domain - two locations and an action to move between them
         */
        PDDLDomain domain = new PDDLDomain("test", EnumSet.of(PDDLRequirement.STRIPS));
        domain.addPredicate(new PDDLPredicate("at_loc1"));
        domain.addPredicate(new PDDLPredicate("at_loc2"));

        PDDLSimpleAction moveAction = new PDDLSimpleAction("move");
        moveAction.setPreconditionList("at_loc1");
        moveAction.setPositiveEffects("at_loc2");
        moveAction.setNegativeEffects("at_loc1");
        domain.addAction(moveAction);

        PDDLProblem problem = new PDDLProblem("problem_1", "test");
        problem.setInitialLiterals("at_loc1");
        problem.setGoalCondition("at_loc2");

        IPDDLObjectDomainProvider domainProvider = new PDDLObjectDomainProvider(domain);
        IPDDLObjectProblemProvider problemProvider = new PDDLObjectProblemProvider(problem);

        /**
         * Get a planner:
         */
        PlannerListManager plannerManager = PlannersPackUtils.getPlannerListManager();

        //Let the engine suggest as a planner that supports strips and runs on current platform
        List<ItSimplePlannerInformation> suggestedPlanners = plannerManager.suggestPlanners(PDDLRequirement.STRIPS);

        if (suggestedPlanners.isEmpty()) {
            System.out.println("No planner found for current platform.");
            return;
        }

        //let's use the first suggested planner
        ItSimplePlannerInformation plannerInfo = suggestedPlanners.get(0);

        //To use a planner by name either call getPlannerByName
        //   ItSimplePlannerInformation plannerInfo = plannerManager.getPlannerByName("Metric-FF");
        //Or use one of the PlannerPackUtils.getXXX predefined methods
        //   ItSimplePlannerInformation plannerInfo = PlannersPackUtils.getBlackBox();

       //This is the place to extract the planner
        File plannersDirectory = new File("target");
        //The planner is extracted (only if it does not exist yet) and exec permissions are set under Linux
        plannerManager.extractAndPreparePlanner(plannersDirectory, plannerInfo);
        
        try {
            IPlanner planner = new ExternalPlanner(new ItSimplePlannerExecutor(plannerInfo,plannersDirectory));            
            //Call to Planning4JUtils.plan gathers domain and problem translators (if needed) and performs the planning
            IPlanningResult result =  Planning4JUtils.plan(planner, domainProvider, problemProvider);
            if (!result.isSuccess()) {
                System.out.println("No solution found.");
                return;
            } else {
                System.out.println("Found solution. The plan is:");
                for (ActionDescription action : result.getPlan()) {
                    System.out.println(action.getName());
                }
            }
        } catch (PlanningException ex) {
            System.out.println("Exception during planning.");
            ex.printStackTrace();
        }



    }    
}
