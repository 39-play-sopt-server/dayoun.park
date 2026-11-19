package org.sopt.repository;

import org.sopt.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    boolean existsByIdAndMember_Id(String id, Long memberId);

    @Modifying
    @Query("delete from RefreshToken token where token.id = :id and token.member.id = :memberId")
    void revoke(@Param("id") String id, @Param("memberId") Long memberId);

    @Modifying
    @Query("delete from RefreshToken token where token.member.id = :memberId")
    void revokeAllForMember(@Param("memberId") Long memberId);
}