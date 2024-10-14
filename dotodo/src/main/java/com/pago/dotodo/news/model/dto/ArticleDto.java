package com.pago.dotodo.news.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ArticleDto {
    private Long id;
    private String header;
    private String content;
    private LocalDateTime uploadDate;
    private Set<ArticleCommentDto> comments;
    private Long ownerId;

    public ArticleDto() {
        comments = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public ArticleDto setId(Long id) {
        this.id = id;
        return this;
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

    public Set<ArticleCommentDto> getComments() {
        return comments;
    }

    public ArticleDto setComments(Set<ArticleCommentDto> comments) {
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
