package com.ecloth.beta.follow.entity;

import com.ecloth.beta.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", nullable = false)
    private Long followId;

    private Long requesterId;

    private Long targetId;

    private boolean followStatus;

    public void changeFollowStatus(boolean isFollowing) {
        this.followStatus = isFollowing;
    }

}
