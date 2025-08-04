package com.moola.obd.analyzer.service;

import com.moola.obd.analyzer.model.ObdData;
import com.moola.obd.analyzer.repository.ObdDataRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ObdDataService {

    private final ObdDataRepository repository;
    private final Random random = new Random();

    public ObdDataService(ObdDataRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 5000) // This will run the method every 5 seconds
    public void generateAndSaveData() {
        ObdData data = new ObdData();
        data.setVin("1HGCM82633A004352");
        data.setSpeed(random.nextDouble() * 120); // km/h
        data.setRpm(random.nextInt(7000));
        data.setFuelLevel(random.nextDouble() * 100);
        data.setCoolantTemp(random.nextInt(120));
        data.setIntakeAirTemp(random.nextInt(100));
        data.setEngineLoad(random.nextDouble() * 100);
        data.setThrottlePosition(random.nextDouble() * 100);
        data.setTimestamp(LocalDateTime.now());

        repository.save(data);
        System.out.println("Generated and saved new OBD data at " + data.getTimestamp());
    }

    public List<ObdData> getAllData() {
        return repository.findAll();
    }

}
