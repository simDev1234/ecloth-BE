package com.ecloth.beta.domain.post.posting.dto;

import com.ecloth.beta.common.page.CustomPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberPostingListRequest extends CustomPage{
    public MemberPostingListRequest(int page, int size, String sortBy, String sortOrder, Long memberId) {
        super(page, size, sortBy, sortOrder);
    }

    public MemberPostingListRequest(int size, String sortBy, String sortOrder, Long memberId) {
        super(1, size, sortBy, sortOrder);
    }

    public MemberPostingListRequest(String sortBy, String sortOrder, Long memberId) {
        super(1, 10, sortBy, sortOrder);
    }

    public MemberPostingListRequest(String sortOrder, Long memberId) {
        super(1, 10, "registerDate", sortOrder);
    }

    public MemberPostingListRequest(Long memberId) {
        super(1, 10, "registerDate", "DESC");
    }

    public CustomPage toCustomPage(){
        return new CustomPage(this.getPage(), this.getSize(), this.getSortBy(), this.getSortOrder());
    }

}


