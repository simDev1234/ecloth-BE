package com.ecloth.beta.common.page;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Locale;
import java.util.Optional;

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

    public static CustomPage of(Pageable pageable) {

        Optional<Sort.Order> sort = pageable.getSort().stream().findFirst();

        return CustomPage.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .sortBy(sort.isEmpty() ? "" : sort.get().getProperty())
                .sortOrder(sort.isEmpty() ? "" : sort.get().getDirection().name())
                .build();
    }

    public Pageable toPageable(){
        return PageRequest.of(this.page, this.size,
                Sort.Direction.valueOf(this.sortOrder.toUpperCase(Locale.ROOT)), sortBy);
    }

}
