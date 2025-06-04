package com.dux.challenge.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dux.challenge.dto.request.EquipoRequest;
import com.dux.challenge.dto.response.EquipoResponse;
import com.dux.challenge.exception.ResourceNotFoundException;
import com.dux.challenge.model.Equipo;
import com.dux.challenge.repository.EquipoRepository;

@Service
public class EquipoServiceImpl implements EquipoService {

    @Autowired
    private EquipoRepository equipoRepo;

    @Override
    public List<EquipoResponse> listarTodos() {
        return equipoRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EquipoResponse buscarPorId(Long id) {
        Equipo equipo = equipoRepo.findById(id)
                .orElseThrow(() -> 
                    new ResourceNotFoundException("Equipo no encontrado con ID: " + id)
                );
        return mapToResponse(equipo);
    }
 
    @Override
    public List<EquipoResponse> buscarPorNombre(String nombre) {
        return equipoRepo.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EquipoResponse crear(EquipoRequest request) {
        Equipo equipo = new Equipo();
        equipo.setNombre(request.getNombre());
        equipo.setLiga(request.getLiga());
        equipo.setPais(request.getPais());
        Equipo guardado = equipoRepo.save(equipo);
        return mapToResponse(guardado);
    }

    @Override
    public EquipoResponse actualizar(Long id, EquipoRequest request) {
        Equipo equipoExistente = equipoRepo.findById(id)
                .orElseThrow(() -> 
                    new ResourceNotFoundException("Equipo no encontrado con ID: " + id)
                );
        equipoExistente.setNombre(request.getNombre());
        equipoExistente.setLiga(request.getLiga());
        equipoExistente.setPais(request.getPais());
        Equipo actualizado = equipoRepo.save(equipoExistente);
        return mapToResponse(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Equipo equipoExistente = equipoRepo.findById(id)
                .orElseThrow(() -> 
                    new ResourceNotFoundException("Equipo no encontrado con ID: " + id)
                );
        equipoRepo.delete(equipoExistente);
    }

    /**
     * Convierte la entidad Equipo en DTO EquipoResponse.
     */
    private EquipoResponse mapToResponse(Equipo eq) {
        return new EquipoResponse(
                eq.getId(),
                eq.getNombre(),
                eq.getLiga(),
                eq.getPais()
        );
    }
}
