package com.ecloth.beta.domain.follow.dto;

import com.ecloth.beta.common.page.CustomPage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Slf4j
public class FollowListRequest extends CustomPage{

    @NotBlank
    private String dir;

    FollowListRequest(){
        super(1, 5, "registerDate", "DESC");
    }

    FollowListRequest(String dir){
        this.dir = dir;
    }

    FollowListRequest(String dir, int page){
        super(page, 5, "registerDate", "DESC");
        this.dir = dir;
    }

    FollowListRequest(String dir, int page, int size){
        super(page, size, "registerDate", "DESC");
        this.dir = dir;
    }

    FollowListRequest(String dir, int page, int size, String sortBy){
        super(page, size, sortBy, "DESC");
        this.dir = dir;
    }

    public CustomPage toCustomPage(){
        return new CustomPage(this.getPage(), this.getSize(), this.getSortBy(), this.getSortOrder());
    }

}
