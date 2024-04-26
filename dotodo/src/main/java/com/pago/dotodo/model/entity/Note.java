package com.pago.dotodo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "notes")
public class Note extends BaseEntity {
    private String title;
    private String content;
    private Boolean isArchived;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime completedOn;
    private String trackProgress;
    private Set<User> peers;
    private User owner;

    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public Note setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Note setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public Note setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public Note setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public Note setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public Note setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
        return this;
    }

    public String getTrackProgress() {
        return trackProgress;
    }

    public Note setTrackProgress(String trackProgress) {
        this.trackProgress = trackProgress;
        return this;
    }

    @OneToMany
    public Set<User> getPeers() {
        return peers;
    }

    public Note setPeers(Set<User> peers) {
        this.peers = peers;
        return this;
    }

    @ManyToOne
    public User getOwner() {
        return owner;
    }

    public Note setOwner(User owner) {
        this.owner = owner;
        return this;
    }
}
