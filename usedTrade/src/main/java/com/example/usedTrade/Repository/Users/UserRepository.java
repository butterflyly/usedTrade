package com.example.usedTrade.Repository.Users;

import com.example.usedTrade.Entity.Users.UserStatus;
import com.example.usedTrade.Entity.Users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsernameAndDeletedFalse(String username);

    // 소프트삭제된 회원들 유효성 검사도 해야 아므로
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);

    Page<Users> findByStatusAndDeletedFalse(UserStatus status, Pageable pageable);

    @Query("select u from Users u where u.deleted = false")
    Page<Users> findAllNotDeleted(Pageable pageable);

    Optional<Users> findByIdAndDeletedFalse(Long id);

    Page<Users> findByDeletedTrue(Pageable pageable);

    Optional<Users> findByIdAndDeletedTrue(Long id);

    List<Long> findIdsByDeletedTrueAndDeleteDateBefore(LocalDateTime time);


    @Query("""
    SELECT DATE(u.createdAt), COUNT(u)
    FROM Users u
    WHERE u.createdAt BETWEEN :start AND :end
    GROUP BY DATE(u.createdAt)
    ORDER BY DATE(u.createdAt)
    """)
    List<Object[]> countCreatedUsersByDate(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
