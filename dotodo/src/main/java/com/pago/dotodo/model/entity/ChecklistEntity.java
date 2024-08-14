package com.pago.dotodo.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="checklists")
public class ChecklistEntity extends BaseEntity{
    private String title;
    private String content;
    private Boolean isArchived;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime completedOn;
    private String trackProgress;
    private Set<ChecklistElementEntity> elements;
    private Set<UserEntity> peers;
    private UserEntity owner;

    public ChecklistEntity() {
    }

    public String getTitle() {
        return title;
    }

    public ChecklistEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ChecklistEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public ChecklistEntity setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public ChecklistEntity setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public ChecklistEntity setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public ChecklistEntity setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
        return this;
    }

    public String getTrackProgress() {
        return trackProgress;
    }

    public ChecklistEntity setTrackProgress(String trackProgress) {
        this.trackProgress = trackProgress;
        return this;
    }

    @OneToMany(targetEntity = ChecklistEntity.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE})
    public Set<ChecklistElementEntity> getElements() {
        return elements;
    }

    public ChecklistEntity setElements(Set<ChecklistElementEntity> elements) {
        this.elements = elements;
        return this;
    }

    @ManyToMany
    public Set<UserEntity> getPeers() {
        return peers;
    }

    public ChecklistEntity setPeers(Set<UserEntity> peers) {
        this.peers = peers;
        return this;
    }

    @ManyToOne
    public UserEntity getOwner() {
        return owner;
    }

    public ChecklistEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }
}
