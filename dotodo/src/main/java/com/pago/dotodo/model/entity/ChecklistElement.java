package com.pago.dotodo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "checklist_elements")
public class ChecklistElement extends BaseEntity {
    private String content;
    private String checklist;
    private String status;
    private Boolean isArchived;
    private Date dueDate;

    public ChecklistElement() {
    }

    public String getContent() {
        return content;
    }

    public ChecklistElement setContent(String content) {
        this.content = content;
        return this;
    }

    public String getChecklist() {
        return checklist;
    }

    public ChecklistElement setChecklist(String checklist) {
        this.checklist = checklist;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ChecklistElement setStatus(String status) {
        this.status = status;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public ChecklistElement setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public ChecklistElement setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }
}
