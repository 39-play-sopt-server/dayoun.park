package org.sopt.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    protected Member() {}

    public Member(String name){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("회원 이름은 비어 있을 수 없습니다.");
        }

        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
