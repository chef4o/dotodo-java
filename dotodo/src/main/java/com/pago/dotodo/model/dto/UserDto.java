package com.pago.dotodo.model.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String dob;
    private String username;
    private String password;
    private String avatarUrl;
    private String address;
    private List<String> roles;

    public UserDto() {
        this.roles = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public UserDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserDto setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDob() {
        return dob;
    }

    public UserDto setDob(String dob) {
        this.dob = dob;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getAvatarId() {
        return avatarUrl;
    }

    public UserDto setAvatarId(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public boolean exists() {
        return this.getEmail() != null || this.getUsername() != null;
    }

    public String getPassword() {
        return password;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public UserDto setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public String getFullName() {
        return this.fullName = String.format("%s %s", this.firstName, this.lastName);
    }

    public UserDto setFullName(String fullName) {
        this.fullName = String.format("%s %s", this.firstName, this.lastName);
        return this;
    }
}
