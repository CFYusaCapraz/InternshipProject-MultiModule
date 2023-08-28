package org.softtech.internship.backend.login.service.user;

import org.softtech.internship.backend.login.model.user.User;
import org.softtech.internship.backend.login.model.user.dto.UserRegisterDTO;

public class UserMapper {

    public static User registerMapper(UserRegisterDTO registerDTO) {
        return User.builder()
                .username(registerDTO.getUsername())
                .password(registerDTO.getPassword())
                .build();
    }
}
