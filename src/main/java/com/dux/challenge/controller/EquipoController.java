package com.dux.challenge.controller;

import com.dux.challenge.dto.request.EquipoRequest;
import com.dux.challenge.dto.response.EquipoResponse;
import com.dux.challenge.service.EquipoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipos")
@Tag(name = "Equipos", description = "Operaciones CRUD para equipos de fútbol")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @Operation(summary = "Listar todos los equipos")
    @ApiResponse(responseCode = "200", description = "Listado de equipos")
    @GetMapping
    public ResponseEntity<List<EquipoResponse>> listarTodos() {
        List<EquipoResponse> lista = equipoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener un equipo por ID")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
      @ApiResponse(responseCode = "404", description = "Equipo no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponse> obtenerPorId(@PathVariable Long id) {
        EquipoResponse respuesta = equipoService.buscarPorId(id);
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Buscar equipos por nombre")
    @ApiResponse(responseCode = "200", description = "Listado de equipos que coinciden")
    @GetMapping("/buscar")
    public ResponseEntity<List<EquipoResponse>> buscarPorNombre(
            @RequestParam("nombre") String nombre) {
        List<EquipoResponse> resultados = equipoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(resultados);
    }

    @Operation(summary = "Crear un nuevo equipo")
    @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Equipo creado"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EquipoResponse> crear(
            @Valid @RequestBody EquipoRequest equipoRequest) {
        EquipoResponse creado = equipoService.crear(equipoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar un equipo existente")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Equipo actualizado"),
      @ApiResponse(responseCode = "404", description = "Equipo no existe"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipoRequest equipoRequest) {
        EquipoResponse actualizado = equipoService.actualizar(id, equipoRequest);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar un equipo")
    @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Equipo eliminado"),
      @ApiResponse(responseCode = "404", description = "Equipo no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        equipoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
