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
import org.softtech.internship.backend.login.model.user.dto.UserRegisterDTO;
import org.softtech.internship.backend.login.repository.UserRepository;
import org.softtech.internship.backend.login.util.HashHandler;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Test
    @DisplayName("Successful registration test")
    public void testSuccessfulRegistration() {
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("testpassword");

        ResponseEntity<?> response = userService.register(registerDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Existing user registration test")
    public void testRegistrationWithExistingUser() {
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername("existinguser");
        registerDTO.setPassword("testpassword");

        when(userRepository.saveAndFlush(any())).thenThrow(DataIntegrityViolationException.class);

        ResponseEntity<?> response = userService.register(registerDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Empty username registration test")
    public void testRegistrationWithEmptyUsername() {
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername("");
        registerDTO.setPassword("testpassword");

        ResponseEntity<?> response = userService.register(registerDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    @DisplayName("Empty password registration test")
    public void testRegistrationWithEmptyPassword() {
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("");

        ResponseEntity<?> response = userService.register(registerDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());

    }

    @Test
    @DisplayName("Exception occurred while registration test")
    public void testExceptionDuringRegistration() {
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("testpassword");

        when(userRepository.saveAndFlush(any())).thenThrow(RuntimeException.class);

        ResponseEntity<?> response = userService.register(registerDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}