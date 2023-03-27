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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static final String[] PERMIT_URL_ARRAY = {
            "/error","/js/**", "/css/**", "/image/**", "/dummy/**",
            "/favicon.ico", "/**/favicon.ico",
            //h2
            "/h2-console/**",
            //swagger
            "/swagger-ui.html", "/swagger-ui/index.html", "/swagger/**",
            "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs", "/webjars/**",

    };

    public static final String[] PERMIT_API_URL_ARRAY ={
            // api
            "/","/api/register","/api/register/email-auth/**","/api/login", "/api/register/email-auth","/api/token/reissue",
            "/KakaoLogin/**","/api/member/{param}/follows","/api/feed/post","/api/feed/post/{post_id}","/api/member/resetPassword",
            "/api/member/resetPassword/update",

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
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .antMatchers(PERMIT_API_URL_ARRAY).permitAll()

                .and()
                .authorizeRequests()
                .anyRequest().authenticated();

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
//        config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용
        config.addAllowedOriginPattern( "*"); // 모든 ip에 응답을 허용
        config.addAllowedHeader("*"); // 모든 header에 응답을 허용
        config.addAllowedMethod("*"); // 모든 요청을 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
