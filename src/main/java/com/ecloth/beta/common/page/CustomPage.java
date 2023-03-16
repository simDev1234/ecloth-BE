package com.ecloth.beta.common.page;

import lombok.*;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomPage {

    private int page;
    private int size;
    private String sortBy;
    private String sortOrder;

    public int getStartIdx(){
        return getEndIdx() - size + 1;
    }

    public int getEndIdx(){
        return page * size - 1;
    }

}
