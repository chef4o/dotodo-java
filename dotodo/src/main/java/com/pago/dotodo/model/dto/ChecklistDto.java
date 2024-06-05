package com.pago.dotodo.model.dto;

import com.pago.dotodo.model.entity.ChecklistElementEntity;

import java.util.Set;

public class ChecklistDto {
    private String title;
    private String content;
    private Boolean isArchived;
    private String startDate;
    private String dueDate;
    private String completedOn;
    private String trackProgress;
    private Set<ChecklistElementEntity> elements;

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

    public String getStartDate() {
        return startDate;
    }

    public ChecklistDto setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getDueDate() {
        return dueDate;
    }

    public ChecklistDto setDueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public ChecklistDto setCompletedOn(String completedOn) {
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

    public Set<ChecklistElementEntity> getElements() {
        return elements;
    }

    public ChecklistDto setElements(Set<ChecklistElementEntity> elements) {
        this.elements = elements;
        return this;
    }
}