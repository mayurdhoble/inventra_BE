package com.business.inventra.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import com.business.inventra.util.KeyGenerator;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "unit_master")
public class Unit extends BaseModel {

    private static final String UNIT_PREFIX = "UNT";

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "unit_code", nullable = false, unique = true)
    private String unitCode;

    @Column(name = "unit_desc", nullable = false)
    private String unitDesc;

    public Unit() {
        this.prefix = UNIT_PREFIX;
    }

    public Unit(String unitCode, String unitDesc) {
        this.prefix = UNIT_PREFIX;
        this.unitCode = unitCode;
        this.unitDesc = unitDesc;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }
} 