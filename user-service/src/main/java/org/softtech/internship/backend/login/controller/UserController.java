package org.softtech.internship.backend.login.controller;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.login.model.APIResponse;
import org.softtech.internship.backend.login.model.user.dto.UserLoginDTO;
import org.softtech.internship.backend.login.model.user.dto.UserRegisterDTO;
import org.softtech.internship.backend.login.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<? extends APIResponse<?>> login(@RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<? extends APIResponse<?>> register(@RequestBody UserRegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @GetMapping(path = "/all-users")
    public List<Map<String, String>> allUsers() {
        return userService.getAllUsers();
    }


}
