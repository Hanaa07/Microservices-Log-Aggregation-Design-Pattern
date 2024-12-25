package org.emsi;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final CentralLogStore centralLogStore = new CentralLogStore();
        final LogAggregator aggregator = new LogAggregator(centralLogStore, LogLevel.INFO);

        final LogProducer serviceA = new LogProducer("ServiceA", aggregator);
        final LogProducer serviceB = new LogProducer("ServiceB", aggregator);

        serviceA.generateLog(LogLevel.INFO, "This is an INFO log from ServiceA");
        serviceB.generateLog(LogLevel.ERROR, "This is an ERROR log from ServiceB");
        serviceA.generateLog(LogLevel.DEBUG, "This is a DEBUG log from ServiceA");

        aggregator.stop();
        centralLogStore.displayLogs();
    }
}