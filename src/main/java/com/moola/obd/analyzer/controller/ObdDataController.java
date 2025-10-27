package com.moola.obd.analyzer.controller;

import com.moola.obd.analyzer.model.ObdData;
import com.moola.obd.analyzer.service.ObdDataService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/obd")
@CrossOrigin(origins = "*")
public class ObdDataController {

    private final ObdDataService service;

    public ObdDataController(ObdDataService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ObdData> getPagedData(@RequestParam(defaultValue = "0") int page) {
        return service.getPagedData(page);
    }
}
