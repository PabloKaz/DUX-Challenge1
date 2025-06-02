package com.dux.challenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO usado en POST /equipos y PUT /equipos/{id}
 */
@Data
public class EquipoRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La liga no puede estar vacía")
    private String liga;

    @NotBlank(message = "El país no puede estar vacío")
    private String pais;
}
