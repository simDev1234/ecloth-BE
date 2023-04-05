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

}
