// src/test/java/com/dux/challenge/service/EquipoServiceImplTest.java
package com.dux.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dux.challenge.dto.request.EquipoRequest;
import com.dux.challenge.dto.response.EquipoResponse;
import com.dux.challenge.exception.ResourceNotFoundException;
import com.dux.challenge.model.Equipo;
import com.dux.challenge.repository.EquipoRepository;

@ExtendWith(MockitoExtension.class)
class EquipoServiceImplTest {

    @Mock
    private EquipoRepository equipoRepo;

    @InjectMocks
    private EquipoServiceImpl equipoService;

    // --- 1) listarTodos() ---
    @Test
    @DisplayName("listarTodos -> devuelve lista vacía cuando no hay entidades")
    void listarTodos_vacio() {
        // dado
        given(equipoRepo.findAll()).willReturn(List.of());

        // cuando
        List<EquipoResponse> resultado = equipoService.listarTodos();

        // entonces
        assertThat(resultado).isEmpty();
        then(equipoRepo).should().findAll();
    }

    @Test
    @DisplayName("listarTodos -> convierte correctamente entidades a DTOs")
    void listarTodos_conDatos() {
        // dado: dos entidades simuladas
        Equipo e1 = new Equipo();
        e1.setId(1L);
        e1.setNombre("FC Barcelona");
        e1.setLiga("LaLiga");
        e1.setPais("España");

        Equipo e2 = new Equipo();
        e2.setId(2L);
        e2.setNombre("Liverpool");
        e2.setLiga("Premier League");
        e2.setPais("Inglaterra");

        given(equipoRepo.findAll()).willReturn(List.of(e1, e2));

        // cuando
        List<EquipoResponse> resultado = equipoService.listarTodos();

        // entonces
        assertThat(resultado).hasSize(2)
            .extracting(EquipoResponse::getId,
                        EquipoResponse::getNombre,
                        EquipoResponse::getLiga,
                        EquipoResponse::getPais)
            .containsExactly(
                tuple(1L, "FC Barcelona", "LaLiga", "España"),
                tuple(2L, "Liverpool",   "Premier League", "Inglaterra")
            );

        then(equipoRepo).should().findAll();
    }

    // --- 2) buscarPorId(Long) ---
    @Test
    @DisplayName("buscarPorId -> lanza ResourceNotFoundException si no existe")
    void buscarPorId_noEncontrado() {
        // dado: repositorio no encuentra nada
        given(equipoRepo.findById(99L)).willReturn(Optional.empty());

        // cuando / entonces
        assertThatThrownBy(() -> equipoService.buscarPorId(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Equipo no encontrado con ID: 99");

        then(equipoRepo).should().findById(99L);
    }

    @Test
    @DisplayName("buscarPorId -> devuelve DTO cuando existe entidad")
    void buscarPorId_encontrado() {
        // dado
        Equipo e = new Equipo();
        e.setId(7L);
        e.setNombre("Juventus");
        e.setLiga("Serie A");
        e.setPais("Italia");

        given(equipoRepo.findById(7L)).willReturn(Optional.of(e));

        // cuando
        EquipoResponse resultado = equipoService.buscarPorId(7L);

        // entonces
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(7L);
        assertThat(resultado.getNombre()).isEqualTo("Juventus");
        assertThat(resultado.getLiga()).isEqualTo("Serie A");
        assertThat(resultado.getPais()).isEqualTo("Italia");

        then(equipoRepo).should().findById(7L);
    }

    // --- 3) buscarPorNombre(String) ---
    @Test
    @DisplayName("buscarPorNombre -> devuelve lista filtrada")
    void buscarPorNombre() {
        // dado: repositorio devuelve una sola entidad cuyo nombre contiene "Real"
        Equipo e = new Equipo();
        e.setId(5L);
        e.setNombre("Real Madrid");
        e.setLiga("LaLiga");
        e.setPais("España");

        given(equipoRepo.findByNombreContainingIgnoreCase("Real"))
            .willReturn(List.of(e));

        // cuando
        List<EquipoResponse> resultado = equipoService.buscarPorNombre("Real");

        // entonces
        assertThat(resultado).hasSize(1);
        EquipoResponse r = resultado.get(0);
        assertThat(r.getId()).isEqualTo(5L);
        assertThat(r.getNombre()).isEqualTo("Real Madrid");
        assertThat(r.getLiga()).isEqualTo("LaLiga");
        assertThat(r.getPais()).isEqualTo("España");

        then(equipoRepo).should().findByNombreContainingIgnoreCase("Real");
    }

    // --- 4) crear(EquipoRequest) ---
    @Test
    @DisplayName("crear -> guarda correctamente y retorna DTO con ID")
    void crear() {
        // dado: request que viene del cliente
        EquipoRequest request = new EquipoRequest();
        request.setNombre("Ajax");
        request.setLiga("Eredivisie");
        request.setPais("Países Bajos");

        // simulamos entidad antes de persistir (sin ID)
        Equipo sinId = new Equipo();
        sinId.setNombre("Ajax");
        sinId.setLiga("Eredivisie");
        sinId.setPais("Países Bajos");

        // simulamos que, al guardar, JPA le asigna ID = 10
        Equipo conId = new Equipo();
        conId.setId(10L);
        conId.setNombre("Ajax");
        conId.setLiga("Eredivisie");
        conId.setPais("Países Bajos");

        // Cuando mapeamos desde request a entidad, el service crea un nuevo Equipo y setea manualmente.
        // No mockeamos ese “mapToResponse” porque es código nuestro; sí simulamos el repositorio:
        given(equipoRepo.save(any(Equipo.class))).willReturn(conId);

        // cuando
        EquipoResponse resultado = equipoService.crear(request);

        // entonces
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getNombre()).isEqualTo("Ajax");
        assertThat(resultado.getLiga()).isEqualTo("Eredivisie");
        assertThat(resultado.getPais()).isEqualTo("Países Bajos");

        // Verificamos que guardó un Equipo con los mismos datos del request:
        then(equipoRepo).should().save(argThat(e -> 
            e.getNombre().equals("Ajax") &&
            e.getLiga().equals("Eredivisie") &&
            e.getPais().equals("Países Bajos")
        ));
    }

    // --- 5) actualizar(Long, EquipoRequest) ---
    @Test
    @DisplayName("actualizar -> lanza ResourceNotFoundException si no existe")
    void actualizar_noEncontrado() {
        EquipoRequest req = new EquipoRequest();
        req.setNombre("PSG");
        req.setLiga("Ligue 1");
        req.setPais("Francia");

        given(equipoRepo.findById(123L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> equipoService.actualizar(123L, req))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Equipo no encontrado con ID: 123");

        then(equipoRepo).should().findById(123L);
        then(equipoRepo).should(never()).save(any());
    }

    @Test
    @DisplayName("actualizar -> modifica campos y retorna DTO actualizado")
    void actualizar_existente() {
        // existente en BD
        Equipo exist = new Equipo();
        exist.setId(8L);
        exist.setNombre("Old Name");
        exist.setLiga("Old Liga");
        exist.setPais("Old Pais");

        given(equipoRepo.findById(8L)).willReturn(Optional.of(exist));

        // request con nuevos valores
        EquipoRequest req = new EquipoRequest();
        req.setNombre("PSG");
        req.setLiga("Ligue 1");
        req.setPais("Francia");

        // simulamos el guardado final en repositorio, devolviendo la entidad ya actualizada
        Equipo updated = new Equipo();
        updated.setId(8L);
        updated.setNombre("PSG");
        updated.setLiga("Ligue 1");
        updated.setPais("Francia");

        given(equipoRepo.save(exist)).willReturn(updated);

        // cuando
        EquipoResponse resultado = equipoService.actualizar(8L, req);

        // entonces
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(8L);
        assertThat(resultado.getNombre()).isEqualTo("PSG");
        assertThat(resultado.getLiga()).isEqualTo("Ligue 1");
        assertThat(resultado.getPais()).isEqualTo("Francia");

        // Verificamos que la entidad “exist” se modificó antes de llamar a save():
        assertThat(exist.getNombre()).isEqualTo("PSG");
        assertThat(exist.getLiga()).isEqualTo("Ligue 1");
        assertThat(exist.getPais()).isEqualTo("Francia");

        then(equipoRepo).should().findById(8L);
        then(equipoRepo).should().save(exist);
    }

    // --- 6) eliminar(Long) ---
    @Test
    @DisplayName("eliminar -> lanza ResourceNotFoundException si no existe")
    void eliminar_noEncontrado() {
        given(equipoRepo.findById(55L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> equipoService.eliminar(55L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Equipo no encontrado con ID: 55");

        then(equipoRepo).should().findById(55L);
        then(equipoRepo).should(never()).delete(any());
    }

    @Test
    @DisplayName("eliminar -> borra entidad existente")
    void eliminar_existente() {
        Equipo exist = new Equipo();
        exist.setId(99L);
        exist.setNombre("Algun Equipo");
        exist.setLiga("Alguna Liga");
        exist.setPais("Algún País");

        given(equipoRepo.findById(99L)).willReturn(Optional.of(exist));
        willDoNothing().given(equipoRepo).delete(exist);

        // cuando (no esperamos excepción)
        equipoService.eliminar(99L);

        then(equipoRepo).should().findById(99L);
        then(equipoRepo).should().delete(exist);
    }
}
