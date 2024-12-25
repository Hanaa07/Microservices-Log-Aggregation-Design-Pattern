package org.emsi;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LogAggregator {

    private static final int BUFFER_THRESHOLD = 3;
    private final CentralLogStore centralLogStore;
    private final ConcurrentLinkedQueue<LogEntry> buffer = new ConcurrentLinkedQueue<>();
    private final LogLevel minLogLevel;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AtomicInteger logCount = new AtomicInteger(0);

    /**
     * constructor of LogAggregator.
     *
     * @param centralLogStore central log store implement
     * @param minLogLevel min log level to store log
     */
    public LogAggregator(CentralLogStore centralLogStore, LogLevel minLogLevel) {
        this.centralLogStore = centralLogStore;
        this.minLogLevel = minLogLevel;
        startBufferFlusher();
    }

    /**
     * Collects a given log entry, and filters it by the defined log level.
     *
     * @param logEntry The log entry to collect.
     */
    public void collectLog(LogEntry logEntry) {
        if (logEntry.getLevel() == null || minLogLevel == null) {
            System.out.println("Log level or threshold level is null. Skipping.");
            return;
        }

        if (logEntry.getLevel().compareTo(minLogLevel) < 0) {
            System.out.println("Log level below threshold. Skipping.");
            return;
        }

        buffer.offer(logEntry);

        if (logCount.incrementAndGet() >= BUFFER_THRESHOLD) {
            flushBuffer();
        }
    }

    /**
     * Stops the log aggregator service and flushes any remaining logs to
     * the central log store.
     *
     * @throws InterruptedException If any thread has interrupted the current thread.
     */
    public void stop() throws InterruptedException {
        executorService.shutdownNow();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("Log aggregator did not terminate.");
        }
        flushBuffer();
    }

    private void flushBuffer() {
        LogEntry logEntry;
        while ((logEntry = buffer.poll()) != null) {
            centralLogStore.storeLog(logEntry);
            logCount.decrementAndGet();
        }
    }

    private void startBufferFlusher() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(5000); // Flush every 5 seconds.
                    flushBuffer();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
