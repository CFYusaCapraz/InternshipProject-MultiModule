package org.softtech.internship.backend.login.model.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class GatewayViewDTO {
    @JsonProperty("users")
    private List<Map<String, String>> users;
}
