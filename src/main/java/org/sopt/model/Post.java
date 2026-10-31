package org.sopt.model;

import jakarta.persistence.*;

@Entity
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Category category;

    @Column(nullable = false)
    private int viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // JPA가 DB에서 읽은 데이터로 객체를 만들 때 사용하는 기본 생성자
    protected Post() {
    }

    // DB가 ID를 생성하는 방식으로 전환한 뒤 사용할 생성자
    public Post(String title, String content, Category category, Member member) {
        validate(title, content, category, member);

        this.title = title;
        this.content = content;
        this.category = category;
        this.member = member;
    }

    private void validate(String title, String content, Category category, Member member) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("본문은 비어 있을 수 없습니다.");
        }

        if (category == null) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }

        if (member == null) {
            throw new IllegalArgumentException("작성자는 필수입니다.");
        }
    }

    public Long getId() {
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

    public Member getMember() { return member;}

    public void update(String title, String content) {
        validate(title, content, this.category, this.member);

        this.title = title;
        this.content = content;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void increaseViewCount() {
        viewCount++;
    }
}