package com.pago.dotodo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "notes")
public class NoteEntity extends BaseEntity {
    private String title;
    private String content;
    private Boolean isArchived;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private Boolean dueDateOnly;
    private LocalDateTime completedOn;
    private String trackProgress;
    private Set<UserEntity> peers;
    private UserEntity owner;

    public NoteEntity() {
    }

    public String getTitle() {
        return title;
    }

    public NoteEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NoteEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public NoteEntity setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public NoteEntity setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public NoteEntity setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Boolean getDueDateOnly() {
        return dueDateOnly;
    }

    public NoteEntity setDueDateOnly(Boolean dueDateOnly) {
        this.dueDateOnly = dueDateOnly;
        return this;
    }

    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public NoteEntity setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
        return this;
    }

    public String getTrackProgress() {
        return trackProgress;
    }

    public NoteEntity setTrackProgress(String trackProgress) {
        this.trackProgress = trackProgress;
        return this;
    }

    @OneToMany
    public Set<UserEntity> getPeers() {
        return peers;
    }

    public NoteEntity setPeers(Set<UserEntity> peers) {
        this.peers = peers;
        return this;
    }

    @ManyToOne
    public UserEntity getOwner() {
        return owner;
    }

    public NoteEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }
}
