package com.pago.dotodo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name="checklist_item")
public class ChecklistElementEntity extends BaseEntity{
    private Integer position;
    private String title;
    private String content;
    private Boolean isArchived;
    private Date startDate;
    private Date dueDate;
    private Date completedOn;
    private String trackProgress;
    @OneToMany
    private Set<UserEntity> sharedWith;
}
