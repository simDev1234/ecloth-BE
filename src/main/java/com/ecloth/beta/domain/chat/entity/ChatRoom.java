package com.ecloth.beta.domain.chat.entity;

import com.ecloth.beta.common.entity.BaseEntity;
import com.ecloth.beta.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id", nullable = false)
    private Long chatRoomId;

    @ManyToMany
    @JoinTable(
            name = "chatroom_members",
            joinColumns = {@JoinColumn(name = "chat_room_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id")}
    )
    private Set<Member> members = new HashSet<>();

}
