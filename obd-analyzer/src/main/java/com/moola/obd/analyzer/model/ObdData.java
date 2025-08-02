package com.moola.obd.analyzer.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ObdData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vin;
    private double speed;
    private int rpm;
    private double fuelLevel;
    private int coolantTemp;
    private int intakeAirTemp;
    private double engineLoad;
    private double throttlePosition;
    private LocalDateTime timestamp;
}
