package ru.itmo.soa.soaspacemarinejava.rest.data;

public class ErrorResponse {

    private String timestamp;

    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(String timestamp, String errorMessage) {
        this.timestamp = timestamp;
        this.errorMessage = errorMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
