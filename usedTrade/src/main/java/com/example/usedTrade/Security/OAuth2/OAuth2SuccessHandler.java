package com.example.usedTrade.Security.OAuth2;

import com.example.usedTrade.JWT.JwtProvider;
import com.example.usedTrade.JWT.JwtToken;
import com.example.usedTrade.Repository.Users.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        // JwtProvider의 generateToken(Authentication) 사용 => JwtToken( access + refresh ) 반환
        JwtToken jwtToken = jwtProvider.generateToken(authentication);

        String accessToken = jwtToken.getAccessToken();
        String refreshToken = jwtToken.getRefreshToken();

        log.info("OAuth2 로그인 성공 - username: {}, issuing tokens", authentication.getName());

        // 액세스 토큰 쿠키
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .path("/")
                .httpOnly(true)
                .sameSite("Lax")
                // .maxAge(60 * 60) // 필요하면 만료시간(초) 설정. jwtProvider의 설정값에 맞춰 조정 가능
                .build();

        // 리프레시 토큰 쿠키 (httpOnly로 내려주기)
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .sameSite("Lax")
                // .maxAge(60 * 60 * 24 * 7) // 예: 7일 (필요하면 설정)
                .build();

        // 쿠키 추가
        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        // 원하면 리다이렉트 대신 JSON 응답으로 토큰을 넘겨줄 수도 있음.
        // 여기서는 기존 흐름을 유지: 마이페이지로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, "/users/mypage");
    }
}