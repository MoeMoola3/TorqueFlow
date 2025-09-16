package com.moola.obd.analyzer.repository;

import com.moola.obd.analyzer.model.Vin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VinRepository extends JpaRepository<Vin, String> {
}
