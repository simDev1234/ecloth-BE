package com.ecloth.beta.post.service;

import com.ecloth.beta.post.dto.PostRequest;
import com.ecloth.beta.post.entity.Posting;
import com.ecloth.beta.post.repository.PostingRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;

    public List<Posting> getAllPosts() {
        return postingRepository.findAll();
    }

    public Posting getPostById(Long id) {
        return postingRepository.findById(id).orElse(new Posting());
    }

    public Posting createPost(PostRequest postRequest) {
        Posting posting = Posting.from(postRequest);
        Posting saved = postingRepository.save(posting);
        return (Posting) postingRepository;
    }

    public Posting updatePost(Long id , Posting posting) {
        return (Posting) postingRepository.findAll((Pageable) posting);
    }


    public boolean deletePost(Long id) {
        Posting posting = postingRepository.findById(id).orElse(new Posting());
        if (posting == null) {
            return false;
        }
        postingRepository.delete(posting);
        return true;
    }

}
