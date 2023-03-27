package com.ecloth.beta.domain.weather.controller;


import com.ecloth.beta.domain.weather.service.WeatherService;
import org.springframework.web.bind.annotation.RestController;

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
