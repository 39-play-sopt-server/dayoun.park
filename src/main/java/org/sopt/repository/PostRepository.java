package org.sopt.repository;

import org.sopt.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("delete from Post post where post.member.id = :memberId")
    void deleteAllWrittenBy(@Param("memberId") Long memberId);
}