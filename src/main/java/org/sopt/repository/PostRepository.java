package org.sopt.repository;

import org.sopt.model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Comparator;

@Repository
public class PostRepository {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(0);

    public int nextId() {
        return sequence.incrementAndGet();
    }

    public void save(Post post) {
        posts.put(post.getId(), post);
    }

    public List<Post> findAll() {
        List<Post> result = new ArrayList<>(posts.values());
        result.sort(Comparator.comparingInt(Post::getId));

        return result;
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void delete(int id) {
        posts.remove(id);
    }
}