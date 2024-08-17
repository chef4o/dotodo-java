package com.pago.dotodo.model.dto;

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

    public String getContent() {
        return content;
    }

    public ContactFormDto setContent(String content) {
        this.content = content;
        return this;
    }
}
