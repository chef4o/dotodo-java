package com.pago.dotodo.model.dto.binding;

public class UserTokenDto {
    private Long id;
    private String firstName;
    private String email;
    private String username;
    private String role;

    public UserTokenDto() {
    }

    public Long getId() {
        return id;
    }

    public UserTokenDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserTokenDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserTokenDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserTokenDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getRole() {
        return role;
    }

    public UserTokenDto setRole(String role) {
        this.role = role;
        return this;
    }

    public boolean exists() {
        return this.getId() != null;
    }
}
