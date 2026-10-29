package com.example.demo.Mypage;

import com.example.demo.Purchase.EpisodePurchase;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PurchasedEpisodeResponse {
    private final Long articleId;
    private final String title;
    private final String genre;
    private final LocalDateTime purchasedAt;

    public PurchasedEpisodeResponse(Long articleId, String title, String genre, LocalDateTime purchasedAt) {
        this.articleId = articleId;
        this.title = title;
        this.genre = genre;
        this.purchasedAt = purchasedAt;
    }

    public static PurchasedEpisodeResponse from(EpisodePurchase purchase) {
        return new PurchasedEpisodeResponse(
                purchase.getArticle().getId(),
                purchase.getArticle().getTitle(),
                purchase.getArticle().getGenre(),
                purchase.getPurchasedAt()
        );
    }
}
