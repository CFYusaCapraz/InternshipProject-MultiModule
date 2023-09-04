package org.softtech.internship.backend.login.service;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.login.model.APIResponse;
import org.softtech.internship.backend.login.model.user.User;
import org.softtech.internship.backend.login.model.user.dto.UserLoginDTO;
import org.softtech.internship.backend.login.model.user.dto.UserRegisterDTO;
import org.softtech.internship.backend.login.repository.UserRepository;
import org.softtech.internship.backend.login.util.HashHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.softtech.internship.backend.login.service.UserMapper.getData;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WebClient.Builder builder;

    public ResponseEntity<? extends APIResponse<?>> login(UserLoginDTO loginDTO) {
        try {
            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();
            if ((username == null || username.isEmpty()) || (password == null || password.isEmpty())) {
                APIResponse<?> body = APIResponse.error("`username` and `password` cannot be empty!");
                return ResponseEntity.unprocessableEntity().body(body);
            } else {
                Optional<User> user = userRepository.findUserByUsernameAndIsDeletedIsFalse(username);
                if (user.isPresent()) {
                    String hashedPassword = HashHandler.getHashedPassword(password);
                    if (hashedPassword.equals(user.get().getPassword())) {
                        Map<String, Object> data = getData(user.get());
                        String token = (String) data.get("token");
                        addTokenToContext(token);
                        APIResponse<Map<String, Object>> body = APIResponse.successWithData(data, "Login success");
                        return ResponseEntity.ok(body);
                    } else {
                        APIResponse<?> body = APIResponse.error("User not found!");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                } else {
                    APIResponse<?> body = APIResponse.error("User not found!");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                }
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while logging in!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> register(UserRegisterDTO registerDTO) {
        try {
            String username = registerDTO.getUsername();
            String password = registerDTO.getPassword();
            if ((username == null || username.isEmpty()) || (password == null || password.isEmpty())) {
                APIResponse<?> body = APIResponse.error("`username` and `password` cannot be empty!");
                return ResponseEntity.unprocessableEntity().body(body);
            } else {
                User registeredUser = UserMapper.registerMapper(registerDTO);
                userRepository.saveAndFlush(registeredUser);
                triggerUserRefreshInApiGateway();
                Map<String, Object> data = getData(registeredUser);
                String token = (String) data.get("token");
                addTokenToContext(token);
                APIResponse<Map<String, Object>> body = APIResponse.successWithData(data, "Registration success");
                return ResponseEntity.status(HttpStatus.CREATED).body(body);
            }
        } catch (DataIntegrityViolationException e) {
            APIResponse<?> body = APIResponse.error("User already exists!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while registering!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public List<Map<String, String>> getAllUsers() {
        try {
            List<User> userList = userRepository.findAllByIsDeletedIsFalse();
            if (userList != null && !userList.isEmpty()) {
                List<Map<String, String>> list = new ArrayList<>();
                for (User user : userList) {
                    list.add(Map.of(
                            "username", user.getUsername(),
                            "password", user.getPassword(),
                            "role", user.getRole().toString()
                    ));
                }
                return list;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private void triggerUserRefreshInApiGateway() {
        builder.build()
                .get()
                .uri("http://localhost:8080/api/gateway/refresh/users")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private void addTokenToContext(String token) {
        String payload = "{\"token\": \"" + token + "\"}";
        builder.build()
                .post()
                .uri("http://localhost:8080/api/gateway/add/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
