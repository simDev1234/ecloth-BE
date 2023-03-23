package com.ecloth.beta.common.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CustomPage {

    private int page;
    private int size;
    private String sortBy;
    private String sortOrder;

    public static CustomPage of(CustomPage requestPage, int realSize){
        return CustomPage.builder()
                .page(requestPage.getPage())
                .size(realSize)
                .sortBy(requestPage.sortBy)
                .sortOrder(requestPage.getSortOrder())
                .build();
    }

    public int getStartIdx(){
        return this.size * (this.page - 1);
    }

    public int getEndIdx(long total){
        int endIdx = getStartIdx() + this.size;
        return (int) Math.min(total, endIdx);
    }
}
