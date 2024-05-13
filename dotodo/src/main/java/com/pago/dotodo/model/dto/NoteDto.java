package com.pago.dotodo.model.dto;

import java.time.LocalDateTime;

public class NoteDto {
    private String title;
    private String content;
    private Boolean isArchived;
    private String startDate;
    private String dueDate;
    private String completedOn;
    private String trackProgress;

    public String getTitle() {
        return title;
    }

    public NoteDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NoteDto setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public NoteDto setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public NoteDto setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getDueDate() {
        return dueDate;
    }

    public NoteDto setDueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public NoteDto setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
        return this;
    }

    public String getTrackProgress() {
        return trackProgress;
    }

    public NoteDto setTrackProgress(String trackProgress) {
        this.trackProgress = trackProgress;
        return this;
    }
}
