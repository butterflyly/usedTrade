package com.example.usedTrade.Security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        log.info("CustomAccessDeniedHandler 접근");

        // alert 띄우고 리다이렉트
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(
                "<script>alert('접근 권한이 없습니다.'); location.href='/item-posts';</script>"
        );
        response.getWriter().flush();
    }
}