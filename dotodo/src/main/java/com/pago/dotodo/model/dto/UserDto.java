package com.pago.dotodo.model.dto;

import java.util.Date;

public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private Date dob;
    private String nickname;
    private Integer role;
    private String avatarUrl;
    private String address;

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

    public Date getDob() {
        return dob;
    }

    public UserDto setDob(Date dob) {
        this.dob = dob;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UserDto setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Integer getRole() {
        return role;
    }

    public UserDto setRole(Integer role) {
        this.role = role;
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
}
