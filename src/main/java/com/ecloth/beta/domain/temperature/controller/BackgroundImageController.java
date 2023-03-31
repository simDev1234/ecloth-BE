package com.ecloth.beta.domain.temperature.controller;


import com.ecloth.beta.domain.temperature.dto.Back;
import com.ecloth.beta.domain.temperature.entity.BackgroundImage;
import com.ecloth.beta.domain.temperature.service.BackgroundImageService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "배경이미지API")
@RequestMapping("/background")
public class BackgroundImageController {
    private final BackgroundImageService backgroundImageService;

    public BackgroundImageController(BackgroundImageService backgroundImageService) {
        this.backgroundImageService = backgroundImageService;
    }

    @GetMapping("/{backgroundId}")
    public Back getBackImage(@PathVariable Integer backgroundId) {
        return backgroundImageService.getBackImage(backgroundId);
    }

    @PostMapping("/(backgroundPath}")
    public String getBackgroundImagePath(@RequestBody Back back) {
        String imagePath = BackgroundImageService.getBackgroundPath();
        BackgroundImage Back = new BackgroundImage();
        return Back.getImagePath();
    }

}