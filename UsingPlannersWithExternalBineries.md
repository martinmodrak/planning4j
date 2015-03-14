# Planners with external binaries #

To run an IPC compliant planner with `ItSimplePlannerExecutor` you need `ItSimplePlannerInformation` instance with correct settings.
There are several ways of obtaining such object:

  * Using the default planner list manager from external-planners-pack artifact either with `PlannersPackUtils.getPlannerListManager().getPlannerByName()` or directly with `PlannersPackUtils.getXXX()`  methods. See [PlannerListManager javadoc](http://diana.ms.mff.cuni.cz:8080/job/Planning4J/javadoc/cz/cuni/amis/planning4j/external/impl/itsimple/PlannerListManager.html).
  * Writing your specificiation in ItSimple XML format and use `SimplePlannerListManager` to load it and access it. (the format can be seen in external-planners-pack artifact at `src/main/resources/planners/itPlanners.xml`)
  * Create `ItSimplePlannerInformation` directly in Java code and fill it with all the neccessary information. (see [ItSimplePlannerInformation javadoc](http://diana.ms.mff.cuni.cz:8080/job/Planning4J/javadoc/cz/cuni/amis/planning4j/external/impl/itsimple/ItSimplePlannerInformation.html) for some more details)

## Running the planner ##

Once you have the planner information object, you run the planner easily:

```
        ItSimplePlannerInformation plannerInfo = .... ;        
        File plannersDirectory = new File("/path/to/binaries");

        
        IPlanner planner = new ExternalPlanner(new ItSimplePlannerExecutor(plannerInfo,plannersDirectory));            

        IPlanningResult result =  Planning4JUtils.plan(planner, yourDomainProvider, yourProblemProvider);

```