package com.example.demo.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder().email(dto.getEmail()).password(bCryptPasswordEncoder.encode(dto.getPassword())).build()).getId();
    }

    // 별 적립 (호출한 쪽의 트랜잭션에 함께 묶인다)
    @Transactional
    public void addStars(Long userId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("적립할 별은 1개 이상이어야 합니다.");
        }
        int updated = userRepository.addStarCount(userId, amount);
        if (updated == 0) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + userId);
        }
    }
} 
