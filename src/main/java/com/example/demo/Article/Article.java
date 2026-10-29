package com.example.demo.Article;

import com.example.demo.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String genre = "기타";

    @Column(nullable = false)
    private boolean paid = false;

    // Nullable for compatibility with articles created before ownership was added.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    public Article(String title, String content, String genre, boolean paid) {
        this.title = title;
        this.content = content;
        this.genre = genre == null || genre.isBlank() ? "기타" : genre;
        this.paid = paid;
    }

    public void update(String title, String content, String genre, boolean paid) {
        this.title = title;
        this.content = content;
        this.genre = genre == null || genre.isBlank() ? "기타" : genre;
        this.paid = paid;
    }
}
