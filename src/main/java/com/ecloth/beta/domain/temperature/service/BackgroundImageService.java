package com.ecloth.beta.domain.temperature.service;


import com.ecloth.beta.domain.temperature.dto.Back;
import com.ecloth.beta.domain.temperature.entity.BackgroundImage;
import com.ecloth.beta.domain.temperature.repository.BackgroundImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackgroundImageService {
    private final BackgroundImageRepository backgroundImageRepository;

    public BackgroundImageService(BackgroundImageRepository backgroundImageRepository) {
        this.backgroundImageRepository = backgroundImageRepository;
    }

    public static String getBackgroundPath() {
        return getBackgroundPath();
    }

    public Back getBackImage(Integer backgroundId) {
        Long id = backgroundId.longValue(); // int -> long 형 변환
        List<BackgroundImage> backs = backgroundImageRepository.findByBackgroundId(id);
        if (backs.size() > 0) {
            BackgroundImage backgroundImage = backs.get(0);
            Back dto = new Back();
            dto.setBackgroundId(backgroundImage.getBackgroundId());
            dto.setImagePath(backgroundImage.getImagePath());
            return dto;
        } else {
            // 해당 id에 대한 배경 이미지를 찾을 수 없는 경우 예외처리
            throw new RuntimeException("Invalid background id: " + backgroundId);
        }
    }
}
