package com.ecloth.beta.domain.location.controller;

import com.ecloth.beta.domain.location.entity.Locational;
import com.ecloth.beta.domain.location.service.LocationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/locations")
@Api(tags = "위치저장API")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Locational> createLocation(@RequestParam int x, @RequestParam int y) {
        Locational locational = locationService.saveLocation(x, y);
        return new ResponseEntity<>(locational, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Locational> getLocationById(@PathVariable Long id) {
        Locational locational = locationService.getLocationById(id);
        if (locational == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(locational, HttpStatus.OK);
    }
}



