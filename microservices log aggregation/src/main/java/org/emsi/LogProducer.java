package org.emsi;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class LogProducer {

    private String serviceName;
    private LogAggregator aggregator;

    /**
     * Generates a log entry with the given log level and message.
     *
     * @param level The level of the log.
     * @param message The message of the log.
     */
    public void generateLog(LogLevel level, String message) {
        final LogEntry logEntry = new LogEntry(serviceName, level, message, LocalDateTime.now());
        System.out.println("Producing log: " + logEntry.getMessage());
        aggregator.collectLog(logEntry);
    }
}