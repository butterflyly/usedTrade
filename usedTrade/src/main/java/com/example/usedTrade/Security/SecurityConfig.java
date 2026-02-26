package com.example.usedTrade.Security;

import com.example.usedTrade.JWT.JwtAuthenticationEntryPoint;
import com.example.usedTrade.JWT.JwtFilter;
import com.example.usedTrade.Security.OAuth2.OAuth2SuccessHandler;
import com.example.usedTrade.Security.OAuth2.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private final AccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final PrincipalOAuth2UserService principalOAuth2UserService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.
                csrf(AbstractHttpConfigurer::disable) // 임시로 csrf 공격 해제 완성이후 변경
                .authorizeHttpRequests(auth -> auth
                        /* =======================
                          * 🔓 완전 공개 리소스
                          ======================= */
                        .requestMatchers(
                                "/",
                                "/favicon.ico",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/.well-known/**"
                        ).permitAll()

                        /* =======================
                         * 🔓 이미지 리소스
                         ======================= */
                        .requestMatchers(
                                "/profileImages/**",
                                "/itemImages/**",
                                "/commentImage/**",
                                "/deleteboardImages/**"
                        ).permitAll()

                        /* =======================
                         * 🔓 인증 / 회원가입 / 로그인
                         ======================= */
                        .requestMatchers(
                                "/api/users/create/**",
                                "/users/login",
                                "/users/register/**",
                                "/api/users/register",
                                "/api/users/login",
                                "/api/auth/**"
                        ).permitAll()

                        /* =======================
                         * 🔓 게시글 조회 (View)
                         ======================= */
                        .requestMatchers(
                                "/item-posts",
                                "/item-posts/*",
                                "/item-posts/detail/*"
                        ).permitAll()

                        /* =======================
                         * 🔓 게시글 조회 (API)
                         ======================= */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/item-posts/**"
                        ).permitAll()

                        /* =======================
                         * 🔐 관리자 페이지
                         ======================= */
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        /* =======================
                         * 🔐 사용자 페이지
                         ======================= */
                        .requestMatchers("/users/**").authenticated()

                        /* =======================
                         * 🔐 게시글 CUD
                         ======================= */
                        .requestMatchers(HttpMethod.POST, "/api/item-posts").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/item-posts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/item-posts/**").authenticated()

                        /* =======================
                         * 🔐 채팅
                         ======================= */
                        .requestMatchers("/chat/**", "/api/chat/**").authenticated()

                        /* =======================
                         * 🔐 API 기본 차단
                         ======================= */
                        .requestMatchers("/api/**").authenticated()

                        // 🔐 게시글 작성/수정 페이지 접근 제한
                        .requestMatchers(
                                HttpMethod.GET,
                                "/item-posts/new",
                                "/item-posts/modify/**"
                        ).authenticated()

                        .requestMatchers("/WEB-INF/**").permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 401
                        .accessDeniedHandler(customAccessDeniedHandler)         // 403
                )
                .anonymous(withDefaults()) // 비로그인 접근 활성화
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ 세션 비활성화
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(principalOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                );

        // JWT 방식을 사용하므로 폼 로그인, 로그아웃을 사용하지 않음
        http.formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }


    /*
    기존 Spring Security(버전 5 이하)는 내부적으로 AuthenticationManager 를 자동으로 생성해줬음
    하지만 Spring Security 6에서는:

    "개발자가 어떤 인증 방식을 쓰는지 모르니
    AuthenticationManager는 직접 설정해라."
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}