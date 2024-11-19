package com.example.demo.member.controller;

import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

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

        System.out.println(memberDto.toString());

        Member member = this.memberService.join(
                memberDto.getUsername(),
                memberDto.getName(),
                memberDto.getPassword1(),
                memberDto.getEmail(),
                memberDto.getPhone(),
                memberDto.getAddr1(),
                memberDto.getAddr2()
        );

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(MemberDto memberDto) {
        return "member/login";
    }

    @PostMapping("/login")
    public String memberLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpSession session,
                              BindingResult bindingResult) {

        Member member = memberService.getMember(username);

        if (member == null) {
            bindingResult.rejectValue("LoginFail", "존재하지 않는 계정입니다.");
            return "redirect:/member/login";
        }

        if (!member.getPassword().equals(password)) {
            bindingResult.rejectValue("LoginFail", "비밀번호가 일치하지 않습니다.");
            return "member/login";
        }

        session.setAttribute("member", member);
        return "redirect:/";
    }


}
