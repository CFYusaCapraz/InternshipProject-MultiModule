package org.softtech.internship.backend.login.model.user.dto;

import lombok.Getter;

@Getter
public class UserRegisterDTO {
    private String username;
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }
}