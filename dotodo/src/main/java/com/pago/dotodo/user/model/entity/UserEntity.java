package com.pago.dotodo.user.model.entity;

import com.pago.dotodo.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;
    private RoleEntity role;
    private AddressEntity address;

    public UserEntity() {
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @NotBlank
    @Column(nullable = false, unique = true)
    @Email
    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    @Column(name = "username", nullable = false, unique = true)
    @Length(min = 5)
    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public UserEntity setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    @Column(name = "phone_num")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserEntity setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Column(name = "created")
    @DateTimeFormat
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Column(name = "updated")
    @DateTimeFormat
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UserEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Column(name = "img_url")
    @URL
    public String getImageUrl() {
        return imageUrl;
    }

    public UserEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public RoleEntity getRole() {
        return role;
    }

    public UserEntity setRole(RoleEntity role) {
        this.role = role;
        return this;
    }

    @ManyToOne
    public AddressEntity getAddress() {
        return address;
    }

    public UserEntity setAddress(AddressEntity address) {
        this.address = address;
        return this;
    }
}