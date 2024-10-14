package com.pago.dotodo.user.model.dto;

public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String username;
    private String password;
    private String avatarUrl;
    private String address;
    private String role;

    public UserDto() {

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public UserDto setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getRole() {
        return role;
    }

    public UserDto setRole(String role) {
        this.role = role;
        return this;
    }
}
