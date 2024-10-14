package com.pago.dotodo.event.model.dto;

import com.pago.dotodo.common.model.BaseEntity;

public class EventDto extends BaseEntity {
    private String title;
    private String content;
    private Boolean isArchived;
    private String date;
    private String time;
    private Boolean dateOnly;
    private Long ownerId;

    public String getTime() {
        return time;
    }

    public EventDto setTime(String time) {
        this.time = time;
        return this;
    }

    public Boolean getDateOnly() {
        return dateOnly;
    }

    public EventDto setDateOnly(Boolean dateOnly) {
        this.dateOnly = dateOnly;
        return this;
    }

    public EventDto() {
    }

    public String getTitle() {
        return title;
    }

    public EventDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EventDto setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public EventDto setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public String getDate() {
        return date;
    }

    public EventDto setDate(String date) {
        this.date = date;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public EventDto setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}
