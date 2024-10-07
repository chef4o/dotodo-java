package com.pago.dotodo.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

public class EditUserProfile {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String dob;
    private MultipartFile profilePicture;
    private String imgUrl;
    private String phoneNumber;
    private String street;
    private String town;

    public EditUserProfile() {
    }

    public String getFirstName() {
        return firstName;
    }

    public EditUserProfile setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public EditUserProfile setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @NotBlank
    @Length(min = 5, message = "Username must be at least 5 characters long")
    public String getUsername() {
        return username;
    }

    public EditUserProfile setUsername(String username) {
        this.username = username;
        return this;
    }

    @NotBlank
    @Email(message = "Email must be valid")
    public String getEmail() {
        return email;
    }

    public EditUserProfile setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDob() {
        return dob;
    }

    public EditUserProfile setDob(String dob) {
        this.dob = dob;
        return this;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public EditUserProfile setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    @URL
    public String getImgUrl() {
        return imgUrl;
    }

    public EditUserProfile setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public EditUserProfile setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public EditUserProfile setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getTown() {
        return town;
    }

    public EditUserProfile setTown(String town) {
        this.town = town;
        return this;
    }
}
