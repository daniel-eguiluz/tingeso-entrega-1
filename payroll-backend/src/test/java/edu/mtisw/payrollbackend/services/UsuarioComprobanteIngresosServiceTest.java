package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.UsuarioComprobanteIngresosEntity;
import edu.mtisw.payrollbackend.repositories.UsuarioComprobanteIngresosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioComprobanteIngresosServiceTest {

    @InjectMocks
    UsuarioComprobanteIngresosService usuarioComprobanteIngresosService;

    @Mock
    UsuarioComprobanteIngresosRepository usuarioComprobanteIngresosRepository;

    @BeforeEach
    void setUp() {
        // No es necesario inicializar los mocks manualmente con @ExtendWith
    }

    @Test
    void whenGetUsuariosComprobanteIngresos_thenReturnListOfUsuariosComprobanteIngresos() {
        // Given
        ArrayList<UsuarioComprobanteIngresosEntity> usuariosComprobante = new ArrayList<>();
        UsuarioComprobanteIngresosEntity usuario1 = new UsuarioComprobanteIngresosEntity(
                1L, // id
                1L, // idUsuario
                1L  // idComprobanteIngresos
        );
        UsuarioComprobanteIngresosEntity usuario2 = new UsuarioComprobanteIngresosEntity(
                2L,
                2L,
                2L
        );
        usuariosComprobante.add(usuario1);
        usuariosComprobante.add(usuario2);

        when(usuarioComprobanteIngresosRepository.findAll()).thenReturn(usuariosComprobante);

        // When
        ArrayList<UsuarioComprobanteIngresosEntity> resultado = usuarioComprobanteIngresosService.getUsuariosComprobanteIngresos();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(2);
        assertThat(resultado).contains(usuario1, usuario2);
        verify(usuarioComprobanteIngresosRepository, times(1)).findAll();
    }

    @Test
    void whenGetUsuarioComprobanteIngresosById_thenReturnUsuarioComprobanteIngresos() {
        // Given
        Long id = 1L;
        UsuarioComprobanteIngresosEntity usuarioComprobante = new UsuarioComprobanteIngresosEntity(
                id,
                1L,
                1L
        );

        when(usuarioComprobanteIngresosRepository.findById(id)).thenReturn(Optional.of(usuarioComprobante));

        // When
        UsuarioComprobanteIngresosEntity resultado = usuarioComprobanteIngresosService.getUsuarioComprobanteIngresosById(id);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.getIdComprobanteIngresos()).isEqualTo(1L);
        verify(usuarioComprobanteIngresosRepository, times(1)).findById(id);
    }

    @Test
    void whenSaveUsuarioComprobanteIngresos_thenReturnSavedUsuarioComprobanteIngresos() {
        // Given
        UsuarioComprobanteIngresosEntity usuarioComprobante = new UsuarioComprobanteIngresosEntity(
                null,
                3L,
                3L
        );
        UsuarioComprobanteIngresosEntity usuarioComprobanteGuardado = new UsuarioComprobanteIngresosEntity(
                3L,
                3L,
                3L
        );

        when(usuarioComprobanteIngresosRepository.save(usuarioComprobante)).thenReturn(usuarioComprobanteGuardado);

        // When
        UsuarioComprobanteIngresosEntity resultado = usuarioComprobanteIngresosService.saveUsuarioComprobanteIngresos(usuarioComprobante);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getIdUsuario()).isEqualTo(3L);
        assertThat(resultado.getIdComprobanteIngresos()).isEqualTo(3L);
        verify(usuarioComprobanteIngresosRepository, times(1)).save(usuarioComprobante);
    }

    @Test
    void whenUpdateUsuarioComprobanteIngresos_thenReturnUpdatedUsuarioComprobanteIngresos() {
        // Given
        Long id = 1L;
        UsuarioComprobanteIngresosEntity usuarioComprobanteActualizado = new UsuarioComprobanteIngresosEntity(
                id,
                1L,
                2L // Cambio en idComprobanteIngresos
        );

        when(usuarioComprobanteIngresosRepository.save(usuarioComprobanteActualizado)).thenReturn(usuarioComprobanteActualizado);

        // When
        UsuarioComprobanteIngresosEntity resultado = usuarioComprobanteIngresosService.updateUsuarioComprobanteIngresos(usuarioComprobanteActualizado);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.getIdComprobanteIngresos()).isEqualTo(2L);
        verify(usuarioComprobanteIngresosRepository, times(1)).save(usuarioComprobanteActualizado);
    }



    @Test
    void whenDeleteUsuarioComprobanteIngresos_thenReturnTrue() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(usuarioComprobanteIngresosRepository).deleteById(id);

        // When
        boolean resultado = usuarioComprobanteIngresosService.deleteUsuarioComprobanteIngresos(id);

        // Then
        assertThat(resultado).isTrue();
        verify(usuarioComprobanteIngresosRepository, times(1)).deleteById(id);
    }

    @Test
    void whenDeleteUsuarioComprobanteIngresosWithException_thenThrowException() {
        // Given
        Long id = 1L;
        doThrow(new RuntimeException("Error al eliminar")).when(usuarioComprobanteIngresosRepository).deleteById(id);

        // When / Then
        try {
            usuarioComprobanteIngresosService.deleteUsuarioComprobanteIngresos(id);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Error al eliminar");
        }
        verify(usuarioComprobanteIngresosRepository, times(1)).deleteById(id);
    }
}
