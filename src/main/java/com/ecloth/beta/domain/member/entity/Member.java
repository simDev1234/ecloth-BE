package com.ecloth.beta.domain.member.entity;

import com.ecloth.beta.domain.chat.entity.ChatRoom;
import com.ecloth.beta.common.entity.BaseEntity;
import com.ecloth.beta.domain.follow.entity.Follow;
import com.ecloth.beta.domain.member.dto.MemberPasswordUpdateRequest;
import com.ecloth.beta.domain.member.dto.MemberUpdateInfoRequest;
import com.ecloth.beta.domain.member.exception.MemberErrorCode;
import com.ecloth.beta.domain.member.exception.MemberException;
import com.ecloth.beta.domain.member.model.MemberRole;
import com.ecloth.beta.domain.member.model.MemberStatus;
import com.ecloth.beta.utill.S3FileUploader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
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

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    // email verify
    private String emailAuthCode;
    private LocalDateTime emailAuthDate;

    // password reset
    private String passwordResetCode;
    private LocalDateTime passwordResetRequestDate;

    // location
    private int x;
    private int y;

    // follow & follower
    @JsonIgnore
    @OneToMany(mappedBy = "requester")
    private List<Follow> followList = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "target")
    private List<Follow> followerList = new ArrayList<>();

    // chat room
    @JsonIgnore
    @ManyToMany(mappedBy = "members")
    private Set<ChatRoom> chatRooms = new HashSet<>();

    public void update(MemberUpdateInfoRequest request, PasswordEncoder passwordEncoder, S3FileUploader s3FileUploader) {
        if (request.getNickname() != null) {
            this.nickname = request.getNickname();
        }
        if (request.getPhone() != null) {
            this.phone = request.getPhone();
        }
        if (request.getProfileImage() != null) {
            MultipartFile profileImage = request.getProfileImage();
            this.profileImagePath = s3FileUploader.uploadImageToS3AndGetURL(profileImage);
        }
        if (request.getNewPassword() != null && request.getPassword() != null) {
            if (!passwordEncoder.matches(request.getPassword(), this.password)) {
                throw new MemberException(MemberErrorCode.WRONG_PASSWORD);
            }
            this.password = passwordEncoder.encode(request.getNewPassword());
        }
    }

    public void updateMemberStatusToActive(LocalDateTime emailAuthDate){
        this.memberStatus = MemberStatus.ACTIVE;
        this.emailAuthDate = emailAuthDate;
    }

    public void updateMemberStatusToInactive() {
        this.memberStatus = MemberStatus.INACTIVE;
    }

    public void setPasswordResetCodeAndRequestDate(String code, LocalDateTime requestDate) {
        this.passwordResetCode = code;
        this.passwordResetRequestDate = requestDate;
    }

    public void updateNewPassword(MemberPasswordUpdateRequest request, PasswordEncoder passwordEncoder) {
        this.passwordResetCode = null;
        this.password = passwordEncoder.encode(request.getNewPassword());
    }

    public void updateLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}