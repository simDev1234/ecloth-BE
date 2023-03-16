package com.ecloth.beta.weather.dto;

import lombok.Getter;
import lombok.Setter;

public class Back {

    @Getter
    @Setter
    public static class imgRequest{
        private String backgroundImgPath;
    }

    @Getter
    @Setter
    public static class titleRequest{
        private int title;
    }
}
