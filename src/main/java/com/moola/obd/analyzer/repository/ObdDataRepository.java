package com.moola.obd.analyzer.repository;

import com.moola.obd.analyzer.model.ObdData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObdDataRepository extends JpaRepository<ObdData, Long> {
}


