package com.ecloth.beta.clothes.dto;

import lombok.Getter;
import lombok.Setter;


public class ClothesDto {

//    @Getter
//    @Setter
//    public static class Request{
//
//    private int startTemp;
//    private int endTemp;
//
//    }

    @Getter
    @Setter
    public static class imgRequest{
       private String imgPath;
    }

    @Getter
    @Setter
    public static class stepRequest{
        private int tmp;
    }

    @Getter
    @Setter
    public static class ImgResponse{
        // 이미지가 비가오든, 오지않는 이미지든 아래의 변수에 담는다.
        private String imgsrc;
    }
}


