package com.ecloth.beta.follow.dto;

import com.ecloth.beta.common.page.CustomPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class FollowListRequest extends CustomPage{

    @NotBlank
    private String dir;

    FollowListRequest(String dir){
        super(1, 5, "created_date", "dsc");
        this.dir = dir;
    }

    FollowListRequest(String dir, int page, int size, String sortBy, String sortOrder){
        super(page, size, sortBy, sortOrder);
        this.dir = dir;
    }

    public CustomPage getCustomPage(){
        return new CustomPage(this.getPage(), this.getSize(), this.getSortBy(), this.getSortOrder());
    }

}
