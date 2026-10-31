package org.sopt.service;

import org.sopt.model.Member;
import org.sopt.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member createMember(String name) {
        Member member = new Member(name);
        return memberRepository.save(member);
    }

    @Transactional
    public void removeMember(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new NoSuchElementException("존재하지 않는 회원입니다.")
                );
        memberRepository.deleteById(memberId);
    }
}