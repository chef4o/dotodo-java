package com.pago.dotodo.model.view;

import java.util.Objects;

public class UserProfileView {
    private String firstName;
    private String lastName;
    private String fullName;
    private String username;
    private String email;
    private String dob;
    private String imgUrl;
    private String phoneNumber;
    private String address;

    public UserProfileView() {
    }

    public String getFullName() {
        return fullName;
    }

    public UserProfileView setFullName() {
        if (this.firstName == null && this.lastName == null) {
            this.fullName = null;
        } else if (this.firstName != null && this.lastName != null) {
            this.fullName = String.format("%s %s", this.getFirstName(), this.getLastName());
        } else this.fullName = Objects.requireNonNullElseGet(this.firstName, () -> this.lastName);

        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserProfileView setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserProfileView setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserProfileView setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfileView setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDob() {
        return dob;
    }

    public UserProfileView setDob(String dob) {
        this.dob = dob;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public UserProfileView setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserProfileView setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserProfileView setAddress(String address) {
        this.address = address;
        return this;
    }
}
