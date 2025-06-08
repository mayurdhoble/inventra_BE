package com.business.inventra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitDTO {
    private String id;
    
    @JsonProperty("unitCode")
    private String unitCode;
    
    @JsonProperty("unitDesc")
    private String unitDesc;

    public UnitDTO() {
    }

    public UnitDTO(String id, String unitCode, String unitDesc) {
        this.id = id;
        this.unitCode = unitCode;
        this.unitDesc = unitDesc;
    }

    public String getId() {
        return id;
    }

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