package com.pago.dotodo.news.model.entity;

import com.pago.dotodo.common.model.BaseEntity;
import com.pago.dotodo.user.model.entity.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "articles")
public class ArticleEntity extends BaseEntity {
    private String header;
    private String content;
    private LocalDateTime uploadDate;
    private Set<ArticleCommentEntity> comments;
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
    public Set<ArticleCommentEntity> getComments() {
        return comments;
    }

    public ArticleEntity setComments(Set<ArticleCommentEntity> comments) {
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
