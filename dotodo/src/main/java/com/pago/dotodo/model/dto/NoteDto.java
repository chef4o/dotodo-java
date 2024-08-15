package com.pago.dotodo.model.dto;

import java.util.HashMap;
import java.util.Map;

public class NoteDto {
    private Long id;
    private String title;
    private String content;
    private String dueDate;
    private String dueTime;
    private Boolean dueDateOnly;
    private Map<String, Integer> dueDaysHours;
    private Long ownerId;

    public NoteDto() {
        this.dueDaysHours = new HashMap<>();
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

    public String getDueTime() {
        return dueTime;
    }

    public NoteDto setDueTime(String dueTime) {
        this.dueTime = dueTime;
        return this;
    }

    public Boolean getDueDateOnly() {
        return dueDateOnly;
    }

    public NoteDto setDueDateOnly(Boolean dueDateOnly) {
        this.dueDateOnly = dueDateOnly;
        return this;
    }

    public int getDueDays() {
        return this.dueDaysHours.get("days") != null ? this.dueDaysHours.get("days") : 0;
    }

    public NoteDto setDueDays(int days) {
        this.dueDaysHours.put("days", days);
        return this;
    }

    public int getDueHours() {
        return this.dueDaysHours.get("hours") != null ? this.dueDaysHours.get("hours") : -1;
    }

    public NoteDto setDueHours(int hours) {
        this.dueDaysHours.put("hours", hours);
        return this;
    }

    public Map<String, Integer> getDueDaysHours() {
        return dueDaysHours;
    }

    public NoteDto setDueDaysHours(Map<String, Integer> dueDaysHours) {
        this.dueDaysHours = dueDaysHours;
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