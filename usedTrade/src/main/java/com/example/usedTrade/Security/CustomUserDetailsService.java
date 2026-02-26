package com.example.usedTrade.Security;

import com.example.usedTrade.Entity.Users.UserStatus;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// JWT 기반 인증을 사용할 때 “DB에서 사용자 정보를 꺼내와서 인증 객체로 만드는 역할”
// Spring Security에게 "사용자를 어떻게 찾을지" 알려주는 클래스
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(
                () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: ")
        );

        if (users.getStatus() == UserStatus.BLACK) {
            throw new DisabledException("블랙 처리된 계정");
        }

        return new CustomUserDetails(users);
    }
}