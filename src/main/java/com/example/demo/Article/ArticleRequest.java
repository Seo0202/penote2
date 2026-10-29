package com.example.demo.Article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequest {
    private Long id;
    private String title;
    private String content;
    private String genre = "기타";
    private boolean paid;

    public Article toEntity() {
        return new Article(title, content, genre, paid);
    }
}
