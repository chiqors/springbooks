package me.chiqors.springbooks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LogDTO {
    private long id;
    private String timestamp;
    private String urlPath;
    private String hostName;
    private String httpMethod;
    private int httpCode;
    private String message;

    public LogDTO(String timestamp, String urlPath, String hostName, String httpMethod, int httpCode, String message) {
        this.timestamp = timestamp;
        this.urlPath = urlPath;
        this.hostName = hostName;
        this.httpMethod = httpMethod;
        this.httpCode = httpCode;
        this.message = message;
    }
}
