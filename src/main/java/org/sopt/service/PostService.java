package org.sopt.service;

import org.sopt.model.Category;
import org.sopt.model.Post;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void writePost(String title, String content, Category category) {
        int id = postRepository.nextId();

        Post post = new Post(id, title, content, category);
        postRepository.save(post);
    }

    public List<Post> readPostList() {
        return postRepository.findAll();
    }

    public Post findPost(int postId){
        Post post = postRepository.findById(postId);

        if (post == null) {
            throw new NoSuchElementException("존재하지 않는 게시글입니다.");
        }

        return post;
    }

    public Post readPost(int postId) {
        Post post = postRepository.findById(postId);
        post.increaseViewCount();

        return post;
    }

    public void editPost(int index, String title, String content) {
        Post post = findPost(index);
        post.update(title, content);
    }

    public void removePost(int index) {
        findPost(index);
        postRepository.delete(index);
    }
}