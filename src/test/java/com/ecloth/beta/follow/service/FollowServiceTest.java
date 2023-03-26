//package com.ecloth.beta.follow.service;
//
//import com.ecloth.beta.follow.dto.FollowList;
//import com.ecloth.beta.follow.dto.FollowList.Response.FollowMember;
//import com.ecloth.beta.follow.dto.Following;
//import com.ecloth.beta.follow.entity.Follow;
//import com.ecloth.beta.follow.entity.Member;
//import com.ecloth.beta.follow.exception.FollowException;
//import com.ecloth.beta.follow.repository.FollowRepository;
//import com.ecloth.beta.follow.repository.MemberRepository;
//import com.ecloth.beta.follow.type.PointDirection;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Optional;
//
//import static com.ecloth.beta.follow.exception.ErrorCode.FOLLOW_TARGET_NOT_FOUND;
//import static com.ecloth.beta.follow.type.PointDirection.FOLLOWERS;
//import static com.ecloth.beta.follow.type.PointDirection.FOLLOWS;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class FollowServiceTest {
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @Mock
//    private FollowRepository followRepository;
//
//    @InjectMocks
//    private FollowService followService;
//
//    @Test
//    @DisplayName("팔로우 또는 언팔로우 하려는 대상 Id가 회원 테이블에 존재하지 않는 경우")
//    void followOrUnFollowTarget_fail() {
//
//        // given
//        Member requester = sampleRequesterMember();
//        Member target = sampleTargetMember();
//
//        Following.Request request = Following.Request.builder()
//                .targetId(target.getId())
//                .followStatus(true)
//                .build();
//
//        given(memberRepository.findByEmail(anyString()))
//                .willReturn(Optional.ofNullable(requester));
//
//        given(memberRepository.findById(anyLong()))
//                .willReturn(Optional.empty());
//
//        // when & then
//        FollowException exception = Assertions.assertThrows(FollowException.class,
//                () -> followService.followOrUnFollowTarget(requester.getEmail(), request));
//        Assertions.assertEquals(exception.getErrorCode(), FOLLOW_TARGET_NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("팔로우 또는 언팔로우 성공")
//    void followOrUnFollowTarget_success() {
//
//        // given
//        Member requester = sampleRequesterMember();
//        Member target = sampleTargetMember();
//
//        Following.Request request = Following.Request.builder()
//                .targetId(target.getId())
//                .followStatus(true)
//                .build();
//
//        given(memberRepository.findByEmail(anyString()))
//                .willReturn(Optional.ofNullable(requester));
//
//        given(memberRepository.findById(anyLong()))
//                .willReturn(Optional.ofNullable(target));
//
//        given(followRepository.save(any()))
//                .willReturn(sampleFollow(requester.getId(), request));
//
//        // when
//        Following.Response response = followService.followOrUnFollowTarget(requester.getEmail(), request);
//
//        // then
//        Assertions.assertEquals(response.isFollowStatus(), true);
//
//    }
//
//    @Test
//    @DisplayName("팔로우 상태 확인 대상 Id가 회원 테이블에 존재하지 않는 경우")
//    void getFollowStatus_fail() {
//
//        // given
//        Member requester = sampleRequesterMember();
//        Member target = sampleTargetMember();
//
//        given(memberRepository.findByEmail(anyString()))
//                .willReturn(Optional.ofNullable(requester));
//
//        given(memberRepository.findById(anyLong()))
//                .willReturn(Optional.empty());
//
//        // when & then
//        FollowException exception = Assertions.assertThrows(FollowException.class,
//                () -> followService.getFollowStatus(requester.getEmail(), target.getId()));
//        Assertions.assertEquals(exception.getErrorCode(), FOLLOW_TARGET_NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("팔로우 상태 확인 성공 - 팔로우 테이블에 정보가 없는 경우")
//    void getFollowStatus_success_followNotExist() {
//
//        // given
//        Member requester = sampleRequesterMember();
//        Member target = sampleTargetMember();
//
//        given(memberRepository.findByEmail(anyString()))
//                .willReturn(Optional.ofNullable(requester));
//
//        given(memberRepository.findById(anyLong()))
//                .willReturn(Optional.ofNullable(target));
//
//        given(followRepository.findByRequesterIdAndTargetId(anyLong(), anyLong()))
//                .willReturn(Optional.empty());
//
//        // when
//        Following.Response response = followService.getFollowStatus(requester.getEmail(), target.getId());
//
//        // then
//        Assertions.assertEquals(response.getTargetId(), target.getId());
//        Assertions.assertEquals(false, response.isFollowStatus());
//
//    }
//
//    @Test
//    @DisplayName("팔로우 상태 확인 성공 - 팔로우 테이블에 정보가 있는 경우")
//    void getFollowStatus_success_followExist() {
//
//        // given
//        Member requester = sampleRequesterMember();
//        Member target = sampleTargetMember();
//        Follow follow = sampleFollow(requester.getId(), target.getId(), true);
//
//        given(memberRepository.findByEmail(anyString()))
//                .willReturn(Optional.ofNullable(requester));
//
//        given(memberRepository.findById(anyLong()))
//                .willReturn(Optional.ofNullable(target));
//
//        given(followRepository.findByRequesterIdAndTargetId(anyLong(), anyLong()))
//                .willReturn(Optional.ofNullable(follow));
//
//        // when
//        Following.Response response = followService.getFollowStatus(requester.getEmail(), target.getId());
//
//        // then
//        Assertions.assertEquals(response.getTargetId(), target.getId());
//        Assertions.assertEquals(true, response.isFollowStatus());
//
//    }
//
//    @Test
//    @DisplayName("나의 팔로우 목록에 저장된 회원이 존재하지 않는 회원인 경우")
//    void getFollowList_whomIFollow_fail() {
//
//        // given
//        Member requester = sampleRequesterMember();
//        FollowList.Request request = sampleGetFollowListRequest(requester.getId(), FOLLOWS);
//        PageRequest pageRequest = PageRequest.of(request.getPageNumber() - 1, request.getRecordSize()
//                , Sort.Direction.valueOf(request.getSortOrder().toUpperCase(Locale.ROOT)), request.getSortBy());
//
//        given(memberRepository.findByEmail(anyString()))
//                .willReturn(Optional.ofNullable(requester));
//
//        given(followRepository.findAll(pageRequest))
//                .willReturn(sampleFollows(requester.getId(), pageRequest, 5));
//
//        given(memberRepository.findById(anyLong()))
//                .willReturn(Optional.empty());
//
//        // when & then
//        FollowException exception = Assertions.assertThrows(FollowException.class,
//                () -> followService.getFollowList(requester.getEmail(), request));
//        Assertions.assertEquals(exception.getErrorCode(), FOLLOW_TARGET_NOT_FOUND);
//
//    }
//
//    @Test
//    @DisplayName("내가 팔로우한 사람이 아무도 없는 경우")
//    void getFollowList_whomIFollow_success() {
//
//        // given
//        Member requester = sampleRequesterMember();
//        FollowList.Request request = sampleGetFollowListRequest(requester.getId(), FOLLOWS);
//        PageRequest pageRequest = PageRequest.of(request.getPageNumber() - 1, request.getRecordSize()
//                , Sort.Direction.valueOf(request.getSortOrder().toUpperCase(Locale.ROOT)), request.getSortBy());
//
//        given(memberRepository.findByEmail(anyString()))
//                .willReturn(Optional.ofNullable(requester));
//
//        given(followRepository.findAll(pageRequest))
//                .willReturn(sampleFollows(requester.getId(), pageRequest, 0));
//
//        // when
//        FollowList.Response response = followService.getFollowList(requester.getEmail(), request);
//
//        // then
//        List<FollowMember> followMembers = response.getFollowList();
//        Assertions.assertEquals(followMembers.size(), 0);
//        Assertions.assertEquals(response.getPointDirection(), FOLLOWS.name());
//
//    }
//
//    @Test
//    @DisplayName("나를 팔로우한 사람 목록 조회")
//    void getFollowList_WhoFollowsMe_success() {
//
//        // given
//        Member requester = sampleRequesterMember();
//        Member target = sampleTargetMember();
//        FollowList.Request request = sampleGetFollowListRequest(requester.getId(), FOLLOWERS);
//        PageRequest pageRequest = PageRequest.of(request.getPageNumber() - 1, request.getRecordSize()
//                , Sort.Direction.valueOf(request.getSortOrder().toUpperCase(Locale.ROOT)), request.getSortBy());
//
//        given(memberRepository.findByEmail(anyString()))
//                .willReturn(Optional.ofNullable(requester));
//
//        given(followRepository.findAllByTargetId(requester.getId(), pageRequest))
//                .willReturn(sampleFollowers(requester.getId(), pageRequest, 5));
//
//        given(memberRepository.findById(anyLong()))
//                .willReturn(Optional.ofNullable(target));
//
//        // when
//        FollowList.Response response = followService.getFollowList(requester.getEmail(), request);
//
//        // then
//        List<FollowMember> followMembers = response.getFollowList();
//        Assertions.assertEquals(followMembers.size(), 5);
//        Assertions.assertEquals(response.getPointDirection(), FOLLOWERS.name());
//        Assertions.assertEquals(response.getRequesterId(), requester.getId());
//
//    }
//
//    Member sampleRequesterMember(){
//        return Member.builder()
//                .id(1L)
//                .email("requester@gmail.com")
//                .build();
//    }
//
//    Member sampleTargetMember(){
//        return Member.builder()
//                .id(2L)
//                .email("target@gmail.com")
//                .build();
//    }
//
//    Follow sampleFollow(Long requesterId, Following.Request request) {
//        return Follow.builder()
//                .requesterId(requesterId)
//                .targetId(request.getTargetId())
//                .followStatus(request.isFollowStatus())
//                .build();
//    }
//
//    Follow sampleFollow(Long requesterId, Long targetId, boolean followStatus) {
//        return Follow.builder()
//                .requesterId(requesterId)
//                .targetId(targetId)
//                .followStatus(followStatus)
//                .build();
//    }
//
//    FollowList.Request sampleGetFollowListRequest(Long requesterId, PointDirection dir) {
//        return FollowList.Request.builder()
//                .pointDirection(dir.name())
//                .pageNumber(1)
//                .recordSize(5)
//                .sortBy("createdDate")
//                .sortOrder("desc")
//                .build();
//    }
//
//    Page<Follow> sampleFollows(Long requesterId, PageRequest page, int size){
//        List<Follow> follows = new ArrayList<>();
//
//        for (int i = 1; i <= size; i++) {
//            follows.add(sampleFollow(requesterId, i * 1L, true));
//        }
//
//        return new PageImpl<>(follows, page, follows.size());
//    }
//
//    Page<Follow> sampleFollowers(Long requesterId, PageRequest page, int size){
//        List<Follow> follows = new ArrayList<>();
//
//        for (int i = 1; i <= size; i++) {
//            follows.add(sampleFollow(i * 1L, requesterId, true));
//        }
//
//        return new PageImpl<>(follows, page, follows.size());
//    }
//
//}