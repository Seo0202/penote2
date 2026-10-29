package com.example.demo.Comment;

import lombok.Getter;

@Getter
public class CommentResponse {
    private final Long id;
    private final Long articleId;
    private final String content;
    public CommentResponse(Long id, Long articleId, String content) {
        this.id = id;
        this.articleId = articleId;
        this.content = content;
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getArticleId(), comment.getContent());
    }
}
