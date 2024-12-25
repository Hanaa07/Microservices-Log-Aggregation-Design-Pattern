package org.emsi;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LogEntry {
    private String serviceName;
    private LogLevel level;
    private String message;
    private LocalDateTime timestamp;
}
