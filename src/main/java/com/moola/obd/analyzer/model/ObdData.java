//package com.moola.obd.analyzer.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//public class ObdData {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private double speed;
//    private int rpm;
//    private double fuelLevel;
//    private int coolantTemp;
//    private int intakeAirTemp;
//    private double engineLoad;
//    private double throttlePosition;
//    private LocalDateTime recordTime;
//
//    @ManyToOne
//    @JoinColumn(name = "vin_number", referencedColumnName = "vin")
//    private Vin vin;
//}


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

    private int engineRpm;
    private double speed; // Assuming km/h
    private int coolantTemp; // Celsius
    private double throttlePosition; // Percentage (0-100)
    private double o2SensorVoltage; // Volts (typically 0.1V - 0.9V)
    private double shortTermFuelTrim; // Percentage (-100 to 100)
    private double engineLoad; // Percentage
    private double fuelLevel; // Percentage
    private LocalDateTime recordTime;

    @ManyToOne
    @JoinColumn(name = "vin_number", referencedColumnName = "vin")
    private Vin vin;
}