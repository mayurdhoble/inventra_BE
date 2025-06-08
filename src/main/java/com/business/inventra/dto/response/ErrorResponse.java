package com.business.inventra.dto.response;

import java.util.List;
import java.util.ArrayList;

public class ErrorResponse {
    private String status;
    private List<ErrorDetail> errors;

    public ErrorResponse() {
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(String status, List<ErrorDetail> errors) {
        this.status = status;
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDetail> errors) {
        this.errors = errors;
    }

    public void addError(String errorCode, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ErrorDetail(errorCode, message));
    }

    public void addFieldError(String field, String errorCode, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ErrorDetail(errorCode, message, field));
    }

    public static class ErrorDetail {
        private String error_code;
        private String message;
        private String field;

        public ErrorDetail() {
        }

        public ErrorDetail(String error_code, String message) {
            this.error_code = error_code;
            this.message = message;
        }

        public ErrorDetail(String error_code, String message, String field) {
            this.error_code = error_code;
            this.message = message;
            this.field = field;
        }

        public String getError_code() {
            return error_code;
        }

        public void setError_code(String error_code) {
            this.error_code = error_code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
} 