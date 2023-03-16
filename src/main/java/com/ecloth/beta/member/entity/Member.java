package com.ecloth.beta.member.entity;

import com.ecloth.beta.common.entity.BaseEntity;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.member.model.MemberRole;
import com.ecloth.beta.member.model.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
//@ToStringtgsgerherhr
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    //@Column(unique = true)
    //@NotEmpty(message = "이메일은 필수 입력 값입니다.")
    //@Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    //@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    //@Length(min = 8, message = "최소 8 자, 하나 이상의 문자, 하나의 숫자 및 하나의 특수 문자로 입력해주세요.")
    private String password;

    //@Column(unique = true)
    //@NotEmpty(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    //@NotEmpty(message = "전화번호는 필수 입력 값입니다.")
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

    // follow & follower cnt
    @OneToMany(mappedBy = "requester")
    private List<Follow> followList;
    @OneToMany(mappedBy = "target")
    private List<Follow> followerList;
//    private long followCnt;
//    private long followerCnt;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

//    public void changeFollowCnt(boolean isIncreasing) {
//        if (isIncreasing) this.followCnt += 1;
//        else this.followCnt -= 1;
//    }
//
//    public void changeFollowerCnt(boolean isIncreasing) {
//        if (isIncreasing) this.followerCnt += 1;
//        else this.followerCnt -= 1;
//    }

}