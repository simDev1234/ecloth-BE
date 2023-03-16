package com.ecloth.beta.clothes.service;


import com.ecloth.beta.clothes.dto.ClothesDto;
import com.ecloth.beta.clothes.entity.Clothes;
import com.ecloth.beta.clothes.repository.ClothesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final ClothesRepository clothesRepository;

    public int saveStep(ClothesDto.stepRequest request) {
        int step;
        if (request.getTmp() < 20){
            step = 1;
        } else {
            step = 2;
        }
        return step;
    }

    public ClothesDto.ImgResponse getClothes(ClothesDto.stepRequest request) {
        int step = saveStep(request);
        Clothes clothes = clothesRepository.findByStep(step).orElseThrow(() -> new RuntimeException("Clouthes not found"));
        ClothesDto.ImgResponse response = new ClothesDto.ImgResponse();
        response.setImgsrc(clothes.getImgPath());
        return response;
    }

    public void saveImgPhat(ClothesDto.imgRequest request) {

    }


//    @Transactional
//    public void saveImg(Long id, ClothesDto.imgRequest request) {
//        Optional<Clothes> findClothes = clothesRepository.findById(id);
//        if (!findClothes.isPresent()) {
//            throw new RuntimeException("존재하지 않습니다.");
//        }
//        Clothes clothes = findClothes.get();
//        clothes.setImgPath(request.getImgPath());
//    }
//
//    public String getImg(Long id) {
//        Optional<Clothes> findClothes = clothesRepository.findById(id);
//        if (!findClothes.isPresent()) {
//            throw new RuntimeException("존재하지 않습니다.");
//        }
//        Clothes clothes = findClothes.get();
//        return clothes.getImgPath();
//    }
//
//    public void writeLevel(LevelDto levelDto) {
//        Level level = Level.builder()
//                .startTemp(levelDto.getStartTemp())
//                .endTemp(levelDto.getEndTemp())
//                .number(levelDto.getNumber())
//                .build();
//
//    }

}
