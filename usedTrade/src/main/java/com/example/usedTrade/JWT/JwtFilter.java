package com.example.usedTrade.JWT;

import com.example.usedTrade.Redis.RedisDao;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisDao redisDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        logger.info("JwtFilter 실행됨: " + request.getRequestURI());


        // 🔥 정적 리소스는 모두 JWT 검사 제외
        if (isStaticResource(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveAccessToken(request);

        try {
            // ✅ 1. AccessToken 정상
            if (accessToken != null && jwtProvider.validateToken(accessToken)) {
                Authentication auth = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("AccessToken 인증 성공: {}", auth.getName());
            }

        } catch (ExpiredJwtException e) {
            // ✅ 2. AccessToken 만료 → RefreshToken으로 재발급 시도
            log.info("AccessToken 만료, RefreshToken 확인");

            String refreshToken = resolveRefreshToken(request);

            if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {

                String username = jwtProvider.getUserNameFromToken(refreshToken);
                String savedToken = (String) redisDao.getValues(username);

                // Redis 검증
                if (refreshToken.equals(savedToken)) {

                    Authentication authentication =
                            jwtProvider.getAuthentication(username);

                    JwtToken newToken =
                            jwtProvider.generateToken(authentication);

                    // 새 AccessToken 쿠키 세팅
                    ResponseCookie accessCookie = ResponseCookie.from(
                                    "accessToken",
                                    newToken.getAccessToken())
                            .path("/")
                            .httpOnly(true)
                            .secure(false)   // 운영 시 true
                            .sameSite("Lax")
                            .maxAge(60 * 60)
                            .build();

                    response.addHeader("Set-Cookie", accessCookie.toString());

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    log.info("AccessToken 재발급 성공: {}", username);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // ===== 토큰 추출 =====

    private String resolveAccessToken(HttpServletRequest request) {
        // 1. Authorization 헤더
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        // 2. 쿠키
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    // 🔥 정적 리소스 판별 메서드
    private boolean isStaticResource(String path) {

        return path.startsWith("/profileImages/")
                || path.startsWith("/itemImages/")
                || path.startsWith("/commentImages/")   // ★ commentImage(s) 둘 다 넣는걸 추천
                || path.startsWith("/commentImage/")
                || path.startsWith("/deleteboardImages/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/img/")
                || path.startsWith("/favicon")
                || path.startsWith("/api/users/register")
                || path.startsWith("/api/users/login");
    }

        // Request Header에서 JWT 토큰을 추출하는 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        // 1. 헤더 우선
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        // 2. 쿠키에서 찾기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    log.info("JWT 필터 엑세스 토큰 있음");
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}