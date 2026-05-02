package org.example.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank
    @Schema(example = "doctor_ivanov")
    private String username;

    @NotBlank
    @Schema(example = "password123")
    private String password;
}