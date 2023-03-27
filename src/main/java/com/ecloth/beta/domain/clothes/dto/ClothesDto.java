package com.ecloth.beta.domain.clothes.dto;

import lombok.Getter;
import lombok.Setter;


public class ClothesDto {

    public String getImgsrc() {
        return null;
    }

    public int getStep() {
        return 0;
    }

    public Object getNewStep() {
        return null;
    }

    public String getImgPath() {
        return null;
    }

    public Object getNewImgPath() {
        return null;
    }

    @Getter
    @Setter
    public static class ImgRequest{
       private String imgPath;
    }

    @Getter
    @Setter
    public static class StepRequest{
        private int tmp;
    }

    @Getter
    @Setter
    public static class ImgResponse{
        // 이미지가 비가오든, 오지않는 이미지든 아래의 변수에 담는다.
        private String imgsrc;
    }
}


