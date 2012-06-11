package cz.cuni.amis.planning.javaaiplanningapi;

import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.impl.PDDLObjectDomainProvider;
import cz.cuni.amis.planning4j.impl.PDDLObjectProblemProvider;
import cz.cuni.amis.planning4j.pddl.*;
import cz.cuni.amis.planning4j.utils.Planning4JUtils;
import java.util.EnumSet;
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

        IDomainProvider domainProvider = new PDDLObjectDomainProvider(domain);
        IProblemProvider problemProvider = new PDDLObjectProblemProvider(problem);
        
        IPlanningResult result = Planning4JUtils.plan(planner, domainProvider, problemProvider);
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
    
    public static void variableDomainTest(IPlanner planner, boolean negativeEffects){
        /**
         * Create a simple domain - two locations and an abstract move action
         */
        PDDLDomain domain = new PDDLDomain("test", EnumSet.of(PDDLRequirement.STRIPS));
        PDDLType locationType = new PDDLType("location", PDDLType.OBJECT_TYPE);
        domain.addType(locationType);
        
        domain.addPredicate(new PDDLPredicate("at",new PDDLParameter("loc", locationType)));
        domain.addConstant(new PDDLConstant("loc1", locationType));
        domain.addConstant(new PDDLConstant("loc2", locationType));

        PDDLSimpleAction moveAction = new PDDLSimpleAction("move", new PDDLParameter("l1", locationType), new PDDLParameter("l2", locationType));        
        moveAction.setPreconditionList("at(l1)"); //TODO - rewrite to our objects
        moveAction.setPositiveEffects("at(l2)");
        if(negativeEffects){
            moveAction.setNegativeEffects("at(l1)");
        }
        domain.addAction(moveAction);

        PDDLProblem problem = new PDDLProblem("problem_1", "test");
        problem.setInitialLiterals("at(loc1)");
        problem.setGoalCondition("at(loc2)");

        IDomainProvider domainProvider = new PDDLObjectDomainProvider(domain);
        IProblemProvider problemProvider = new PDDLObjectProblemProvider(problem);
        
        IPlanningResult result = Planning4JUtils.plan(planner, domainProvider, problemProvider);
        if (!result.isSuccess()) {
            fail("No solution found.");
        } else {
//            System.out.println("Found solution. The plan is:");
//            for (ActionDescription action : result.getPlan()) {
//                System.out.println(action.getName());
//            }
            assertTrue(result.getPlan().size() == 1);
            ActionDescription firstAction = result.getPlan().get(0);
            assertTrue(firstAction.getName().equalsIgnoreCase("move"));            
            assertTrue(firstAction.getParameters().size() == 2);            
            assertTrue(firstAction.getParameters().get(0).equalsIgnoreCase("loc1"));            
            assertTrue(firstAction.getParameters().get(0).equalsIgnoreCase("loc2"));            
        }

        
    }
    
}
