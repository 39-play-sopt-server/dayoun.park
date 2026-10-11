package org.sopt.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Post {

    private String title;
    private String content;
    private final int id;
    private final Category category;
    private final LocalDateTime createdAt;
    private final AtomicInteger viewCount = new AtomicInteger(0);

    public Post(int id, String title, String content, Category category) {
        validate(title, content);

        if (category == null) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }

        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    private void validate(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("본문은 비어 있을 수 없습니다.");
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void update(String title, String content) {
        validate(title, content);

        this.title = title;
        this.content = content;
    }

    public int getViewCount() {
        return viewCount.get();
    }

    public void increaseViewCount() {
        viewCount.incrementAndGet();
    }
}