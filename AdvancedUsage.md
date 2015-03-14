Before reading, please make sure you are familiar with [BasicConcepts](BasicConcepts.md).

# Domain and problem in different formats #

To supply your problem or domain in any format supported by Planning4J, you only need to create provider instances and forward them to `Planning4JUtils.plan()` or `Planning4JUtils.planAsync()` methods. If your providers are compatible with the given planner or a translation is possible, it will be done automatically.

# Asynchronuous planning #

Sometimes you cannot block your code to wait for planner execution. You certainly do not need to do that with Planning4J. Most of the planner implementations implement the `IAsyncPlanner` interface. If so, they should fully support asynchronuous execution including termination of planner process before it completes. If they don't, you can use `AsyncPlannerWrapper` to add asynchronuous functionality to a synchronous planner implementation, but then you cannot expect the planner to terminate before it finishes.

Most convenient way of invoking asynchronuous planner is through `Planning4JUtils.planAsync()` as it translates your domain and problem providers for you. The method returns an instance of `IPlanFuture` ([Javadoc](http://diana.ms.mff.cuni.cz:8080/job/Planning4J/javadoc/cz/cuni/amis/planning4j/IPlanFuture.html)). It behaves as regular `java.util.concurrent.Future`, but has few extra features: it allows you to inspect the status of the future more closesly (especially distinguish between computation exception and regular termination) and you can add listeners to it so that you are notified of change of its state.

Some examples are needed here, if you have some, please do not hesitate to post them.