package com.pago.dotodo.news.model.dto;

import java.time.LocalDateTime;

public class ArticleCommentDto {
    private String content;
    private LocalDateTime date;
    private Long ownerId;

    public ArticleCommentDto() {
    }

    public String getContent() {
        return content;
    }

    public ArticleCommentDto setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ArticleCommentDto setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public ArticleCommentDto setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}
