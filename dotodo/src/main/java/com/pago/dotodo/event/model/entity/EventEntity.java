package com.pago.dotodo.event.model.entity;

import com.pago.dotodo.common.model.BaseEntity;
import com.pago.dotodo.user.model.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
public class EventEntity extends BaseEntity {
    private String title;
    private String content;
    private Boolean isArchived;
    private LocalDateTime date;
    private Set<UserEntity> peers;
    private UserEntity owner;

    public EventEntity() {
    }

    public String getTitle() {
        return title;
    }

    public EventEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EventEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public EventEntity setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public EventEntity setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    @ManyToMany
    public Set<UserEntity> getPeers() {
        return peers;
    }

    public EventEntity setPeers(Set<UserEntity> peers) {
        this.peers = peers;
        return this;
    }

    @ManyToOne
    public UserEntity getOwner() {
        return owner;
    }

    public EventEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }
}
