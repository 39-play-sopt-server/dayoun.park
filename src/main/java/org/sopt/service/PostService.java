package org.sopt.service;

import org.sopt.model.Category;
import org.sopt.model.Post;
import org.sopt.repository.PostRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void writePost(String title, String content, Category category) {
        Post post = new Post(title, content, category);
        postRepository.save(post);
    }

    public List<Post> readPostList() {
        return postRepository.findAll();
    }

    public Post readPost(int index) {
        Post post = postRepository.findByIndex(index);

        if (post == null) {
            throw new NoSuchElementException("존재하지 않는 게시글입니다.");
        }

        return post;
    }

    public void editPost(int index, String title, String content) {
        Post post = readPost(index);
        post.update(title, content);
    }

    public void removePost(int index) {
        readPost(index);
        postRepository.delete(index);
    }
}