package com.pago.dotodo.user.model.dto;

public class AdminPanelUserDto {
    private Long id;
    private String email;
    private String username;
    private String role;

    public AdminPanelUserDto() {

    }

    public Long getId() {
        return id;
    }

    public AdminPanelUserDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AdminPanelUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AdminPanelUserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getRole() {
        return role;
    }

    public AdminPanelUserDto setRole(String role) {
        this.role = role;
        return this;
    }
}
