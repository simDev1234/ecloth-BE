package com.ecloth.beta.domain.post.posting.entity;

import com.ecloth.beta.common.entity.BaseEntity;
import com.ecloth.beta.utill.S3FileUploader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;

    private String url;

    private boolean isRepresentImage;

    public void changePosting(Posting posting) {
        this.posting = posting;
        if (!posting.getImageList().contains(this)) {
            posting.getImageList().add(this);
        }
    }

    public String getImageUrl() {
        return getImageUrl();
    }
}
