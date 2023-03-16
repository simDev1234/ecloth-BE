package com.ecloth.beta.weather.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDTO {
        private String baseDate;
        private String baseTime;
        private String category;
        private Long nx;
        private Long ny;
        private String obsrValue;


}
