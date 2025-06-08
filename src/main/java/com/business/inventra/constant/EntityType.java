package com.business.inventra.constant;

public enum EntityType {
    USER("USER"),
    CUSTOMER("CUSTOMER");

    private final String value;

    EntityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
} 