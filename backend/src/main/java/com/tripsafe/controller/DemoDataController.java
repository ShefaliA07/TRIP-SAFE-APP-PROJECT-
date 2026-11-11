package com.tripsafe.controller;

import com.tripsafe.service.DemoDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoDataController {

    private final DemoDataService demoDataService;

    public DemoDataController(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadDemoData() {
        demoDataService.loadDemoData();
        return ResponseEntity.ok(Map.of("message", "Demo data loaded successfully!"));
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetAllData() {
        demoDataService.resetAllData();
        return ResponseEntity.ok(Map.of("message", "All application data reset successfully."));
    }
}
