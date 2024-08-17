package com.pago.dotodo.model.dto;

import com.pago.dotodo.model.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Set;

public class ArticleDto {
    private String header;
    private String content;
    private LocalDateTime uploadDate;
    private Set<CommentDto> comments;
    private Long ownerId;

    public ArticleDto() {
    }

    public String getHeader() {
        return header;
    }

    public ArticleDto setHeader(String header) {
        this.header = header;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticleDto setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public ArticleDto setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
        return this;
    }

    public Set<CommentDto> getComments() {
        return comments;
    }

    public ArticleDto setComments(Set<CommentDto> comments) {
        this.comments = comments;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public ArticleDto setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}
