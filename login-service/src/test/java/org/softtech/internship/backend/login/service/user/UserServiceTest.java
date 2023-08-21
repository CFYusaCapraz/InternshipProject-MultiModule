package org.softtech.internship.backend.login.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.softtech.internship.backend.login.model.user.User;
import org.softtech.internship.backend.login.model.user.dto.UserLoginDTO;
import org.softtech.internship.backend.login.repository.UserRepository;
import org.softtech.internship.backend.login.util.HashHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.MockitoAnnotations.openMocks;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Successful login test")
    public void testValidLoginWithCorrectCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(HashHandler.getHashedPassword("testpassword"));

        when(userRepository.findUserByUsernameAndIsDeletedIsFalse(anyString())).thenReturn(Optional.of(user));

        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("testpassword");

        ResponseEntity<?> response = userService.login(loginDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Wrong password login test")
    public void testValidLoginWithIncorrectPassword() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(HashHandler.getHashedPassword("testpassword"));

        when(userRepository.findUserByUsernameAndIsDeletedIsFalse(anyString())).thenReturn(Optional.of(user));

        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("wrongpassword");

        ResponseEntity<?> response = userService.login(loginDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Empty username login test")
    public void testLoginWithEmptyUsername() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("");
        loginDTO.setPassword("testpassword");

        ResponseEntity<?> response = userService.login(loginDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    @DisplayName("Empty password login test")
    public void testLoginWithEmptyPassword() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("");

        ResponseEntity<?> response = userService.login(loginDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    @DisplayName("User not registered test")
    public void testUserNotFound() {
        when(userRepository.findUserByUsernameAndIsDeletedIsFalse(anyString())).thenReturn(Optional.empty());

        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("testpassword");

        ResponseEntity<?> response = userService.login(loginDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Exception occurred while login test")
    public void testExceptionDuringLogin() {
        when(userRepository.findUserByUsernameAndIsDeletedIsFalse(anyString())).thenThrow(new RuntimeException("Simulated exception"));

        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("testpassword");

        ResponseEntity<?> response = userService.login(loginDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }
}