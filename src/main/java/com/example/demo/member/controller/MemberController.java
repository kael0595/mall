package com.example.demo.member.controller;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;

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
    public String login() {
        return "member/login";
    }

    @PostMapping("/login")
    public String memberLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {

        Member member = memberService.getMember(username);

        if (member == null) {
            model.addAttribute("loginError", "존재하지 않는 회원입니다.");
            return "redirect:/member/login";
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            model.addAttribute("loginError", "비밀번호가 일치하지 않습니다.");
            return "redirect:/member/login";
        }

        session.setAttribute("member", member);
        return "redirect:/member/join";
    }

    @GetMapping("/me/{id}")
    public String me(@PathVariable("id") String id, Model model) {
        Member member = memberService.getMember(id);

        if (member == null) {
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }

        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(member.getUsername());
        memberDto.setName(member.getName());
        memberDto.setEmail(member.getEmail());
        memberDto.setPhone(member.getPhone());
        memberDto.setAddr1(member.getAddr1());
        memberDto.setAddr2(member.getAddr2());

        model.addAttribute("member", member);
        model.addAttribute("memberDto", memberDto);

        return "member/me";
    }

    @PostMapping("/me/{id}/modify")
    public String modify(@PathVariable("id") String id,
                         @Valid MemberDto memberDto,
                         BindingResult bindingResult,
                         Model model) {

        Member member = this.memberService.getMember(id);

        if (member == null) {
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("member", member);
            return "member/me";
        }


        Member modifyMember = this.memberService.modify(
                member.getUsername(),
                memberDto.getPassword1(),
                memberDto.getName(),
                memberDto.getEmail(),
                memberDto.getPhone(),
                memberDto.getAddr1(),
                memberDto.getAddr2()
        );

        return "redirect:/";

    }

}
