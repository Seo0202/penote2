package com.example.demo.Purchase;

import com.example.demo.Article.Article;
import com.example.demo.Article.ArticleRepository;
import com.example.demo.User.User;
import com.example.demo.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EpisodePurchaseService {
    public static final int EPISODE_PRICE = 5;

    private final EpisodePurchaseRepository purchaseRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public boolean hasPurchased(Long userId, Long articleId) {
        return userId != null && purchaseRepository.existsByUser_IdAndArticle_Id(userId, articleId);
    }

    @Transactional
    public void purchase(Long userId, Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("회차를 찾을 수 없습니다."));
        if (!article.isPaid()) {
            throw new IllegalArgumentException("무료 회차는 구매할 필요가 없습니다.");
        }
        if (purchaseRepository.existsByUser_IdAndArticle_Id(userId, articleId)) {
            throw new IllegalArgumentException("이미 구매한 회차입니다.");
        }

        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (user.getStarCount() < EPISODE_PRICE) {
            throw new IllegalArgumentException("별이 부족합니다. 회차 구매에는 별 5개가 필요합니다.");
        }

        user.spendStars(EPISODE_PRICE);
        purchaseRepository.save(new EpisodePurchase(user, article));
    }
}
