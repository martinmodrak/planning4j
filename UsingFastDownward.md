# Installation #

The Fast Downward platform has to be installed on your computer manually.
Example code to obtain LAMA 2011 planner (using external-planners-pack artifact):

```
        ItSimplePlannerInformation plannerInfo = PlannersPackUtils.getLAMA2011(); 
        //Use getFastDownwardIPCConfiguration("name") for other IPC Fast Downward planners
        
        File plannersDirectory = new File("/path/to/fastdownward/downward");

        
        IPlanner planner = new ExternalPlanner(new ItSimplePlannerExecutor(plannerInfo,plannersDirectory));            

```