package com.ecloth.beta.common.security;

import com.ecloth.beta.common.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] PERMIT_URL_ARRAY = {
            "/", "/api/register/**", "/api/email-auth/**", "/api", "/js/**", "/css/**", "/image/**", "/dummy/**",
            "/favicon.ico", "/**/favicon.ico",
            //h2
            "/h2-console/**",
            //swagger
            "/swagger-ui.html", "/swagger-ui/index.html", "/swagger/**",
            "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs", "/webjars/**",
            // api
            "/api/register", "/api/login", "/api/register/email-auth","/api/token/reissue"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, jwtTokenUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //인증 실패
                .accessDeniedHandler(jwtAccessDeniedHandler) //인가 실패

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .anyRequest()
                .authenticated(); //회원가입,이메일인증,로그인,토큰재발급을 제외한 모든 API 인증필요

    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**", "/swagger-ui.html/**","/swagger-ui.html/#/**", "/v2/api-docs")
                .antMatchers("/css/**", "/vendor/**", "/js/**", "/images/**");
        web.ignoring()
                .mvcMatchers("/swagger-ui/*", "/swagger-resources/**", "/swagger-ui.html");
        web.ignoring().antMatchers("*");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 서버가 응답 시 json을 자바스크립트에서 처리할 수 있게 허용877
        config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용
        config.addAllowedHeader("*"); // 모든 header에 응답을 허용
        config.addAllowedMethod("*"); // 모든 요청을 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
