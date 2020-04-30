package com.github.mrazjava.booklink.rest.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * Generic error response in case of error scenarios for REST Service calls.
 *
 * @author AZ
 */
@ApiModel(value = "generic backend response",
        description = "returned in all error scenarios")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ErrorResponse {

    @ApiModelProperty
    private String message;

    @ApiModelProperty(value = "additional information about the error")
    private String description;

    @ApiModelProperty
    private String requestData;

    @ApiModelProperty(value = "simplified information about underlying exception")
    private String exceptionInfo;

    @ApiModelProperty(value = "simplified information about root cause exception")
    private String exceptionRootCause;

    @ApiModelProperty(value = "date and time when error response was created")
    private LocalDateTime timestamp;

    public ErrorResponse() {
        timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message) {
        this();
        this.message = message;
    }

    public ErrorResponse(String message, String description) {
        this(message);
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponse withMessage(String message) {
        setMessage(message);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ErrorResponse withDescription(String description) {
        setDescription(description);
        return this;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public ErrorResponse withRequestData(String requestData) {
        setRequestData(requestData);
        return this;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public ErrorResponse withExceptionInfo(String exceptionInfo) {
        setExceptionInfo(exceptionInfo);
        return this;
    }

    public String getExceptionRootCause() {
        return exceptionRootCause;
    }

    public void setExceptionRootCause(String exceptionRootCause) {
        this.exceptionRootCause = exceptionRootCause;
    }

    public ErrorResponse withExceptionRootCause(String exceptionRootCause) {
        setExceptionRootCause(exceptionRootCause);
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}