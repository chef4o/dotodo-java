package com.pago.dotodo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name="checklist_elements")
public class ChecklistEntity extends BaseEntity{
    private Integer position;
    private String content;
    private String note;
    private String status;
    private Boolean isArchived;
    private Date dueDate;
    @OneToMany
    private Set<ChecklistElementEntity> elements;
}
