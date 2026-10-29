package com.example.demo.Comment;

import lombok.Getter;
import lombok.Setter;

public class CommentRequest {

    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private Long articleId;

    @Getter
    @Setter
    private String content;

    public Comment toEntity() {
        return new Comment(articleId, content);
    }

}
