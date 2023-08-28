package org.softtech.internship.backend.login.service;

import org.softtech.internship.backend.login.model.user.User;
import org.softtech.internship.backend.login.model.user.dto.UserRegisterDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.softtech.internship.backend.login.util.JwtHandler.generateJwtToken;

public class UserMapper {
    private static final Long EXPIRATION_TIME = 1000L * 60 * 60; // 1 hour

    public static User registerMapper(UserRegisterDTO registerDTO) {
        return User.builder()
                .username(registerDTO.getUsername())
                .password(registerDTO.getPassword())
                .build();
    }

    public static Map<String, Object> getData(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusNanos(EXPIRATION_TIME * 1000000L);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        Map<String, Object> data = new HashMap<>();
        data.put("token", generateJwtToken(user.getUserId().toString(), user.getUsername()));
        data.put("expiration_time", expiration.format(timeFormat));
        return data;
    }
}
