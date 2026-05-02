package org.example.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.authservice.enums.Role;

@Data
public class RegisterRequestDTO {
    @NotBlank
    @Schema(example = "doctor_ivanov")
    private String username;

    @NotBlank
    @Schema(example = "password123")
    private String password;

    @NotNull
    private Role role;

    // Для роли PATIENT – patientId
    private Long patientId;

    // Для роли DOCTOR – doctorId
    private Long doctorId;
}