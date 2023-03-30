package com.ecloth.beta.common.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Locale;

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

    public int findStartIdx(){
        return this.size * (this.page - 1);
    }

    public int findEndIdx(long total){
        int endIdx = findStartIdx() + this.size;
        return (int) Math.min(total, endIdx);
    }

}
