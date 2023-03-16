package com.ecloth.beta.weather.controller;


import com.ecloth.beta.weather.dto.WeatherDTO;
import com.ecloth.beta.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

//    @GetMapping("/weather/{x}/{y}")
//    public ResponseEntity<?> getWeather(@PathVariable("x") Long x, @PathVariable("y") Long y) throws ParseException {
//        try {
//            List<WeatherDTO> weather = weatherService.getWeather(x, y);
//            return ResponseEntity.ok().body(weather);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}
