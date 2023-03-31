package com.ecloth.beta.common.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Locale;
import java.util.Optional;

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

    public static CustomPage of(Pageable pageable) {

        Optional<Sort.Order> sort = pageable.getSort().stream().findFirst();

        return CustomPage.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .sortBy(sort.isEmpty() ? "" : sort.get().getProperty())
                .sortOrder(sort.isEmpty() ? "" : sort.get().getDirection().name())
                .build();
    }

    public int findStartIdx(){
        return this.size * (this.page - 1);
    }

    public int findEndIdx(long total){
        int endIdx = findStartIdx() + this.size;
        return (int) Math.min(total, endIdx);
    }

    public Pageable toPageable(){
        return PageRequest.of(this.page, this.size,
                Sort.Direction.valueOf(this.sortOrder.toUpperCase(Locale.ROOT)), sortBy);
    }

}
