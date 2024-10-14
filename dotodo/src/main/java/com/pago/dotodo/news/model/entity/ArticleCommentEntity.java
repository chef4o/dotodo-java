package com.pago.dotodo.news.model.entity;

import com.pago.dotodo.common.model.BaseEntity;
import com.pago.dotodo.user.model.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class ArticleCommentEntity extends BaseEntity {
    private String content;
    private LocalDateTime date;
    private UserEntity owner;

    public ArticleCommentEntity() {
    }

    public String getContent() {
        return content;
    }

    public ArticleCommentEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ArticleCommentEntity setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    @ManyToOne
    public UserEntity getOwner() {
        return owner;
    }

    public ArticleCommentEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }
}
