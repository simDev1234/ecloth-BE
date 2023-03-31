package com.ecloth.beta.domain.temperature.entity;


import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "background_image")
public class BackgroundImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backgroundId;

    private String imagePath;

}
