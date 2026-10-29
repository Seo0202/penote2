package com.example.demo.Purchase;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodePurchaseRepository extends JpaRepository<EpisodePurchase, Long> {
    boolean existsByUser_IdAndArticle_Id(Long userId, Long articleId);
    List<EpisodePurchase> findByUser_IdOrderByPurchasedAtDesc(Long userId);
}
