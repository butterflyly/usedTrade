package com.example.usedTrade.Service.Users;

import com.example.usedTrade.DTO.ChartData;
import com.example.usedTrade.DTO.Response.Users.UserImageResponseDto;
import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Entity.Users.UserImage;
import com.example.usedTrade.Entity.Users.UserStatus;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.Users.UserImageRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용
public class UserReadService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserImageRepository userImageRepository;


    public UserResponseDto getUserResponseDTO(String username)
    {
        Users users = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow();

        return new UserResponseDto(users);
    }

    /*
      회원가입 로직 처리
     */
    public boolean existsByUsername(String username) {

        return userRepository.existsByUsername(username);
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkPassword(UserResponseDto userResponseDto , String prePassword) {

        Users users = userRepository.findByUsernameAndDeletedFalse(userResponseDto.getUsername()).orElseThrow();

        String realPassword = users.getPassword();
        boolean matches = passwordEncoder.matches(prePassword, realPassword);
        return matches;
    }


    public UserImageResponseDto getUserImage(Long id) {

        Users users = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("유저없음"));

        UserImage userImage  = userImageRepository.findByUsers(users);

        return new UserImageResponseDto(userImage);
    }

    public Users findByUsername(String username) {

        return userRepository.findByUsernameAndDeletedFalse(username).orElseThrow();
    }

    public Page<UserResponseDto> getUserPageList(UserStatus status,int page) {

        Pageable pageable = PageRequest.of(
                page,
                20,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Users> usersPage;


       if (status == null) {
           usersPage = userRepository.findAllNotDeleted(pageable);
        }
       else {
           usersPage = userRepository.findByStatusAndDeletedFalse(status, pageable);
       }

        return usersPage.map(UserResponseDto :: new);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserDetail(Long id) {
        Users user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return UserResponseDto.from(user);
    }

    public Page<UserResponseDto> getUserSoftPageList(int page) {

        Pageable pageable = PageRequest.of(
                page,
                20,
                Sort.by(Sort.Direction.DESC, "deleteDate"));

        Page<Users> usersPage = userRepository.findByDeletedTrue(pageable);

        return usersPage.map(UserResponseDto :: new);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getDeletedUser(Long id) {
        Users user = userRepository.findByIdAndDeletedTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("탈퇴 유저 아님"));

        return UserResponseDto.from(user);
    }

    public ChartData RegisterChart() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthMinus = now.minusMonths(1);

        List<Object[]> results =
                userRepository.countCreatedUsersByDate(oneMonthMinus, now);

        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Object[] row : results) {
            labels.add(row[0].toString());         // yyyy-MM-dd
            values.add(((Number) row[1]).intValue());
        }

        return new ChartData(labels, values);
    }
}
