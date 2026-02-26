package com.example.usedTrade.JWT;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.info("jwtAuthenticationEntryPoint 접근");


        Object expiredAttr = request.getAttribute("jwt_expired");

        // AJAX / fetch 요청인지 판별 (헤더 또는 Accept 활용)
        String accept = request.getHeader("Accept");
        String requestedWith = request.getHeader("X-Requested-With");

        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(requestedWith) ||
                (accept != null && accept.contains("application/json"));

        if (expiredAttr != null) {
            // 토큰 만료 상황
            if (isAjax) {
                log.info("토큰 만료됨");
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"TOKEN_EXPIRED\",\"message\":\"access token expired\"}");
                return;
            } else {
                log.info("토큰 만료 안됨");
                // 페이지 요청이면 로그인 페이지로 이동시키되,
                // 스크립트 대신 부드럽게 redirect (원하면 alert 유지)
                response.sendRedirect("/users/login?expired=true");
                return;
            }
        }

        // 기본 (토큰 없음 또는 기타 인증 실패)
        if (isAjax) {
            log.info("토큰 없음 또는 기타 인증 실패");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"UNAUTHORIZED\",\"message\":\"로그인이 필요합니다.\"}");
        } else {
            log.info("로그인 하세영");
            // 기존 스크립트 방식 유지(원하면 그대로)
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>");
            response.getWriter().println("alert('로그인이 필요합니다.');");
            response.getWriter().println("location.href='/users/login';");
            response.getWriter().println("</script>");
            response.getWriter().flush();
        }
    }
}