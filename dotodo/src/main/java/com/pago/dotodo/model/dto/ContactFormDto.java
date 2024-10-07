package com.pago.dotodo.model.dto;

import com.pago.dotodo.configuration.constraint.error.FormErrors;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class ContactFormDto {
    private String name;
    private String email;
    private String phone;
    private String content;

    public ContactFormDto() {
    }

    public String getName() {
        return name;
    }

    public ContactFormDto setName(String name) {
        this.name = name;
        return this;
    }

    @Email(message = FormErrors.INVALID_EMAIL)
    public String getEmail() {
        return email;
    }

    public ContactFormDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public ContactFormDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Length(min = 10, message = FormErrors.TEXT_TOO_SHORT)
    public String getContent() {
        return content;
    }

    public ContactFormDto setContent(String content) {
        this.content = content;
        return this;
    }
}
