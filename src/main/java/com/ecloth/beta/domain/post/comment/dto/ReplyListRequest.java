package com.ecloth.beta.domain.post.comment.dto;

import com.ecloth.beta.common.page.CustomPage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyListRequest extends CustomPage {

    public ReplyListRequest(int page, int size, String sortBy, String sortOrder) {
        super(page, size, sortBy, sortOrder);
    }

    public ReplyListRequest(int size, String sortBy, String sortOrder) {
        super(1, size, sortBy, sortOrder);
    }

    public ReplyListRequest(String sortBy, String sortOrder) {
        super(1, 10, sortBy, sortOrder);
    }

    public ReplyListRequest(String sortOrder) {
        super(1, 10, "registerDate", sortOrder);
    }

    public ReplyListRequest() {
        super(1, 10, "registerDate", "ASC");
    }

    public CustomPage toCustomPage(){
        return new CustomPage(this.getPage(), this.getSize(), this.getSortBy(), this.getSortOrder());
    }

}
