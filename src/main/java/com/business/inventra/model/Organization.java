package com.business.inventra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import com.business.inventra.util.KeyGenerator;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization extends BaseModel {

    private static final String ORGANIZATION_PREFIX = KeyGenerator.ORGANIZATION_PREFIX;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public Organization() {
        this.prefix = ORGANIZATION_PREFIX;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    
    
}
