package com.ecloth.beta.domain.post.comment.dto;

import com.ecloth.beta.common.page.CustomPage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentListRequest extends CustomPage {

    public CommentListRequest(int page, int size, String sortBy, String sortOrder) {
        super(page, size, sortBy, sortOrder);
    }

    public CommentListRequest(int size, String sortBy, String sortOrder) {
        super(1, size, sortBy, sortOrder);
    }

    public CommentListRequest(String sortBy, String sortOrder) {
        super(1, 10, sortBy, sortOrder);
    }

    public CommentListRequest(String sortOrder) {
        super(1, 10, "registerDate", sortOrder);
    }

    public CommentListRequest() {
        super(1, 10, "registerDate", "ASC");
    }

    public CustomPage toCustomPage(){
        return new CustomPage(this.getPage(), this.getSize(), this.getSortBy(), this.getSortOrder());
    }

}
