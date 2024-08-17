package com.pago.dotodo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {
    private String content;
    private LocalDateTime date;
    private UserEntity owner;

    public CommentEntity() {
    }

    public String getContent() {
        return content;
    }

    public CommentEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public CommentEntity setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    @ManyToOne
    public UserEntity getOwner() {
        return owner;
    }

    public CommentEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }
}
