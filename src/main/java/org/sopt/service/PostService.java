package org.sopt.service;

import org.sopt.dto.PostResponse;
import org.springframework.transaction.annotation.Transactional;
import org.sopt.model.Category;
import org.sopt.model.Member;
import org.sopt.model.Post;
import org.sopt.repository.MemberRepository;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(
            PostRepository postRepository,
            MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void writePost(
            String title,
            String content,
            Category category,
            Long memberId) {
        // 1. 작성자 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new NoSuchElementException("존재하지 않는 회원입니다.")
                );

        // 2. Post 객체 생성
        Post post = new Post(
                title, content, category, member
        );

        // 3. DB에 저장
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> readPostList(int page, int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "페이지는 0 이상, 크기는 1~100이어야 합니다."
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt", "id")
        );

        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(post -> new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember().getName(),
                post.getViewCount()
        ));
    }

    @Transactional(readOnly = true)
    public Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new NoSuchElementException("존재하지 않는 게시글입니다.")
                );
    }

    @Transactional
    public PostResponse readPost(Long postId) {
        Post post = findPost(postId);
        post.increaseViewCount();

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember().getName(),
                post.getViewCount()
        );
    }

    @Transactional
    public void editPost(Long postId, String title, String content) {
        Post post = findPost(postId);
        post.update(title, content);
    }

    @Transactional
    public void removePost(Long postId) {
        findPost(postId);
        postRepository.deleteById(postId);
    }
}