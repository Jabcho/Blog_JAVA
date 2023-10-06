package com.example.blog.config;

import com.example.blog.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.stream.Stream;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailService userService;
    private static final String[] PERMIT_URLS = new String[] {"/login", "/signup", "/user"};

    // 스프링 시큐리티 기능 비활성화
    // 모든 곳에 인증, 인가 서비스를 적용하지 않음
    // 정적 리소스(static 하위 경로 파일)와 h2-console 하위 url 대상으로 ignoring
    @Bean
    public WebSecurityCustomizer configure(){
        System.out.println("너가 문제니");
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(
                        Stream
                                .of("/static/**")
                                .map(AntPathRequestMatcher::antMatcher)
                                .toArray(AntPathRequestMatcher[]::new)
                );
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("아니면 너가 문제니");
        return http.authorizeRequests() // 특정 경로에 대한 엑세스 설정
                .requestMatchers(Stream
                        .of(PERMIT_URLS)
                        .map(AntPathRequestMatcher::antMatcher)
                        .toArray(AntPathRequestMatcher[]::new)).permitAll() // 특정 요청과 일치하는 url에 대한 엑세스 설정 --> /login, signup, user로 요청이 오면 누구나 접근 가능
                .anyRequest().authenticated() // 그 밖에 경우, 인가는 피료하지 않지만 인증이 성공한 상태여야 접근 가능
                .and() // login, signup, user외 url로 접근했을 때 인증이 되어있지 않은 경우
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/articles")
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true) // 로그아웃 후에 세션을 전체 삭제할지 여부
                .and()
                .csrf().disable() // 개발 편의를 위해 우선 CSRF 설정 비활성화. 마지막에 활성화로 변경
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
