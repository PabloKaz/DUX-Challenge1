package com.dux.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO que devuelve la información de un equipo.
 */
@Data
@AllArgsConstructor
public class EquipoResponse {
    private Long id;
    private String nombre;
    private String liga;
    private String pais;
}