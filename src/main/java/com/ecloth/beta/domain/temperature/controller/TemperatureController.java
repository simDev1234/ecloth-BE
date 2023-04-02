package com.ecloth.beta.domain.temperature.controller;

import com.ecloth.beta.domain.temperature.service.TemperatureService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@Api(tags = "옷차림API")
@RequestMapping("/api/temperature")
public class TemperatureController {
    private final TemperatureService temperatureService;

    @Autowired
    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @GetMapping("/images")
    public ResponseEntity<String[]> getImageUrlsByTemperature(@RequestParam double temperature) {
        String[] imageUrls = temperatureService.getImageUrlsByTemperature(temperature);
        return ResponseEntity.ok(imageUrls);
    }
}
