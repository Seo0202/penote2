package com.example.demo.Purchase;

import com.example.demo.Article.Article;
import com.example.demo.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "episode_purchases",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "article_id"})
)
@Getter
@NoArgsConstructor
public class EpisodePurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;

    public EpisodePurchase(User user, Article article) {
        this.user = user;
        this.article = article;
        this.purchasedAt = LocalDateTime.now();
    }
}
