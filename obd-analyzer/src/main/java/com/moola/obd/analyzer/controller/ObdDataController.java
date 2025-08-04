package com.moola.obd.analyzer.controller;

import com.moola.obd.analyzer.model.ObdData;
import com.moola.obd.analyzer.service.ObdDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/obd")
@CrossOrigin(origins = "*") // Allow frontend access
public class ObdDataController {

    private final ObdDataService service;

    public ObdDataController(ObdDataService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<ObdData> getAllData() {
        return service.getAllData();
    }
}
