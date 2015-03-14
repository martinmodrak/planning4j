# Limitations #

The ANA`*` is fast planner for relaxed domains. The provided domain has to be grounded and it cannot contain negative effects.

As of current version, ANA`*` planner can be connected only with PDDL domain and problem providers. Support for providers for other forms of domain specs will be added soon.


# Installation - Maven #

For Maven users, add dependency to ANA connector to your pom:

```
        <dependency>
            <groupId>cz.cuni.ktiml.plan</groupId>
            <artifactId>planning4j-connector</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

(You should have already added our repository as described under [Installation](Installation.md)

# Installation - Other #

You can download ANA artifacts from our artifactory at http://diana.ms.mff.cuni.cz:8081/artifactory/libs-snapshot-local/cz/cuni/ktiml/plan/
In this case you'll need both the plan and planning4j-connector artifact and the plan artifact depends on several other libraries - here is relevant excerpt of the project's POM to guide your downloading:

```
         <dependency>
            <groupId>dk.brics.automaton</groupId>
            <artifactId>automaton</artifactId>
            <version>1.11-8</version>
        </dependency>
        <dependency>
            <groupId>choco</groupId>
            <artifactId>choco-solver</artifactId>
            <version>2.1.1-SNAPSHOT</version>
        </dependency>  
        <dependency>
            <groupId>org.sat4j</groupId>
            <artifactId>org.sat4j.core</artifactId>
            <version>2.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.antlr</groupId>
            <artifactId>antlr-runtime</artifactId>
            <version>3.4</version>
        </dependency>
        <dependency>
            <groupId>org.antlr</groupId>
            <artifactId>antlr</artifactId>
            <version>3.4</version>
        </dependency>
```


# Usage #

To use the planner simply instantiate `ANAPlanner` class and use it as any other planner (see [SimpleUsage](SimpleUsage.md)). Here is a brief fully working example:

```
import cz.cuni.amis.planning4j.ActionDescription;
import cz.cuni.amis.planning4j.IDomainProvider;
import cz.cuni.amis.planning4j.IPlanner;
import cz.cuni.amis.planning4j.IPlanningResult;
import cz.cuni.amis.planning4j.IProblemProvider;
import cz.cuni.amis.planning4j.PlanningException;
import cz.cuni.amis.planning4j.impl.PDDLDomainProvider;
import cz.cuni.amis.planning4j.impl.PDDLProblemProvider;
import cz.cuni.amis.planning4j.pddl.PDDLDomain;
import cz.cuni.amis.planning4j.pddl.PDDLPredicate;
import cz.cuni.amis.planning4j.pddl.PDDLProblem;
import cz.cuni.amis.planning4j.pddl.PDDLRequirement;
import cz.cuni.amis.planning4j.pddl.PDDLSimpleAction;
import cz.cuni.ktiml.plan.planning4jconnector.ANAPlanner;
import java.util.EnumSet;

/**
 *
 * @author Martin Cerny
 */
public class ANAPlannerTest {
    public static void main(String args[]) {
   

        /**
         * Create a simple relaxed domain - two locations that can be reachable
         */
        PDDLDomain domain = new PDDLDomain("test", EnumSet.of(PDDLRequirement.STRIPS));
        domain.addPredicate(new PDDLPredicate("reachable_loc1"));
        domain.addPredicate(new PDDLPredicate("reachable_loc2"));

        PDDLSimpleAction moveAction = new PDDLSimpleAction("reach");
        moveAction.setPreconditionList("reachable_loc1");
        moveAction.setPositiveEffects("reachable_loc2");
        domain.addAction(moveAction);

        PDDLProblem problem = new PDDLProblem("problem_1", "test");
        problem.setInitialLiterals("reachable_loc1");
        problem.setGoalCondition("reachable_loc2");

        
        
        IDomainProvider domainProvider = new PDDLDomainProvider(domain);
        IProblemProvider problemProvider = new PDDLProblemProvider(problem);

        try {
            IPlanner planner = new ANAPlanner(5000 /*Timeout in msec*/);
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

```