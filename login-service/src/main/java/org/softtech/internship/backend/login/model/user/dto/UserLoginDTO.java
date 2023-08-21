package org.softtech.internship.backend.login.model.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {
    private String username;
    private String password;
}
