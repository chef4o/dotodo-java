package com.pago.dotodo.model.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name="users")
public class UserEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Date dateOfBirth;
    private String phoneNumber;
    private Date createdAt;
    private Date updatedAt;
    private String imageUrl;
    @ManyToOne
    private AddressEntity addressEntity;
    @OneToMany
    private Set<UserEntity> friends;
    @OneToMany
    private Set<NoteEntity> noteEntities;
    @OneToMany
    private Set<ChecklistEntity> checklistEntities;
}
