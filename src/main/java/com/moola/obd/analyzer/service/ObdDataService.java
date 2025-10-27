package com.moola.obd.analyzer.service;

import com.moola.obd.analyzer.model.EngineMode;
import com.moola.obd.analyzer.model.ObdData;
import com.moola.obd.analyzer.model.Vin;
import com.moola.obd.analyzer.repository.ObdDataRepository;
import com.moola.obd.analyzer.repository.VinRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.data.domain.Page;

@Service
public class ObdDataService {

    private final ObdDataRepository obdDataRepository;
    private final VinRepository vinRepository;
    private final Random random = new Random();

    private EngineMode currentMode;
    private ObdData lastObdData;

    public ObdDataService(ObdDataRepository obdDataRepository, VinRepository vinRepository) {
        this.obdDataRepository = obdDataRepository;
        this.vinRepository = vinRepository;
    }

    @PostConstruct
    private void initialize() {
        // Set the initial state when the service starts. IDLE is a good default.
        this.currentMode = EngineMode.IDLE;
        this.lastObdData = createInitialObdData();
        System.out.println("ObdDataService initialized. Default mode: " + this.currentMode);
    }

    /**
     * Method to change the engine mode from an external source (e.g., a controller).
     * @param mode The new EngineMode to simulate.
     */
    public void setEngineMode(EngineMode mode) {
        if (mode != null) {
            this.currentMode = mode;
            System.out.println("Engine mode externally set to: " + this.currentMode);
        }
    }

    private ObdData createInitialObdData() {
        ObdData initialData = new ObdData();
        initialData.setEngineRpm(750);
        initialData.setSpeed(0);
        initialData.setCoolantTemp(20);
        initialData.setThrottlePosition(3);
        initialData.setO2SensorVoltage(0.1);
        initialData.setShortTermFuelTrim(0);
        initialData.setEngineLoad(20);
        initialData.setFuelLevel(75);
        return initialData;
    }

    public ObdData generateAndSaveData() {
        String vinString = "1HGCM82633A004352";
        Vin vin = vinRepository.findById(vinString).orElseGet(() -> {
            Vin newVin = new Vin();
            newVin.setVin(vinString);
            return vinRepository.save(newVin);
        });


        ObdData newData = generateDataForCurrentMode(vin);
        this.lastObdData = newData;

        obdDataRepository.save(newData);
        System.out.println("Generated data for mode: " + currentMode + " | RPM: " + newData.getEngineRpm() + " | Speed: " + String.format("%.2f", newData.getSpeed()));

        return newData;
    }

    public Page<ObdData> getPagedData(int page) {
        int pageSize = 50;
        Pageable pageable = PageRequest.of(page, pageSize);
        return obdDataRepository.findAll(pageable);
    }

    private ObdData generateDataForCurrentMode(Vin vin) {
        ObdData data = new ObdData();
        data.setVin(vin);
        data.setRecordTime(LocalDateTime.now());
        data.setFuelLevel(Math.max(0, lastObdData.getFuelLevel() - 0.01)); // Slowly consume fuel

        switch (currentMode) {
            case COLD_START:
                generateColdStartData(data);
                break;
            case IDLE:
                generateIdleData(data);
                break;
            case ACCELERATION:
                generateAccelerationData(data);
                break;
            case CRUISE:
                generateCruiseData(data);
                break;
            case DECELERATION:
                generateDecelerationData(data);
                break;
            case HIGH_LOAD:
                generateHighLoadData(data);
                break;
        }
        return data;
    }

    private void generateColdStartData(ObdData data) {
        data.setEngineRpm(generateRealisticValue(1100, 50, 900, 1500));
        data.setSpeed(0);
        data.setCoolantTemp(Math.min(lastObdData.getCoolantTemp() + 2, 95));
        data.setThrottlePosition(generateRealisticValue(15, 2, 12, 20));
        data.setO2SensorVoltage(generateRealisticValue(0.1, 0.05, 0.05, 0.2));
        data.setShortTermFuelTrim(generateRealisticValue(5, 2, 3, 10));
        data.setEngineLoad(generateRealisticValue(30, 3, 25, 40));
    }

    private void generateIdleData(ObdData data) {
        data.setEngineRpm(generateRealisticValue(750, 25, 700, 850));
        data.setSpeed(lastObdData.getSpeed() > 0 ? Math.max(0, lastObdData.getSpeed() - 2) : 0);
        data.setCoolantTemp(generateRealisticValue(95, 2, 90, 105));
        data.setThrottlePosition(generateRealisticValue(3, 1, 2, 5));
        data.setO2SensorVoltage(random.nextDouble() * 0.8 + 0.1);
        data.setShortTermFuelTrim(generateRealisticValue(0, 2, -5, 5));
        data.setEngineLoad(generateRealisticValue(20, 3, 15, 25));
    }

    private void generateAccelerationData(ObdData data) {
        data.setEngineRpm(Math.min(lastObdData.getEngineRpm() + 300, 6500));
        data.setSpeed(Math.min(lastObdData.getSpeed() + 7, 200));
        data.setCoolantTemp(generateRealisticValue(98, 2, 94, 108));
        data.setThrottlePosition(generateRealisticValue(60, 10, 40, 90));
        data.setO2SensorVoltage(generateRealisticValue(0.9, 0.05, 0.8, 0.95));
        data.setShortTermFuelTrim(generateRealisticValue(2, 2, -2, 6));
        data.setEngineLoad(generateRealisticValue(75, 10, 60, 95));
    }

    private void generateCruiseData(ObdData data) {
        data.setEngineRpm(generateRealisticValue(2500, 100, 1800, 3000));
        data.setSpeed(generateRealisticValue(lastObdData.getSpeed(), 2, 80, 130));
        data.setCoolantTemp(generateRealisticValue(95, 2, 90, 105));
        data.setThrottlePosition(generateRealisticValue(20, 3, 15, 30));
        data.setO2SensorVoltage(random.nextDouble() * 0.8 + 0.1);
        data.setShortTermFuelTrim(generateRealisticValue(0, 1.5, -4, 4));
        data.setEngineLoad(generateRealisticValue(35, 5, 25, 45));
    }

    private void generateDecelerationData(ObdData data) {
        data.setEngineRpm(Math.max(lastObdData.getEngineRpm() - 200, 800));
        data.setSpeed(Math.max(lastObdData.getSpeed() - 5, 0));
        data.setCoolantTemp(generateRealisticValue(92, 2, 90, 100));
        data.setThrottlePosition(0);
        data.setO2SensorVoltage(generateRealisticValue(0.1, 0.05, 0.05, 0.15));
        data.setShortTermFuelTrim(generateRealisticValue(-10, 3, -25, -5));
        data.setEngineLoad(generateRealisticValue(15, 3, 10, 20));
    }

    private void generateHighLoadData(ObdData data) {
        data.setEngineRpm(generateRealisticValue(4500, 500, 3500, 7000));
        data.setSpeed(Math.min(lastObdData.getSpeed() + 3, 200));
        data.setCoolantTemp(generateRealisticValue(105, 3, 100, 115));
        data.setThrottlePosition(generateRealisticValue(85, 5, 70, 100));
        data.setO2SensorVoltage(generateRealisticValue(0.9, 0.05, 0.85, 0.95));
        data.setShortTermFuelTrim(generateRealisticValue(1, 2, -2, 5));
        data.setEngineLoad(generateRealisticValue(90, 5, 80, 100));
    }


    private double generateRealisticValue(double base, double fluctuation, double min, double max) {
        double value = base + (ThreadLocalRandom.current().nextDouble() * 2 - 1) * fluctuation;
        return Math.max(min, Math.min(max, value));
    }

    private int generateRealisticValue(int base, int fluctuation, int min, int max) {
        return (int) generateRealisticValue((double) base, (double) fluctuation, (double) min, (double) max);
    }
}