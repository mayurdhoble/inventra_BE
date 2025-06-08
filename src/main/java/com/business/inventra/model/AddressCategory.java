package com.business.inventra.model;

import jakarta.persistence.*;

@Entity
@Table(name = "address_category_master")
public class AddressCategory extends BaseModel {
    private static final String ADDRESS_CATEGORY_PREFIX = "ADC";

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    public AddressCategory() {
        this.prefix = ADDRESS_CATEGORY_PREFIX;
    }

    public AddressCategory(String name, String description) {
        this.prefix = ADDRESS_CATEGORY_PREFIX;
        this.name = name;
        this.description = description;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 