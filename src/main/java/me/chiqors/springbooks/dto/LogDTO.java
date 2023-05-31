package me.chiqors.springbooks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LogDTO {
    private long id;

    private String timestamp;

    @JsonProperty("url_path")
    private String urlPath;

    @JsonProperty("host_name")
    private String hostName;

    @JsonProperty("http_method")
    private String httpMethod;

    @JsonProperty("http_code")
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
