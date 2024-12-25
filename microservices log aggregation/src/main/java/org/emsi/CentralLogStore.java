package org.emsi;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CentralLogStore {
    private final ConcurrentLinkedQueue<LogEntry> logs = new ConcurrentLinkedQueue<>();

    /**
     * Stores the given log entry into the central log store.
     *
     * @param logEntry The log entry to store.
     */
    public void storeLog(LogEntry logEntry) {
        if (logEntry == null) {
            System.out.println("Received null log entry. Skipping.");
            return;
        }
        logs.offer(logEntry);
    }

    /**
     * Displays all logs currently stored in the central log store.
     */
    public void displayLogs() {
        System.out.println("----- Centralized Logs -----");
        for (LogEntry logEntry : logs) {
            System.out.println(
                    logEntry.getTimestamp() + " [" + logEntry.getLevel() + "] " + logEntry.getMessage());
        }
    }
}
