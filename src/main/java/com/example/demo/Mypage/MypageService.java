package com.example.demo.Mypage;

import com.example.demo.Purchase.EpisodePurchase;
import com.example.demo.Purchase.EpisodePurchaseRepository;
import com.example.demo.User.User;
import com.example.demo.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final UserRepository userRepository;
    private final EpisodePurchaseRepository purchaseRepository;

    public MypageResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return MypageResponse.from(user);
    }

    public List<PurchasedEpisodeResponse> findPurchasedEpisodes(Long userId) {
        return purchaseRepository.findByUser_IdOrderByPurchasedAtDesc(userId)
                .stream()
                .map(PurchasedEpisodeResponse::from)
                .toList();
    }

    public void updateProfileImage(Long id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("사진을 선택해 주세요.");
        }
        if (file.getSize() > 5L * 1024 * 1024) {
            throw new IllegalArgumentException("사진은 5MB 이하만 가능합니다.");
        }

        String contentType = file.getContentType();
        String extension = switch (contentType == null ? "" : contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("JPG, PNG, WEBP 파일만 업로드할 수 있습니다.");
        };

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        try {
            Path uploadRoot = Paths.get("uploads").toAbsolutePath().normalize();
            Path profileDir = uploadRoot.resolve("profiles");
            Files.createDirectories(profileDir);
            String filename = UUID.randomUUID() + extension;
            Path destination = profileDir.resolve(filename).normalize();

            if (!destination.startsWith(profileDir)) {
                throw new IllegalArgumentException("잘못된 파일 경로입니다.");
            }

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            user.updateProfileImageUrl("/uploads/profiles/" + filename);
            userRepository.save(user);
        } catch (IOException e) {
            throw new IllegalStateException("사진 저장에 실패했습니다.", e);
        }
    }
}
