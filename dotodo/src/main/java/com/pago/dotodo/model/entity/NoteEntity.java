package com.pago.dotodo.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

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

    @NotBlank
    @Length(max = 255)
    public String getTitle() {
        return title;
    }

    public NoteEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    @NotBlank
    @Column(columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public NoteEntity setContent(String content) {
        this.content = content;
        return this;
    }

    @Column(nullable = false)
    public Boolean getArchived() {
        return isArchived;
    }

    public NoteEntity setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    @DateTimeFormat
    @NotNull
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public NoteEntity setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    @DateTimeFormat
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

    @DateTimeFormat
    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public NoteEntity setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
        return this;
    }

    @NotNull
    public String getTrackProgress() {
        return trackProgress;
    }

    public NoteEntity setTrackProgress(String trackProgress) {
        this.trackProgress = trackProgress;
        return this;
    }

    @OneToMany(targetEntity = UserEntity.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE})
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
