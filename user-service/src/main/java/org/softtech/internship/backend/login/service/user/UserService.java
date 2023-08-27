package org.softtech.internship.backend.login.service.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.login.model.APIResponse;
import org.softtech.internship.backend.login.model.user.User;
import org.softtech.internship.backend.login.model.user.dto.UserLoginDTO;
import org.softtech.internship.backend.login.model.user.dto.UserRegisterDTO;
import org.softtech.internship.backend.login.repository.UserRepository;
import org.softtech.internship.backend.login.util.HashHandler;
import org.softtech.internship.backend.login.util.KeyUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final PrivateKey PRIVATE_KEY = KeyUtils.loadPrivateKeyFromResource("/private_key.pem");
    private static final Long EXPIRATION_TIME = 1000L * 60 * 60; // 1 hour
    private final UserRepository userRepository;

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
                String hashedPassword = HashHandler.getHashedPassword(password);
                registerDTO.setPassword(hashedPassword);
                User registeredUser = UserMapper.registerMapper(registerDTO);
                userRepository.saveAndFlush(registeredUser);

                Map<String, Object> data = getData(registeredUser);
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

    private Map<String, Object> getData(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusNanos(EXPIRATION_TIME*1000000L);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        Map<String, Object> data = new HashMap<>();
        data.put("token", generateJwtToken(user.getUserId().toString(), user.getUsername()));
        data.put("expiration_time", expiration.format(timeFormat));
        return data;
    }

    public String generateJwtToken(String userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(PRIVATE_KEY, SignatureAlgorithm.RS256)
                .compact();
    }
}
