package com.pago.dotodo.model.dto;

import com.pago.dotodo.model.entity.UserEntity;


public class NoteDto {
    private Long id;
    private String title;
    private String content;
    private String dueDate;
    private String startDate;
    private Long ownerId;

    public NoteDto() {
    }

    public Long getId() {
        return id;
    }

    public NoteDto setId(Long id) {
        this.id = id;
        return this;
    }

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

    public String getDueDate() {
        return dueDate;
    }

    public NoteDto setDueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public NoteDto setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public NoteDto setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}