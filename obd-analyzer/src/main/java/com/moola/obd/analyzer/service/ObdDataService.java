package com.moola.obd.analyzer.service;

import com.moola.obd.analyzer.model.ObdData;
import com.moola.obd.analyzer.model.Vin;
import com.moola.obd.analyzer.repository.ObdDataRepository;
import com.moola.obd.analyzer.repository.VinRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ObdDataService {

    private final ObdDataRepository obdDataRepository;
    private final VinRepository vinRepository;
    private final Random random = new Random();

    public ObdDataService(ObdDataRepository obdDataRepository, VinRepository vinRepository) {
        this.obdDataRepository = obdDataRepository;
        this.vinRepository = vinRepository;
    }

    public ObdData generateAndSaveData() {
        String vinString = "1HGCM82633A004352";

        Vin vin = vinRepository.findById(vinString).orElseGet(() -> {
            Vin newVin = new Vin();
            newVin.setVin(vinString);
            return vinRepository.save(newVin);
        });

        ObdData data = new ObdData();
        data.setVin(vin);
        data.setSpeed(random.nextDouble() * 120);
        data.setRpm(random.nextInt(7000));
        data.setFuelLevel(random.nextDouble() * 100);
        data.setCoolantTemp(random.nextInt(120));
        data.setIntakeAirTemp(random.nextInt(100));
        data.setEngineLoad(random.nextDouble() * 100);
        data.setThrottlePosition(random.nextDouble() * 100);
        data.setRecordTime(LocalDateTime.now());

        obdDataRepository.save(data);
        System.out.println("Generated and saved new OBD data for VIN " + vin.getVin() + " at " + data.getRecordTime());

        return data;
    }

    public List<ObdData> getAllData() {
        return obdDataRepository.findAll();
    }
}
