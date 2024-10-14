package com.pago.dotodo.auth.model.dto;

public class UserAuthDto {
    private String username;

    public String getUsername() {
        return username;
    }

    public UserAuthDto setUsername(String username) {
        this.username = username;
        return this;
    }
}