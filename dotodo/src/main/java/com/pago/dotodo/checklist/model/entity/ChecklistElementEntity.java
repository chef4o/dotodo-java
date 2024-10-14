package com.pago.dotodo.checklist.model.entity;

import com.pago.dotodo.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "checklist_elements")
public class ChecklistElementEntity extends BaseEntity {
    private String content;
    private String checklist;
    private String status;
    private Boolean isArchived;
    private Date dueDate;

    public ChecklistElementEntity() {
    }

    public String getContent() {
        return content;
    }

    public ChecklistElementEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public String getChecklist() {
        return checklist;
    }

    public ChecklistElementEntity setChecklist(String checklist) {
        this.checklist = checklist;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ChecklistElementEntity setStatus(String status) {
        this.status = status;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public ChecklistElementEntity setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public ChecklistElementEntity setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }
}
