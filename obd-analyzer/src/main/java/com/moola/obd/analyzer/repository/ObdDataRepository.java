package com.moola.obd.analyzer.repository;

import com.moola.obd.analyzer.model.ObdData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObdDataRepository extends JpaRepository<ObdData, Long> {
}


