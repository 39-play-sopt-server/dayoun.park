package org.sopt.service;

import org.sopt.model.Member;
import org.sopt.repository.MemberRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.RefreshTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberService(
            MemberRepository memberRepository,
            PostRepository postRepository,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void removeMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."
                ));

        refreshTokenRepository.revokeAllForMember(memberId);
        postRepository.deleteAllWrittenBy(memberId);
        memberRepository.delete(member);
    }
}