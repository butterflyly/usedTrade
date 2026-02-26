package com.example.usedTrade.Service.Users;

import com.example.usedTrade.DTO.Request.Users.UserRequestDto;
import com.example.usedTrade.DTO.Request.Users.UserUpdateRequestDto;
import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Entity.Users.UserImage;
import com.example.usedTrade.Entity.Users.UserRole;
import com.example.usedTrade.Entity.Users.UserStatus;
import com.example.usedTrade.Entity.Users.Users;

import com.example.usedTrade.Form.UserRegisterForm;
import com.example.usedTrade.JWT.JwtProvider;
import com.example.usedTrade.Repository.Users.UserImageRepository;
import com.example.usedTrade.Repository.Users.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserImageRepository userImageRepository;

    @Value("${file.profileImagePath}")
    private String uploadFolder;

    // 회원가입
     /*
     회원가입 로직
     */
    @Transactional
    public void UserCreate(UserRegisterForm userRegisterForm, MultipartFile profileImage) throws IOException {

        String hashPassword = passwordEncoder.encode(userRegisterForm.getPassword());


        UserRequestDto userRequestDto = new UserRequestDto(userRegisterForm.getUsername(),
                userRegisterForm.getNickname(),userRegisterForm.getEmail(), UserRole.NORMAL);
        Users users = userRequestDto.toEntity(hashPassword);
        userRepository.save(users);

        // 이미지 저장 경로
        String imageUrl;

        log.info("uploadFolder = {}", uploadFolder);


        if (profileImage != null && !profileImage.isEmpty()) {
            log.info("이미피 파일 접근");
            String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            profileImage.transferTo(new File(uploadFolder, fileName));

            log.info("saved imageFileName = {}", fileName);


            imageUrl = "/profileImages/" + fileName;
        } else {
            imageUrl = "/profileImages/default.png";
        }

        log.info("FINAL imageUrl = {}", imageUrl);

        // UserImage 엔티티 생성
        UserImage userImage = UserImage.builder()
                .url(imageUrl)
                .users(users)
                .build();

        userImageRepository.save(userImage);
    }

    @Transactional
    public void userUpdate(UserUpdateRequestDto dto, String username, MultipartFile profileImage) {

        Users users = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow();

        // 닉네임 수정
        users.updateNickname(dto.getNickname());

        // 2️⃣ 이미지 변경 처리
        if (profileImage != null && !profileImage.isEmpty()) {

            // 기존 이미지 엔티티 조회 (없을 수도 있음)
            UserImage userImage = userImageRepository.findByUsers(users);

            // 2-1️⃣ 기존 파일 삭제
            if (userImage != null) {
                String oldFileName = new File(userImage.getUrl()).getName();
                File oldFile = new File(uploadFolder, oldFileName);
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // 2-2️⃣ 새 파일 저장
            String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            File saveFile = new File(uploadFolder, fileName);

            try {
                profileImage.transferTo(saveFile);
            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지 저장 실패", e);
            }

            String imageUrl = "/profileImages/" + fileName;

            // 2-3️⃣ 엔티티 처리
            if (userImage == null) {
                userImage = UserImage.builder()
                        .users(users)
                        .url(imageUrl)
                        .build();
                userImageRepository.save(userImage);
            } else {
                userImage.updateUrl(imageUrl);
            }
        }
    }

    // 회원탈퇴
    public void userDelete(String username) {

        Users users = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow();

        // 🔥 refresh token 삭제 (여기서 하는 게 가장 안전)
        jwtProvider.deleteRefreshToken(username);

        users.softDelete();

        userRepository.save(users); // 삭제시간 저장
    }

    public void PWChange(UserResponseDto user,  String newPassword1) {
        Users users = userRepository.findByUsernameAndDeletedFalse(user.getUsername()).orElseThrow();

        users.updatePassword(passwordEncoder.encode(newPassword1));
        this.userRepository.save(users);
    }

    @Transactional
    public void changeStatus(Long userId, UserStatus status) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        user.changeStatus(status);
    }

    @Transactional
    public void restoreUser(Long userId) {

        Users user = userRepository.findByIdAndDeletedTrue(userId)
                .orElseThrow(() -> new IllegalArgumentException("복원 불가"));

        user.restore();
    }

    public void hardDelete(Long userId) {
        if (!userRepository.existsById(userId)) {
            return; // or throw custom exception
        }
        userRepository.deleteById(userId);
    }
}
