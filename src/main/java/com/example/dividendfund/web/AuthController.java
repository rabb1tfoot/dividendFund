package com.example.dividendfund.web;

import com.example.dividendfund.model.Auth;
import com.example.dividendfund.security.TokenProvider;
import com.example.dividendfund.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request){
        return ResponseEntity.ok(memberService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request){
        var member = memberService.authenticate(request);
        log.info("user login -> " + request.getUsername());
        return ResponseEntity.ok(tokenProvider.generateToken(member.getUsername(), member.getRoles()));
    }

}
