package com.business.inventra.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import com.business.inventra.util.KeyGenerator;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "roles")
public class Role extends BaseModel {

    private static final String ROLE_PREFIX = KeyGenerator.ROLE_PREFIX;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;
    
    public Role() {
        this.prefix = ROLE_PREFIX;
    }

    public Role(String name, String description) {
        this.prefix = ROLE_PREFIX;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
