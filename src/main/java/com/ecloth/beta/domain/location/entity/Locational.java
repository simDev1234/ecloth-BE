package com.ecloth.beta.domain.location.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Locational {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    public Locational(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Locational() {

    }
}





