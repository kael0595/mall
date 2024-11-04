package com.example.demo.member.service;

import com.example.demo.member.entity.Grade;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(String username, String name, String password1, String email, String addr1, String addr2) {

        Member member = new Member();
        member.setUsername(username);
        member.setName(name);
        member.setPassword(password1);
        member.setEmail(email);
        member.setAddr1(addr1);
        member.setAddr2(addr2);

        if (username.startsWith("admin")) {
            member.setGrade(Grade.ADMIN);
        }

        member.setGrade(Grade.BASIC);
        return memberRepository.save(member);
    }
}
