package com.ecloth.beta.domain.temperature.service;


import org.springframework.stereotype.Service;

@Service
public class TemperatureService {
    private String s3BucketUrl = "https://s3.amazonaws.com/weatheroutfit/";

    public String[] getImageUrlsByTemperature(double temperature) {
        int level = getTemperatureLevel(temperature);
        String[] imageUrls = new String[2];
        imageUrls[0] = s3BucketUrl + level + "_rain.jpeg";
        imageUrls[1] = s3BucketUrl + level + ".jpeg";
        return imageUrls;
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
