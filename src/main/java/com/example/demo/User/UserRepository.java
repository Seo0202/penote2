package com.example.demo.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdForUpdate(@Param("id") Long id);

    // 읽고-더하고-저장하는 방식은 동시에 댓글을 달면 별이 덮어써질 수 있으므로
    // DB에서 직접 "star_count = star_count + n" 으로 한 번에 더한다.
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.starCount = u.starCount + :amount where u.id = :id")
    int addStarCount(@Param("id") Long id, @Param("amount") int amount);
}
