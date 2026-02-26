package com.example.usedTrade.Config;

import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Repository.Users.UserRepository;
import com.example.usedTrade.Service.Users.UserCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
/*
  소프트 삭제 데이터 or 특정 시간만 존재하는 데이터를 기간을 설정하여 HARD DELETE 시키는 클래스
 */
public class DeleteCleanUpScheduler {

    private final UserCommandService userCommandService;
    private final UserRepository userRepository;


    @Scheduled(cron = "0 0 0 * * *")
    public void hardDeleteExpiredUsers() {
        LocalDateTime beforeTime = LocalDateTime.now().minusMonths(1);

        List<Long> userIds =
                userRepository.findIdsByDeletedTrueAndDeleteDateBefore(beforeTime);

        for (Long userId : userIds) {
            userCommandService.hardDelete(userId);
        }
    }
}