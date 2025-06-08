package com.business.inventra.controller;

import com.business.inventra.dto.request.StockRequest;
import com.business.inventra.dto.response.ApiResponse;
import com.business.inventra.dto.response.StockResponse;
import com.business.inventra.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations/{orgId}")
@CrossOrigin(origins = "*")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/stock")
    public ResponseEntity<ApiResponse<StockResponse>> updateStock(
            @PathVariable String orgId,
            @Valid @RequestBody StockRequest request) {
        request.setOrgId(orgId);
        StockResponse response = stockService.updateStock(request);
        return ResponseEntity.ok(ApiResponse.success("Stock updated successfully", response));
    }
} 