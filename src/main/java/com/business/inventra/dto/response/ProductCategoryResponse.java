package com.business.inventra.dto.response;

import com.business.inventra.dto.ProductCategoryDTO;

public class ProductCategoryResponse {
    private ProductCategoryDTO category;

    public ProductCategoryResponse() {
    }

    public ProductCategoryResponse(ProductCategoryDTO category) {
        this.category = category;
    }

    public ProductCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ProductCategoryDTO category) {
        this.category = category;
    }
} 