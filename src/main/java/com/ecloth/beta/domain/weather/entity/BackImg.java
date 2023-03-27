package com.ecloth.beta.domain.weather.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class BackImg {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String backgroundImgPath;
    private int title;

    public Object backgroundImgPath() {
        return this.backgroundImgPath;
    }

    public void backgroundImgPath(Object backgroundImgPath) {
    }
}