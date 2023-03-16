package com.ecloth.beta.clothes.controller;


import com.ecloth.beta.clothes.dto.ClothesDto;
import com.ecloth.beta.clothes.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClothesController {
    private final ClothesService clothesService;

    @PostMapping("/step")
    public void saveStep(@RequestBody ClothesDto.stepRequest stepRequest) {
        clothesService.saveStep(stepRequest);
    }

//    @PutMapping("/imgPath")
//    public void saveImgPath(@RequestBody ClothesDto.imgRequest request) {
//        clothesService.saveImgPhat(request);
//    }

//    @GetMapping("/img/{id}")
//    public ResponseEntity<?> getImg(@PathVariable Long id){
//        return new ResponseEntity<>(clothesService.getImg(id), HttpStatus.OK);
//    }
}
