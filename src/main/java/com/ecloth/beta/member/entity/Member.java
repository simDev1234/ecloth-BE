package com.ecloth.beta.member.entity;

import com.ecloth.beta.member.model.BaseEntity;
import com.ecloth.beta.member.model.MemberRole;
import com.ecloth.beta.member.model.MemberStatus;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity(name = "MEMBER")
@Getter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity implements UserDetails {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true)
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, message = "최소 8 자, 하나 이상의 문자, 하나의 숫자 및 하나의 특수 문자로 입력해주세요.")
    private String password;

    @Column(unique = true)
    @NotEmpty(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
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

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(memberRole)
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
