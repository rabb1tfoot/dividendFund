package com.example.dividendfund.service;

import com.example.dividendfund.exception.impl.AlreadyExistUserException;
import com.example.dividendfund.model.Auth;
import com.example.dividendfund.model.MemberEntity;
import com.example.dividendfund.persist.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user ->" + username));
    }

    public MemberEntity register(Auth.SignUp member){
        boolean exists = memberRepository.existsByUsername(member.getUsername());
        if(exists){
            throw new AlreadyExistUserException();
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        MemberEntity memberEntity = memberRepository.save(member.toEntity());
        System.out.println(memberEntity.getUsername() + memberEntity.getPassword());
         return memberEntity;
    }
    public MemberEntity authenticate(Auth.SignIn member){
        MemberEntity user = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지않는 ID입니다."));

        if(!passwordEncoder.matches(member.getPassword(), user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

}
