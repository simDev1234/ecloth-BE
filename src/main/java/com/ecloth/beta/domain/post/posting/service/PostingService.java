package com.ecloth.beta.domain.post.posting.service;

import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.member.repository.MemberRepository;
import com.ecloth.beta.domain.post.posting.dto.*;
import com.ecloth.beta.domain.post.posting.entity.Image;
import com.ecloth.beta.domain.post.posting.entity.Posting;
import com.ecloth.beta.domain.post.posting.exception.ErrorCode;
import com.ecloth.beta.domain.post.posting.exception.PostingException;
import com.ecloth.beta.domain.post.posting.repository.ImageRepository;
import com.ecloth.beta.domain.post.posting.repository.PostingRepository;
import com.ecloth.beta.utill.RedisClient;
import com.ecloth.beta.utill.S3FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final RedisClient redisClient;
    private final S3FileUploader s3FileUploader;

    public void createPost(PostingCreateRequest request) throws Exception {

        // 회원 확인
        Member writer = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        // 포스트 생성
        Posting newPosting = postingRepository.save(request.toPosting(writer));

        // 이미지 생성
        List<Image> imageList = createImageAfterSavingToS3(request.getImages(), newPosting);

        newPosting.changeImageList(imageList);
        postingRepository.save(newPosting);
    }


    public MemberPostingListResponse getMemberPostList(Long memberId, MemberPostingListRequest request) {

        // 회원 확인
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        // 포스트 목록 조회
        Page<Posting> postingPage = postingRepository.findByWriter(writer, request.toCustomPage().toPageable());

        return MemberPostingListResponse.fromEntity(postingPage);
    }

    public List<Image> createImageAfterSavingToS3(MultipartFile[] images, Posting newPosting) throws Exception {

        List<Image> imageList = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            String imageUrlPath = s3FileUploader.uploadImageToS3AndGetURL(images[i]);
            if (StringUtils.hasText(imageUrlPath)) {
                Image newImage = imageRepository.save(
                        Image.builder().posting(newPosting).url(imageUrlPath).isRepresentImage(i == 0).build()
                );
                imageList.add(newImage);
            }
        }
        return imageList;

    }

    public PostingDetailResponse getPostDetail(Long postingId) {

        // 포스트 조회
        Posting posting = postingRepository.findByPostingIdFetchJoinedWithMemberAndImage(postingId)
                .orElseThrow(() -> new PostingException(ErrorCode.POSTING_NOT_FOUND));

        // 조회수 + 1
        posting.increaseViewCount();
        postingRepository.save(posting);

        // 포스트 상세 정보 전달
        return PostingDetailResponse.fromEntity(posting);
    }

    public PostingListResponse getPostListByPage(PostingListRequest request) {

        // 포스트 목록 조회
        Page<Posting> postingPage = postingRepository.findPostingByPaging(request.toPageable());

        return PostingListResponse.fromEntity(postingPage);
    }

    public void updatePost(Long postingId, PostingUpdateRequest request) throws Exception {

        // 포스트 조회
        Posting posting = postingRepository.findByPostingIdFetchJoinedWithMember(postingId)
                .orElseThrow(() -> new PostingException(ErrorCode.POSTING_NOT_FOUND));

        // 포스트 제목, 본문 수정
        posting.changeTitle(request.getTitle());
        posting.changeContent(request.getContent());

        // 교체할 이미지
        List<Image> imageList = createImageAfterSavingToS3(request.getImages(), posting);
        posting.changeImageList(imageList);

    }

    public void checkOrUnCheckLike(Long postingId, Long memberId) {

        // 포스트 조회
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingException(ErrorCode.POSTING_NOT_FOUND));

        // 레디스에서 좋아요 기록 조회
        String redisLikeKey = redisLikeKey(postingId, memberId);
        Optional<Boolean> optionalLikeHistory = redisClient.get(redisLikeKey, Boolean.class);

        // 레디스에 있으면 like count -1, 없으면 + 1
        if (optionalLikeHistory.isPresent()) {
            redisClient.delete(redisLikeKey);
            posting.decreaseLikeCount();
        } else {
            redisClient.put(redisLikeKey, true);
            posting.increaseLikeCount();
        }
    }

    private String redisLikeKey(Long postingId, Long memberId) {
        return String.format("like:%d-%d", postingId,memberId);
    }

    // 게시글 삭제
    public void deletePost(Long postingId, Long memberId) throws Exception {

        // 게시글 조회
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingException(ErrorCode.POSTING_NOT_FOUND));

        if (!posting.getWriter().getMemberId().equals(memberId)) {
            throw new PostingException(ErrorCode.POSTING_WRITER_NOT_MATCHING);
        }

        // 해당 게시글에 연관된 Image 엔티티 삭제
        for (Image image : posting.getImageList()) {
            // 이미지 엔티티 삭제
            imageRepository.delete(image);
        }

        // 게시글 삭제
        postingRepository.delete(posting);
    }
}


