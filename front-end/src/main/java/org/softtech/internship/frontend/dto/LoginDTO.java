/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author yusa
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginDTO {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
