package com.pago.dotodo.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "articles")
public class ArticleEntity extends BaseEntity {
    private String header;
    private String content;
    private LocalDateTime uploadDate;
    private Set<CommentEntity> comments;
    private UserEntity owner;

    public ArticleEntity() {
    }

    @Column(name = "header", columnDefinition = "TEXT")
    public String getHeader() {
        return header;
    }

    public ArticleEntity setHeader(String header) {
        this.header = header;
        return this;
    }

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public ArticleEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public ArticleEntity setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
        return this;
    }

    @OneToMany
    public Set<CommentEntity> getComments() {
        return comments;
    }

    public ArticleEntity setComments(Set<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    @ManyToOne
    public UserEntity getOwner() {
        return owner;
    }

    public ArticleEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }
}
