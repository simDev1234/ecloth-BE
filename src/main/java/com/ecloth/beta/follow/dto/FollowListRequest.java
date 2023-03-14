package com.ecloth.beta.follow.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FollowListRequest implements Serializable{

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String pointDirection;

    @Builder.Default
    private CustomPage page = new CustomPage(1, 5, "createdDate", "dsc");


}
