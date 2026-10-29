package com.example.demo.Article;

import lombok.Getter;

@Getter
public class ArticleResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String genre;
    private final boolean paid;
    private final Long authorId;

    public ArticleResponse(Long id, String title, String content, String genre, boolean paid, Long authorId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.genre = genre;
        this.paid = paid;
        this.authorId = authorId;
    }

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getGenre(),
                article.isPaid(),
                article.getAuthor() == null ? null : article.getAuthor().getId()
        );
    }

    public ArticleResponse withoutContent() {
        return new ArticleResponse(id, title, null, genre, paid, authorId);
    }
}
