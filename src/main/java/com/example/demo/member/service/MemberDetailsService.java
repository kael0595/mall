package com.example.demo.member.service;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.member.entity.Grade;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberOptional = this.memberRepository.findByUsername(username);

        if (memberOptional.isEmpty()) {
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }

        Member member = memberOptional.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Grade.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Grade.BASIC.getValue()));
        }

        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}
