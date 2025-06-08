package com.business.inventra.service;

import com.business.inventra.dto.UnitDTO;
import com.business.inventra.model.Unit;
import com.business.inventra.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    @Transactional(readOnly = true)
    public List<UnitDTO> getAllUnits() {
        List<Unit> units = unitRepository.findAll();
        return units.stream()
                .map(unit -> new UnitDTO(
                        unit.getId(),
                        unit.getUnitCode(),
                        unit.getUnitDesc()))
                .collect(Collectors.toList());
    }
} 