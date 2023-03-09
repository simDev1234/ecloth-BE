package com.ecloth.beta.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Bean // 로그인 시 실행되는 메소드
//    public AuthenticationProvider authenticationProvider() {
//        return new LoginAuthenticationProvider();
//    }
//
//    @Bean // 로그인 성공 시 실행되는 메소드
//    public AuthenticationSuccessHandler successHandlerHandler() {
//        return new LoginSuccessHandler();
//    }
//
//    @Bean // 로그인 실패 시 실행되는 메소드
//    public AuthenticationFailureHandler failureHandlerHandler() {
//        return new LoginFailureHandler();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] PERMIT_URL_ARRAY = {
            "/","/api/register/**","/api/email-auth/**","/api","/js/**","/css/**","/image/**","/dummy/**",
            "/favicon.ico","/**/favicon.ico",
            //h2
            "/h2-console/**",
            //swagger
            "/swagger-ui.html","/swagger/**","/swagger-resources/**","/v2/api-docs","/webjars/**"
    };


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();;
        http.csrf().disable()// 세션을 사용하지 않고 JWT 토큰을 활용하여 진행, csrf토큰검사를 비활성화
                .authorizeRequests() //특정 경로에 권한을 가진 사용자만 접근 가능
                .antMatchers(PERMIT_URL_ARRAY).permitAll() // 설정된 url은 인증되지 않더라도 누구든 접근 가능
                .anyRequest().authenticated()// 위 페이지 외 인증이 되어야 접근가능(ROLE에 상관없이)
                .and()
                .formLogin().loginPage("/login")  // 접근이 차단된 페이지 클릭시 이동할 url
                .loginProcessingUrl("/loginProc") // 로그인시 맵핑되는 url
                .usernameParameter("userId")      // view form 태그 내에 로그인 할 id 에 맵핑되는 name ( form 의 name )
                .passwordParameter("userPw")      // view form 태그 내에 로그인 할 password 에 맵핑되는 name ( form 의 name )
//                .successHandler(successHandlerHandler()) // 로그인 성공시 실행되는 메소드
//                .failureHandler(failureHandlerHandler()) // 로그인 실패시 실행되는 메소드
                .permitAll()
                .and()
                .logout() // 로그아웃 설정
                .logoutUrl("/logout") // 로그아웃시 맵핑되는 url
                .logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소
                .invalidateHttpSession(true); // 세션 clear
    }

}
