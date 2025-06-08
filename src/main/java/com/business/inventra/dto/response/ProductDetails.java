package com.business.inventra.dto.response;

public class ProductDetails {
    private String pCode;
    private String pName;
    private String pCategory;
    private String pCategoryName;

    public String getPCode() {
        return pCode;
    }

    public void setPCode(String pCode) {
        this.pCode = pCode;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getPCategory() {
        return pCategory;
    }

    public void setPCategory(String pCategory) {
        this.pCategory = pCategory;
    }

    public String getPCategoryName() {
        return pCategoryName;
    }

    public void setPCategoryName(String pCategoryName) {
        this.pCategoryName = pCategoryName;
    }
} 