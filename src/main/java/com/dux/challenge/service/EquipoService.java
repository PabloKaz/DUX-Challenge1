package com.dux.challenge.service;

import com.dux.challenge.dto.request.EquipoRequest;
import com.dux.challenge.dto.response.EquipoResponse;

import java.util.List;

public interface EquipoService {

    /**
     * Listar todos los equipos.
     */
    List<EquipoResponse> listarTodos();

    /**
     * Buscar un equipo por su ID.
     * @throws ResourceNotFoundException si no existe.
     */
    EquipoResponse buscarPorId(Long id);

    /**
     * Buscar equipos cuyo nombre contenga el valor (case‐insensitive).
     */
    List<EquipoResponse> buscarPorNombre(String nombre);

    /**
     * Crear un nuevo equipo a partir de los datos en EquipoRequest.
     */
    EquipoResponse crear(EquipoRequest request);

    /**
     * Actualizar un equipo existente. 
     * @throws ResourceNotFoundException si el ID no existe.
     */
    EquipoResponse actualizar(Long id, EquipoRequest request);

    /**
     * Eliminar un equipo por ID.
     * @throws ResourceNotFoundException si el ID no existe.
     */
    void eliminar(Long id);
}
