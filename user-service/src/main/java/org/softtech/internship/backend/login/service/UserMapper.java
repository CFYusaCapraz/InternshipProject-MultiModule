package org.softtech.internship.backend.login.service;

import org.softtech.internship.backend.login.model.user.User;
import org.softtech.internship.backend.login.model.user.dto.UserRegisterDTO;
import org.softtech.internship.backend.login.util.HashHandler;
import org.softtech.internship.backend.login.util.JwtHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class UserMapper {
    private static final Long EXPIRATION_TIME = 1000L * 60 * 60; // 1 hour
    private static final JwtHandler jwtHandler = new JwtHandler();

    public static User registerMapper(UserRegisterDTO registerDTO) {
        return User.builder()
                .username(registerDTO.getUsername())
                .password(HashHandler.getHashedPassword(registerDTO.getPassword()))
                .build();
    }

    public static Map<String, Object> getData(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusNanos(EXPIRATION_TIME * 1000000L);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        Map<String, String> extraClaims = new HashMap<>();

        String username = user.getUsername();
        String password = user.getPassword();
        String role = user.getRole().name();

        extraClaims.put("username", username);
        extraClaims.put("password", password);
        extraClaims.put("role", role);

        Map<String, Object> data = new HashMap<>();
        data.put("token", jwtHandler.generateJwtToken(user, null, extraClaims));
        data.put("expiration_time", expiration.format(timeFormat));
        return data;
    }
}
