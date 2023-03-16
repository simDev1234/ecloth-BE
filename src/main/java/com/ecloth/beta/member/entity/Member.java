package com.ecloth.beta.member.entity;

import com.ecloth.beta.member.model.BaseEntity;
import com.ecloth.beta.member.model.MemberRole;
import com.ecloth.beta.member.model.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String phone;

    private String profileImagePath;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    // email verify
    private String emailAuthCode;
    private LocalDateTime emailAuthDate;

    // password reset
    private String passwordResetCode;
    private LocalDateTime passwordResetRequestDate;

    // location
    private String latitude;
    private String longitude;

    // 팔로우,팔로워
    private long follow_cnt;
    private long follower_cnt;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
}