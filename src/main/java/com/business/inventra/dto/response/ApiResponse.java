package com.business.inventra.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private Object errors;

    public ApiResponse() {}

    public ApiResponse(int status, String message, T data, Object errors) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, data, null);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message, Object errors) {
        return new ApiResponse<>(status.value(), message, null, errors);
    }

    public static <T> ApiResponse<T> error(String message, Object errors) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), message, null, errors);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}
