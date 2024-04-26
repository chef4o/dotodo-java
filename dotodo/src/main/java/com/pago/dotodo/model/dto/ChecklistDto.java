package com.pago.dotodo.model.dto;

import com.pago.dotodo.model.entity.ChecklistElement;
import com.pago.dotodo.model.entity.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public class ChecklistDto {
    private String title;
    private String content;
    private Boolean isArchived;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime completedOn;
    private String trackProgress;
    private Set<ChecklistElement> elements;

    public ChecklistDto() {
    }

    public String getTitle() {
        return title;
    }

    public ChecklistDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ChecklistDto setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public ChecklistDto setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public ChecklistDto setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public ChecklistDto setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public ChecklistDto setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
        return this;
    }

    public String getTrackProgress() {
        return trackProgress;
    }

    public ChecklistDto setTrackProgress(String trackProgress) {
        this.trackProgress = trackProgress;
        return this;
    }

    public Set<ChecklistElement> getElements() {
        return elements;
    }

    public ChecklistDto setElements(Set<ChecklistElement> elements) {
        this.elements = elements;
        return this;
    }
}