# Supported planners #

A list of supported planners follows.

## Cross-platform ##

  * ANA`*` - Fast planner for relaxed domains (without negative effects) see [UsingANA](UsingANA.md)

## Platform specific ##

Some planners are included in binary form and are thus platform specific.

Following platform specific planners are provided by the external planner connector taken from ItSimple project. For usage instructions see [SimpleUsage](SimpleUsage.md).

### For Windows ###
  * [Metric-FF](http://www.loria.fr/~hoffmanj/metric-ff.html)
  * [BlackBox](http://www.cs.rochester.edu/~kautz/satplan/blackbox/)

### For Linux ###

## Binaries included in external-planners-pack ##
Those can be extracted either as a part of Maven build (see [ExtractingPlannersWithMaven](ExtractingPlannersWithMaven.md) ) or
directly from Java Code with `PlannersPackUtils.getPlannerListManager().extractAndPreparePlanner()`  (see [PlannersPackUtils javadoc](http://diana.ms.mff.cuni.cz:8080/job/Planning4J/javadoc/cz/cuni/amis/planning4j/external/plannerspack/PlannersPackUtils.html))
  * [Metric-FF](http://www.loria.fr/~hoffmanj/metric-ff.html)
  * [BlackBox](http://www.cs.rochester.edu/~kautz/satplan/blackbox/)
  * [FF 2.3](http://www.loria.fr/~hoffmanj/ff.html)
  * [SGPlan 5.22](http://manip.crhc.uiuc.edu/programs/SGPlan/index.html)
  * [SGPlan 6](http://manip.crhc.uiuc.edu/programs/SGPlan/index.html)
  * [Mips XXL 1.0](http://andorfer.cs.uni-dortmund.de/~edelkamp/mips/mips-xxl.html)
  * [Mips XXL 2008](http://andorfer.cs.uni-dortmund.de/~edelkamp/mips/mips-xxl.html)
  * [hspsp](http://ipc.informatik.uni-freiburg.de/Planners/)
  * [Plan-A 1.0](http://ipc.informatik.uni-freiburg.de/Planners/)
  * [MaxPlan](http://www.cse.wustl.edu/~chen/maxplan/)
  * [LPRPG](http://planning.cis.strath.ac.uk/LPRPG/)
  * [Marvin](http://planning.cis.strath.ac.uk/Marvin/)

## Binaries not included ##
See [UsingPlannersWithExternalBineries](UsingPlannersWithExternalBineries.md)
  * Planners based on [Fast Downward platform](http://www.fast-downward.org/). See [UsingFastDownward](UsingFastDownward.md)
  * [Probe](http://www.tecn.upf.es/~nlipovet/).
  * Possibly any IPC-compliant planner.