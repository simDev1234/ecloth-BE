package com.ecloth.beta.domain.clothes.controller;

import com.ecloth.beta.domain.clothes.dto.ClothesDto;
import com.ecloth.beta.domain.clothes.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@RestController
@RequiredArgsConstructor
public class ClothesController {
    private final ClothesService clothesService;

    @PostMapping("/step")
    public ResponseEntity<Integer> saveStep(@RequestBody ClothesDto request) {
        int step = clothesService.saveStep(request);
        return ResponseEntity.ok().body(step);
    }

    @PutMapping("/imgPath")
    public void saveImgPath(@RequestBody ClothesDto request) {
        clothesService.saveImgPath(request);
    }

    @GetMapping("/img/{id}")
    public ResponseEntity<?> getImg(@PathVariable Long id){
        ClothesDto.ImgResponse clothes = clothesService.getClothes(id);
        byte[] imgBytes = null;
        try {
            BufferedImage img = ImageIO.read(new File(clothes.getImgsrc()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            baos.flush();
            imgBytes = baos.toByteArray();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imgBytes);
    }
}
