package com.business.inventra.repository;

import com.business.inventra.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, String> {
    Optional<Unit> findByUnitCode(String unitCode);
} 