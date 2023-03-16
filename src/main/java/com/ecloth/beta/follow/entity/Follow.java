package com.ecloth.beta.follow.entity;

import com.ecloth.beta.common.entity.BaseEntity;
import com.ecloth.beta.member.entity.Member;
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

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Member target;

    public void changeRequester(Member requester){
        this.requester = requester;
        if (!requester.getFollowList().contains(this)) {
            requester.getFollowList().add(this);
        }
    }

    public void changeTarget(Member target){
        this.target = target;
        if (!target.getFollowerList().contains(this)){
            target.getFollowerList().add(this);
        }
    }

}
