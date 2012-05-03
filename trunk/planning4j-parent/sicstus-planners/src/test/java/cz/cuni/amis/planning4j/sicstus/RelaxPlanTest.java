package cz.cuni.amis.planning4j.sicstus;

import cz.cuni.amis.planning.javaaiplanningapi.PlannerTestUtils;
import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class RelaxPlanTest 
{
    @Test
    public void simpleTest(){
        PlannerTestUtils.simpleDomainTest(new RelaxPlan(30000), false);
    }
}
