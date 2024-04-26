package com.pago.dotodo.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="checklists")
public class Checklist extends BaseEntity{
    private String title;
    private String content;
    private Boolean isArchived;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime completedOn;
    private String trackProgress;
    private Set<ChecklistElement> elements;
    private Set<User> peers;
    private User owner;

    public Checklist() {
    }

    public String getTitle() {
        return title;
    }

    public Checklist setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Checklist setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public Checklist setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public Checklist setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public Checklist setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public Checklist setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
        return this;
    }

    public String getTrackProgress() {
        return trackProgress;
    }

    public Checklist setTrackProgress(String trackProgress) {
        this.trackProgress = trackProgress;
        return this;
    }

    @OneToMany
    public Set<ChecklistElement> getElements() {
        return elements;
    }

    public Checklist setElements(Set<ChecklistElement> elements) {
        this.elements = elements;
        return this;
    }

    @ManyToMany
    public Set<User> getPeers() {
        return peers;
    }

    public Checklist setPeers(Set<User> peers) {
        this.peers = peers;
        return this;
    }

    @ManyToOne
    public User getOwner() {
        return owner;
    }

    public Checklist setOwner(User owner) {
        this.owner = owner;
        return this;
    }
}
