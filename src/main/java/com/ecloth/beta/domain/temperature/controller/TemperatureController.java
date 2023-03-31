package com.ecloth.beta.domain.temperature.controller;

import com.ecloth.beta.domain.temperature.service.TemperatureService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@Api(tags = "온도단계API")
@RequestMapping("/temperature")
public class TemperatureController {
    private final TemperatureService temperatureService;

    @Autowired
    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @GetMapping("/images")
    public ResponseEntity<Map<String, String>> getTemperatureImages(@RequestParam int temperature) {
        Map<String, String> images = new HashMap<>();
        String imageUrl1 = temperatureService.getImageUrlByTemperature(temperature);
        images.put("image1", imageUrl1);

        String imageUrl2;
        if (temperature >= 1 && temperature <= 8) {
            imageUrl2 = temperatureService.getImageUrlByTemperature(Double.parseDouble(temperature + "_rain"));
        } else {
            imageUrl2 = "";
        }
        images.put("image2", imageUrl2);

        return ResponseEntity.ok(images);
    }
}
