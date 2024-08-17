package com.pago.dotodo.model.dto;

import java.time.LocalDateTime;

public class CommentDto {
    private String content;
    private LocalDateTime date;
    private Long ownerId;

    public CommentDto() {
    }

    public String getContent() {
        return content;
    }

    public CommentDto setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public CommentDto setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public CommentDto setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}
