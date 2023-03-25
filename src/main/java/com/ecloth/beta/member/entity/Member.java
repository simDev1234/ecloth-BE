package com.ecloth.beta.member.entity;

import com.ecloth.beta.common.entity.BaseEntity;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.member.dto.InfoMeUpdateRequest;
import com.ecloth.beta.member.exception.ErrorCode;
import com.ecloth.beta.member.exception.MemberException;
import com.ecloth.beta.member.model.MemberRole;
import com.ecloth.beta.member.model.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "member_id")
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

    // follow & follower
    @OneToMany(mappedBy = "requester")
    private List<Follow> followList;
    @OneToMany(mappedBy = "target")
    private List<Follow> followerList;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;


    public void update(InfoMeUpdateRequest request, PasswordEncoder passwordEncoder) {
        if (request.getNickname() != null) {
            this.nickname = request.getNickname();
        }
        if (request.getPhone() != null) {
            this.phone = request.getPhone();
        }
        if (request.getProfileImagePath() != null) {
            this.profileImagePath = request.getProfileImagePath();
        }
        if (request.getNewPassword() != null && request.getPassword() != null) {
            if (!passwordEncoder.matches(request.getPassword(), this.password)) {
                throw new MemberException(ErrorCode.WRONG_PASSWORD);
            }
            this.password = passwordEncoder.encode(request.getNewPassword());
        }
    }
}