package com.ecloth.beta.domain.clothes.service;

import com.ecloth.beta.domain.clothes.dto.ClothesDto;
import com.ecloth.beta.domain.clothes.entity.Clothes;
import com.ecloth.beta.domain.clothes.repository.ClothesRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ClothesService {

    private final ClothesRepository clothesRepository;

    private final String UPLOAD_DIR = "C:/temp/";

    public int getStep(int temperature) {
        int step = 0;

        if (temperature <= 4) {
            step = 1;
        } else if (temperature >= 5 && temperature <= 8) {
            step = 2;
        } else if (temperature >= 9 && temperature <= 11) {
            step = 3;
        } else if (temperature >= 12 && temperature <= 16) {
            step = 4;
        } else if (temperature >= 17 && temperature <= 19) {
            step = 5;
        } else if (temperature >= 20 && temperature <= 22) {
            step = 6;
        } else if (temperature >= 23 && temperature <= 27) {
            step = 7;
        } else {
            step = 8;
        }

        return step;
    }

    public String saveImage(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        String[] tokens = filename.split("\\.(?=[^\\.]+$)");
        String newFilename = UUID.randomUUID().toString() + "." + tokens[1];
        String filePath = UPLOAD_DIR + newFilename;
        File dest = new File(filePath);
        file.transferTo(dest);
        return newFilename;
    }

    public void saveClothes(int temperature, MultipartFile file) throws Exception {
        int step = getStep(temperature);
        String filename = saveImage(file);
        Clothes clothes = Clothes.builder()
                .step(step)
                .imgPath(filename)
                .build();
        clothesRepository.save(clothes);
    }

    public int saveStep(ClothesDto request) {
        Optional<Clothes> clothes = clothesRepository.findByStep((long) request.getStep());
        if (clothes.isPresent()) {
            Clothes entity = clothes.get();
            entity.setStep(request.getNewStep());
            return entity.getStep();
        } else {
            return 0;
        }
    }

    public void saveImgPath(ClothesDto request) {
        Optional<Clothes> clothes = clothesRepository.findByImgPath(request.getImgPath());
        if (clothes.isPresent()) {
            Clothes entity = clothes.get();
            entity.setImgPath(request.getNewImgPath());
            clothesRepository.save(entity);
        }

    }


    public ClothesDto.ImgResponse getClothes(Long id) {
        return null;
    }
}

