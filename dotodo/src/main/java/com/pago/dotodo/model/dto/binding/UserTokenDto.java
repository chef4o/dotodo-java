package com.pago.dotodo.model.dto.binding;

public class UserTokenDto {
    private Long id;
    private String firstName;
    private String email;
    private String nickname;

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

    public String getNickname() {
        return nickname;
    }

    public UserTokenDto setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
}
