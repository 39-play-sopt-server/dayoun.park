package org.sopt.repository;

import org.sopt.model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {

    private final List<Post> posts = new ArrayList<>();

    public void save(Post post) {
        posts.add(post);
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts);
    }

    public Post findByIndex(int index) {
        if (index < 0 || index >= posts.size()) {
            return null;
        }

        return posts.get(index);
    }

    public void delete(int index) {
        posts.remove(index);
    }
}