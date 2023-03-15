package com.ll.basic1.boundedContext.member.controller;

import com.ll.basic1.base.rsData.RsData;
import com.ll.basic1.boundedContext.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/login")
    @ResponseBody
    public RsData login(String username, String password, HttpServletResponse resp) {
        if ( username == null || username.trim().length() == 0 ) {
            return RsData.of("F-3", "username(을)를 입력해주세요.");
        }

        if ( password == null || password.trim().length() == 0 ) {
            return RsData.of("F-4", "password(을)를 입력해주세요.");
        }

        resp.addCookie(new Cookie("username", username));

        return memberService.tryLogin(username, password);
    }

    @GetMapping("/member/me")
    @ResponseBody
    public RsData userInfo(HttpServletRequest req) {
        if (req.getCookies() == null) {
            return RsData.of("F-1", "로그인 후 이용해주세요.");
        }

        String username = Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals("username"))
                .map(c -> c.getValue())
                .findFirst()
                .orElse("null");

        //resp.addCookie(new Cookie("username", username));

        return RsData.of("S-1","당신의 username(은)는 %s 입니다."
                .formatted(username));
    }
}