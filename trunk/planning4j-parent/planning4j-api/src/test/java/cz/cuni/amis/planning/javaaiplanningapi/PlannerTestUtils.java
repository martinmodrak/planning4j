package cz.cuni.amis.planning.javaaiplanningapi;

import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.impl.PDDLDomainProvider;
import cz.cuni.amis.planning4j.impl.PDDLProblemProvider;
import cz.cuni.amis.planning4j.pddl.*;
import java.io.File;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlannerTestUtils 
{
    public static void simpleDomainTest(IPlanner planner, boolean negativeEffects){
        /**
         * Create a simple domain - two locations and an action to move between them
         */
        PDDLDomain domain = new PDDLDomain("test", EnumSet.of(PDDLRequirement.STRIPS));
        domain.addPredicate(new PDDLPredicate("at_loc1"));
        domain.addPredicate(new PDDLPredicate("at_loc2"));

        PDDLSimpleAction moveAction = new PDDLSimpleAction("move");
        moveAction.setPreconditionList("at_loc1");
        moveAction.setPositiveEffects("at_loc2");
        if(negativeEffects){
            moveAction.setNegativeEffects("at_loc1");
        }
        domain.addAction(moveAction);

        PDDLProblem problem = new PDDLProblem("problem_1", "test");
        problem.setInitialLiterals("at_loc1");
        problem.setGoalCondition("at_loc2");

        IDomainProvider domainProvider = new PDDLDomainProvider(domain);
        IProblemProvider problemProvider = new PDDLProblemProvider(problem);
        
        IPlanningResult result = planner.plan(domainProvider, problemProvider);
        if (!result.isSuccess()) {
            fail("No solution found.");
        } else {
//            System.out.println("Found solution. The plan is:");
//            for (ActionDescription action : result.getPlan()) {
//                System.out.println(action.getName());
//            }
            assertTrue(result.getPlan().size() == 1);
            assertTrue(result.getPlan().get(0).getName().equalsIgnoreCase("move"));            
        }

        
    }
}
