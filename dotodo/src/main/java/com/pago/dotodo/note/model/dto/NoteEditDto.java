package com.pago.dotodo.note.model.dto;

import com.pago.dotodo.common.model.Date;
import jakarta.validation.constraints.NotBlank;

public class NoteEditDto implements Date {
    private Long id;
    private String title;
    private String content;
    private String dueDate;
    private String dueTime;
    private Boolean dueDateOnly;

    public NoteEditDto() {
    }

    public Long getId() {
        return id;
    }

    public NoteEditDto setId(Long id) {
        this.id = id;
        return this;
    }

    @NotBlank
    public String getTitle() {
        return title;
    }

    public NoteEditDto setTitle(String title) {
        this.title = title;
        return this;
    }

    @NotBlank
    public String getContent() {
        return content;
    }

    public NoteEditDto setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String getDueDate() {
        return dueDate;
    }

    public NoteEditDto setDueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    @Override
    public String getDueTime() {
        return dueTime;
    }

    public NoteEditDto setDueTime(String dueTime) {
        this.dueTime = dueTime;
        return this;
    }

    public Boolean getDueDateOnly() {
        return dueDateOnly;
    }

    public NoteEditDto setDueDateOnly(Boolean dueDateOnly) {
        this.dueDateOnly = dueDateOnly;
        return this;
    }
}