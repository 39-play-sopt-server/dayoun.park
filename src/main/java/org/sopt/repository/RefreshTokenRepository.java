package org.sopt.repository;

import org.sopt.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    boolean existsByIdAndMember_Id(String id, Long memberId);
}