package com.example.demo.member.entity;

import lombok.Getter;

@Getter
public enum Grade {

    BASIC("ROLE_BASIC"),
    VIP("ROLE_VIP"),
    VVIP("ROLE_VVIP"),
    ADMIN("ROLE_ADMIN");

    private String value;

    Grade(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
