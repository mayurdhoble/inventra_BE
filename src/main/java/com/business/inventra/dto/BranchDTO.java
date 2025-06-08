package com.business.inventra.dto;

public class BranchDTO {
    private String id;
    private String orgID;
    private String name;
    private String gstNumber;

    public BranchDTO() {
    }

    public BranchDTO(String id, String orgID, String name, String gstNumber) {
        this.id = id;
        this.orgID = orgID;
        this.name = name;
        this.gstNumber = gstNumber;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getOrgID() {
        return orgID;
    }

    public String getName() {
        return name;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }
} 