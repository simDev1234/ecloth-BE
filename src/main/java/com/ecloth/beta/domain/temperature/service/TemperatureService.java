package com.ecloth.beta.domain.temperature.service;


import org.springframework.stereotype.Service;

@Service
public class TemperatureService {
    private String s3BucketUrl = "https://s3.amazonaws.com/weatheroutfit/";

    public String getImageUrlByTemperature(double temperature) {
        int level = getTemperatureLevel(temperature);
        String imageUrl = "image-" + level + ".jpeg";
        return s3BucketUrl + imageUrl;
    }

    private int getTemperatureLevel(double temperature) {
        if (temperature <= 4) {
            return 1;
        } else if (temperature <= 8) {
            return 2;
        } else if (temperature <= 11) {
            return 3;
        } else if (temperature <= 16) {
            return 4;
        } else if (temperature <= 19) {
            return 5;
        } else if (temperature <= 22) {
            return 6;
        } else if (temperature <= 27) {
            return 7;
        } else {
            return 8;
        }
    }


}
