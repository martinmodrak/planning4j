# Basic concepts #

The Planning4J planning functionality has three basic components: Planners, Domain providers and Problem providers.

## Domain and problem providers ##

There are multiple possible ways you can specify you problems and domains, but different planner implementations might need different ways. To ease your work with that, Planning4J introduces domain and problem providers.

A provider is an object that can present the domain or problem data in a particular way. As of version 1.1 there are four types of providers for both domains and problems (the respective classes are called XXXDomainProvider or XXXXProblemProvider)
  * PDDLObject - contains a java object from the Planning4J PDDL API - still very limited but where the feature set is sufficient, it lets you conveniently create Java objects that correspond to various PDDL elements
  * PDDLFile - contains a `java.io.File` instance that corresponds to a file containing PDDL
  * PDDLString - contains a string with the PDDL definition
  * IPDDLWriter - sometimes it is more convenient (and faster) to output the PDDL text directly to a writer, instead of endless string concatenations. Subclass this and implement the write method which is called to write the problem to a `java.io.Writer`.

## Planners ##

Planners implement the `IPlanner` or `IAsyncPlanner` interface and are tied with a concrete problem and domain provider type. You usually get a planner instance by directly instantiating the planner constructor. As of now, the most useful is `ExternalPlanner` together with `ItSimplePlannerExecutor` which is capable of running basically any IPC compliant planner (see [UsingPlannersWithExternalBineries](UsingPlannersWithExternalBineries.md))

## Putting it together ##

Since different planners may require input in different forms, you would need to change your problems and domain providers if you change your planner. But this is not very convenient. To remedy this, Planning4J has domain and problem translators, that translate data from one form to another (if possible). To use them there are mutliple methods in `Planning4JUtils` class (see [javadoc](http://diana.ms.mff.cuni.cz:8080/job/Planning4J/javadoc/cz/cuni/amis/planning4j/utils/Planning4JUtils.html)), most notably `Planning4JUtils.plan()` and `Planning4JUtils.planAsync()` that allow you to just specify any planner and providers of any type and the translating is done for you, if neccessary and possible.