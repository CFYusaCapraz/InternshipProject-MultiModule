package org.softtech.internship.backend.apigateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.softtech.internship.backend.apigateway.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
public class GatewayService {
    private final Map<String, User> userDatabase = new ConcurrentHashMap<>();
    private final List<String> tokenDatabase = new ArrayList<>();
    private final WebClient.Builder webClientBuilder;

    public GatewayService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        loadUsers();
    }

    private void loadUsers() {
        webClientBuilder.build()
                .get()
                .uri("http://localhost:9999/api/user/all-users")
                .retrieve()
                .bodyToMono(User[].class)
                .subscribe(users1 -> {
                    userDatabase.clear();
                    for (User user : users1) {
                        userDatabase.put(user.getUsername(), user);
                    }
                });
    }

    public void refreshUsers() {
        loadUsers();
    }

    @SneakyThrows
    public boolean addToken(String token) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(token, Map.class);
        String jwtToken = map.get("token");
        return tokenDatabase.add(jwtToken);
    }

    public boolean isTokenRegistered(String token) {
        return tokenDatabase.stream()
                .anyMatch(s -> s.equals(token));
    }

    @SneakyThrows
    public boolean removeToken(String token) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(token, Map.class);
        String jwtToken = map.get("token");
        boolean removed = tokenDatabase.remove(jwtToken);
        refreshUsers();
        return removed;
    }
}
