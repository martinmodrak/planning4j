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
import cz.cuni.amis.planning4j.external.IExternalPlannerExecutor;
import cz.cuni.amis.planning4j.external.impl.itsimple.*;
import cz.cuni.amis.planning4j.external.plannerspack.PlannersPackUtils;
import cz.cuni.amis.planning4j.impl.PDDLDomainProvider;
import cz.cuni.amis.planning4j.impl.PDDLProblemProvider;
import cz.cuni.amis.planning4j.pddl.*;
import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cernm6am
 */
public class LAMATest {
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

        IDomainProvider domainProvider = new PDDLDomainProvider(domain);
        IProblemProvider problemProvider = new PDDLProblemProvider(problem);

        Logger.getLogger(IExternalPlannerExecutor.LOGGER_NAME).setLevel(Level.ALL);        
        /**
         * Get a planner:
         */
        
//        PlannerListManager plannerManager = PlannersPackUtils.getPlannerListManager();

        //Let the engine suggest as a planner that supports strips and runs on current platform
       
//        ItSimplePlannerInformation plannerInfo = plannerManager.getPlannerByName("SGPlan 6");
//        File plannersDirectory = new File("/tmp");
//        plannerManager.extractAndPreparePlanner(plannersDirectory,plannerInfo);
        
        SimplePlannerListManager plannerManager = PlannersPackUtils.getInstalledPlannerListManager();


        //let's use the first suggested planner
        ItSimplePlannerInformation plannerInfo = plannerManager.getPlannerByName("Fast Downward");
        plannerInfo.getSettings().addAdditionalArgument(new PlannerArgument("--search", "lazy_greedy(blind())"));
//        plannerInfo.getSettings().addAdditionalArgument(new PlannerArgument("--heuristic", "hlm,hff=lm_ff_syn(lm_rhw(reasonable_orders=true,cost_type=1,lm_cost_type=1))"));
//        plannerInfo.getSettings().addAdditionalArgument(new PlannerArgument("--search", "lazy_greedy([hlm, hff], preferred=[hlm, hff])"));
        
        File plannersDirectory = new File("/home/martin_cerny/downward");

        
        try {
            IPlanner planner = new ExternalPlanner(new ItSimplePlannerExecutor(plannerInfo,plannersDirectory));            
            IPlanningResult result = planner.plan(domainProvider, problemProvider);
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
