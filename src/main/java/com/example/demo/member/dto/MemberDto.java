package com.example.demo.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {

    private String username;

    private String name;

    private String password1;

    private String password2;

    private String email;

    private String phone;

    private String addr1;

    private String addr2;
}
