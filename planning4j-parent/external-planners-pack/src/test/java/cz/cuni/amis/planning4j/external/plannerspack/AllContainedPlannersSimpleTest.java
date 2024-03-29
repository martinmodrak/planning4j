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

import cz.cuni.amis.planning.javaaiplanningapi.PlannerTestUtils;
import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.external.ExternalPlanner;
import cz.cuni.amis.planning4j.external.impl.itsimple.*;
import cz.cuni.amis.planning4j.pddl.PDDLRequirement;
import org.junit.Test;

/**
 *
 * @author cernm6am
 */
public class AllContainedPlannersSimpleTest {
    
   @Test 
   public void test() {
   
       PlannerListManager list = PlannersPackUtils.getPlannerListManager();
       
       for(ItSimplePlannerInformation plannerInfo : list.suggestPlanners(PDDLRequirement.STRIPS)){
      
           list.extractAndPreparePlanner(plannerInfo);
           
           IPlanner planner = new ExternalPlanner(new ItSimplePlannerExecutor(plannerInfo));            
        
            PlannerTestUtils.simpleDomainTest(planner, true);
            PlannerTestUtils.variableDomainTest(planner, true);
       }

       
    }
   
}
