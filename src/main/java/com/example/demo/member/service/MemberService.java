package com.example.demo.member.service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.member.entity.Grade;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member join(String username, String name, String password1, String email, String phone, String addr1, String addr2) {

        System.out.println(phone);

        Member member = new Member();
        member.setUsername(username);
        member.setName(name);
        member.setPassword(passwordEncoder.encode(password1));
        member.setEmail(email);
        member.setPhone(phone);
        member.setAddr1(addr1);
        member.setAddr2(addr2);

        if (username.startsWith("admin")) {
            member.setGrade(Grade.ADMIN);
        }

        member.setGrade(Grade.BASIC);
        return memberRepository.save(member);
    }

    public Member getMember(String username) {
        Optional<Member> memberOptional = memberRepository.findByUsername(username);
        if (memberOptional.isPresent()) {
            return memberOptional.get();
        } else {
            throw new DataNotFoundException("member not found");
        }
    }
}
