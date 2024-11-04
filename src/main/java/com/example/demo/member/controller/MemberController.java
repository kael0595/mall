package com.example.demo.member.controller;

import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.demo.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join(MemberDto memberDto) {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberDto memberDto,
                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        if (!memberDto.getPassword1().equals(memberDto.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "member/join";
        }

        Member member = this.memberService.join(memberDto.getUsername(),
                memberDto.getName(),
                memberDto.getPassword1(),
                memberDto.getEmail(),
                memberDto.getAddr1(),
                memberDto.getAddr2());

        return "redirect:/";
    }


}
