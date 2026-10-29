package com.example.demo.Comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="comments")
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long articleId;
    private String content;


    public Comment(Long articleId, String content) {
        this.articleId = articleId;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
