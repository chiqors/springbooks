package me.chiqors.springbooks.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JSONResponse {
    @JsonProperty("http_code")
    private int httpCode;

    private String message;

    private Object data;

    @JsonProperty("errors")
    private List<String> errors;
}
