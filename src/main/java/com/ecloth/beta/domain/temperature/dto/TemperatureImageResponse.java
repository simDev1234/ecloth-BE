package com.ecloth.beta.domain.temperature.dto;

public class TemperatureImageResponse {
    private final String imageUrl;

    public TemperatureImageResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

