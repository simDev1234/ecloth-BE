package com.ecloth.beta.post.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Base {

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime create_date;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime update_date;
}
