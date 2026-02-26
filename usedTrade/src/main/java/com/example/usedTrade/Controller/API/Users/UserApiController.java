package com.example.usedTrade.Controller.API.Users;

import com.example.usedTrade.DTO.Request.Users.UserUpdateRequestDto;
import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Entity.Users.UserStatus;
import com.example.usedTrade.Form.*;
import com.example.usedTrade.JWT.JwtProvider;
import com.example.usedTrade.JWT.JwtToken;
import com.example.usedTrade.Redis.RedisDao;
import com.example.usedTrade.Security.SecurityUtil;
import com.example.usedTrade.Service.Users.UserCommandService;
import com.example.usedTrade.Service.Users.UserReadService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserApiController {

    private final UserCommandService userCommandService;
    private final UserReadService userReadService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisDao redisDao;

    // 회원가입 완료
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> userRegister(@ModelAttribute UserRegisterForm userRegisterForm,
                                          @RequestPart(value = "files", required = false)
                                          MultipartFile profileImage)
            throws IOException {

        userCommandService.UserCreate(userRegisterForm, profileImage);

        return ResponseEntity.ok().build();
    }

    // 회원가입 검증 로직
     /*
    아이디 중복체크
     */
    @PostMapping("/create/check-duplicate-id")
    public boolean checkDuplicateId(@RequestParam String username) {

        boolean UsernameCheck = userReadService.existsByUsername(username);

        // 삭제 테이블에 유저이름이 있거나 , 기존 테이블에 유저 이름이 있으면 false 반환
        if(UsernameCheck)
        {
            return false;
        }
        else {
            return true;
        }
    }

    /*
     닉네임 중복 체크
    */
    @PostMapping("/create/check-duplicate-nickname")
    public boolean checkDuplicateNickname(@RequestParam String nickname) {


        boolean NicknameCheck = userReadService.existsByNickname(nickname);

        if(NicknameCheck)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /*
    이메일 중복체크
    */

    @PostMapping("/create/check-duplicate-email")
    public boolean checkDuplicateEmail(@RequestParam String email) {
        return !userReadService.existsByEmail(email);
    }

    /*
     인증번호 일치 확인
     */
    /*
    @PostMapping("/mailCheck")
    public ResponseEntity<Boolean> verifyCode(@RequestParam("inputCode") String inputCode, HttpSession session) {
        String storedCode = (String) session.getAttribute("verificationCode");

        if (storedCode != null && storedCode.equals(inputCode)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

     */


    // 수정 로직 검증
    @PostMapping(value = "/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<?> UserUpdateValidate(@AuthenticationPrincipal UserDetails userDetails,
                                         @Valid @ModelAttribute UserUpdateForm userUpdateForm,
                                         BindingResult bindingResult,
                                         @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
    {
        if(bindingResult.hasErrors())
        {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();

            log.info("유효성 검증 실패: {}", errors);

            return ResponseEntity.badRequest().body(errors);
        }

        UserResponseDto userResponseDto = userReadService.getUserResponseDTO(userDetails.getUsername());

        boolean nicknameUnchanged =
                userResponseDto.getNickname().equals(userUpdateForm.getNickname());

        boolean imageUnchanged =
                (profileImage == null || profileImage.isEmpty());

        // 3️⃣ 아무 것도 변경되지 않은 경우
        if (nicknameUnchanged && imageUnchanged) {
            log.info("변경 사항 없음");
            return ResponseEntity.ok().build();
        }

        // 4️⃣ 닉네임 변경 시에만 중복 체크
        if (!nicknameUnchanged) {
            if (userReadService.existsByNickname(userUpdateForm.getNickname())) {
                bindingResult.rejectValue(
                        "nickname",
                        "duplicate",
                        "이미 사용 중인 닉네임입니다."
                );

                return ResponseEntity.badRequest()
                        .body(List.of("이미 사용 중인 닉네임입니다."));
            }
        }

        // 5️⃣ 수정 실행
        userCommandService.userUpdate(
                new UserUpdateRequestDto(userUpdateForm.getNickname()),
                userDetails.getUsername(),
                profileImage
        );

        log.info("회원 정보 수정 완료: {}", userDetails.getUsername());
        return ResponseEntity.ok().build();

    }

    /*
   비밀번호 수정
    */
    @PostMapping("/pwchange")
    public ResponseEntity<?> changePassword(
            @RequestBody PWChangeForm form,
            @AuthenticationPrincipal UserDetails userDetails) {

        UserResponseDto user =
                userReadService.getUserResponseDTO(userDetails.getUsername());

        if (!userReadService.checkPassword(user, form.getPrePassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("현재 비밀번호가 일치하지 않습니다.");
        }

        userCommandService.PWChange(user, form.getNewPassword1());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo() {

        boolean linkCheck = false;

        String username = SecurityUtil.getCurrentUsername();

        if (username == null) {
            // 로그인 안됨 → linkCheck=false 명시적으로 내려줌
            Map<String, Object> response = new HashMap<>();
            response.put("linkCheck", false);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        // 로그인 됨
        linkCheck = true;

        UserResponseDto dto = userReadService.getUserResponseDTO(username);

        Map<String, Object> response = new HashMap<>();
        response.put("linkCheck", true);
        response.put("user", dto);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm request, HttpServletResponse response) {

        // 기존 accessToken 삭제 (로그인 재시도 시 상태 초기화)
        ResponseCookie clearCookie = ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("None")
                .maxAge(60*60) // 쿠키 만료 처리
                .build();
        response.addHeader("Set-Cookie", clearCookie.toString());

        try {
            // 1. 아이디/비밀번호 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserResponseDto user = userReadService.getUserResponseDTO(request.getUsername());

            if (user.getUserStatus() == UserStatus.BLACK) {
                throw new RuntimeException("정지된 계정입니다.");
            }

            // 2. 인증 성공 시 JWT 생성
            JwtToken token = jwtProvider.generateToken(authentication);

            // 3. Access Token을 쿠키로 내려줌
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", token.getAccessToken())
                    .path("/")
                    .httpOnly(true)
                    .secure(false)   // 개발환경 HTTP
                    .sameSite("Lax") // 브라우저 허용
                    .maxAge(60 * 60) // 1시간
                    .build();

            response.addHeader("Set-Cookie", accessCookie.toString());

            // RefreshToken
            long refreshMaxAge = request.isRememberMe()
                    ? 60 * 60 * 24 * 14   // 14일
                    : -1;                // 세션 쿠키


            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                    .path("/")
                    .httpOnly(true)
                    .secure(false)   // 운영 시 true
                    .sameSite("Lax")
                    .maxAge(refreshMaxAge) // 14일
                    .build();

            response.addHeader("Set-Cookie", refreshCookie.toString());

            return ResponseEntity.ok("login success");

        } catch (AuthenticationException e) {
            // 로그인 실패 시 기존 토큰 확실히 제거
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login failed");
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response) {

        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    try {
                        String username =
                                jwtProvider.getUserNameFromToken(cookie.getValue());
                        redisDao.deleteValues(username); // RT 삭제
                    } catch (Exception ignored) {

                    }
                }
            }
        }

        // AccessToken 삭제
        ResponseCookie deleteAccess = ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(0)
                .build();

        // RefreshToken 삭제
        ResponseCookie deleteRefresh = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", deleteAccess.toString());
        response.addHeader("Set-Cookie", deleteRefresh.toString());

        return ResponseEntity.ok("로그아웃 완료");
    }


    // 재발급
    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request,
                                     HttpServletResponse response) {

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null)
            return ResponseEntity.status(401).build();


        String username = jwtProvider.getUserNameFromToken(refreshToken);
        String savedToken = (String) redisDao.getValues(username);

        if (!refreshToken.equals(savedToken))
            return ResponseEntity.status(401).build();

        Authentication authentication = jwtProvider.getAuthentication(username);

        // 🔥 새 토큰 세트 발급
        JwtToken newToken = jwtProvider.generateToken(authentication);

        ResponseCookie accessCookie = ResponseCookie.from(
                        "accessToken", newToken.getAccessToken())
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(60 * 60)
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        return ResponseEntity.ok().build();

    }

    // 수정
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @RequestPart("nickname") String nickname,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();

        UserUpdateRequestDto dto = new UserUpdateRequestDto(nickname);

        userCommandService.userUpdate(dto, username, profileImage);

        return ResponseEntity.ok().build();
    }

    // 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response)
    {
        userCommandService.userDelete(userDetails.getUsername());

        ResponseCookie deleteCookie = ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .secure(false) // 🔥 로그인과 동일하게
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", deleteCookie.toString());

        ResponseCookie deleteRefresh = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", deleteRefresh.toString());

        return ResponseEntity.ok().build();
    }


}
