package com.business.inventra.dto.response;

import com.business.inventra.dto.ProductDTO;

public class ProductResponse {
    private ProductDTO product;

    public ProductResponse() {
    }

    public ProductResponse(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
} 