package com.ecloth.beta.domain.location.controller;


import com.ecloth.beta.domain.location.dto.Locational;
import com.ecloth.beta.domain.location.service.LocationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/member/locations")
@Api(tags = "위치저장API")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PutMapping
    public ResponseEntity<Void> updateLocation(@PathVariable Long memberId, @RequestBody Locational locational) {
        locationService.updateLocation(memberId, locational);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
