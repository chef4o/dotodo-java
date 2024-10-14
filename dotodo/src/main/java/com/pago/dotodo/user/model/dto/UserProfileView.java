package com.pago.dotodo.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class UserProfileView {
    private String firstName;
    private String lastName;
    private String fullName;
    private String username;
    private String email;
    private String dob;
    private MultipartFile profilePicture;
    private String imageUrl;
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

    @NotBlank
    @Length(min = 5, message = "Username must be at least 5 characters long")
    public String getUsername() {
        return username;
    }

    public UserProfileView setUsername(String username) {
        this.username = username;
        return this;
    }

    @NotBlank
    @Email(message = "Email must be valid")
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

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public UserProfileView setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    @URL
    public String getImageUrl() {
        return imageUrl;
    }

    public UserProfileView setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
