package com.pago.dotodo.model.dto;

import com.pago.dotodo.util.validation.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordMatch(rawPassword = "rawPassword", rePassword = "rePassword")
public class UserRegisterDto {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";

    private String email;
    private String username;
    private String rawPassword;
    private String rePassword;
    private String password;
    private String role;

    public UserRegisterDto() {
    }

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    public String getEmail() {
        return email;
    }

    public UserRegisterDto setEmail(String email) {
        this.email = email;
        return this;
    }

    @NotBlank(message = "Username is required")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    public String getUsername() {
        return username;
    }

    public UserRegisterDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterDto setPassword(String password) {
        this.password = password;
        return this;
    }

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = PASSWORD_PATTERN,
            message = "Password must be more secure.")
    public String getRawPassword() {
        return rawPassword;
    }

    public UserRegisterDto setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
        return this;
    }

    @NotBlank(message = "Field is required")
    public String getRePassword() {
        return rePassword;
    }

    public UserRegisterDto setRePassword(String rePassword) {
        this.rePassword = rePassword;
        return this;
    }

    public String getRole() {
        return role;
    }

    public UserRegisterDto setRole(String role) {
        this.role = role;
        return this;
    }
}
