package com.business.inventra.controller;

import com.business.inventra.dto.UnitDTO;
import com.business.inventra.dto.response.ApiResponse;
import com.business.inventra.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/units")
public class UnitController {

    @Autowired
    private UnitService unitService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UnitDTO>>> getAllUnits() {
        List<UnitDTO> units = unitService.getAllUnits();
        return ResponseEntity.ok(ApiResponse.success("Units retrieved successfully", units));
    }
} 