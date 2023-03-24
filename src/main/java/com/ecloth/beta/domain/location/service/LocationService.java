package com.ecloth.beta.domain.location.service;

import com.ecloth.beta.domain.location.entity.Locational;
import com.ecloth.beta.domain.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Locational saveLocation(int x, int y) {
        Locational locational = new Locational(x, y);
        return locationRepository.save(locational);
    }

    public Locational getLocationById(Long id) {
        return locationRepository.findById(id).orElseThrow(() -> new RuntimeException("error"));
    }
}

