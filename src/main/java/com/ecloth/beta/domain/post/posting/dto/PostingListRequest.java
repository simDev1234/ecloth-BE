package com.ecloth.beta.domain.post.posting.dto;

import com.ecloth.beta.common.page.CustomPage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class PostingListRequest extends CustomPage {

    public PostingListRequest() {
        super(1, 10, "registerDate", "DESC");
    }

    @Override
    public Pageable toPageable() {
        return super.toPageable();
    }
}
