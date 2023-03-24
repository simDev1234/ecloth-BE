package com.ecloth.beta.domain.weather.controller;


import com.ecloth.beta.domain.weather.entity.BackImg;
import com.ecloth.beta.domain.weather.service.BackService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/back-imgs")
public class BackController {

    private final BackService backService;

    public BackController(BackService backService) {
        this.backService = backService;
    }

    @GetMapping("")
    public List<BackImg> getAllBackImgs() {
        return backService.getAllBackImgs();
    }
    @GetMapping("/{id}")
    public ResponseEntity<BackImg> getBackImgById(@PathVariable("id") Long id) {
        BackImg backImg = backService.getBackImgById(id);
        return ResponseEntity.ok().body(backImg);
    }

    @PostMapping("")
    public ResponseEntity<Void> saveBackImg(@RequestBody BackImg backImg, UriComponentsBuilder builder) {
        backService.saveBackImg(backImg);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/back-imgs/{id}").buildAndExpand(backImg.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BackImg> updateBackImg(@PathVariable("id") Long id, @RequestBody BackImg backImg) {
        backImg.setId(id);
        backService.updateBackImg(backImg);
        return ResponseEntity.ok().body(backImg);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBackImg(@PathVariable("id") Long id) {
        backService.deleteBackImg(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/img")
    public ResponseEntity<?> imgs(){
        return new ResponseEntity<>(backService.getImg(), HttpStatus.OK);
    }

}