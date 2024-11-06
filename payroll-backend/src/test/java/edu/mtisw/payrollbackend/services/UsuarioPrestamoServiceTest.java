package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.UsuarioPrestamoEntity;
import edu.mtisw.payrollbackend.repositories.UsuarioPrestamoRepository;
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
class UsuarioPrestamoServiceTest {

    @InjectMocks
    UsuarioPrestamoService usuarioPrestamoService;

    @Mock
    UsuarioPrestamoRepository usuarioPrestamoRepository;

    @BeforeEach
    void setUp() {
        // No es necesario inicializar los mocks manualmente con @ExtendWith
    }

    @Test
    void whenGetUsuariosPrestamos_thenReturnListOfUsuariosPrestamos() {
        // Given
        ArrayList<UsuarioPrestamoEntity> usuariosPrestamos = new ArrayList<>();
        UsuarioPrestamoEntity usuarioPrestamo1 = new UsuarioPrestamoEntity(
                1L, // id
                1L, // idUsuario
                1L  // idPrestamo
        );
        UsuarioPrestamoEntity usuarioPrestamo2 = new UsuarioPrestamoEntity(
                2L,
                2L,
                2L
        );
        UsuarioPrestamoEntity usuarioPrestamo3 = new UsuarioPrestamoEntity(
                3L,
                3L,
                3L
        );
        usuariosPrestamos.add(usuarioPrestamo1);
        usuariosPrestamos.add(usuarioPrestamo2);
        usuariosPrestamos.add(usuarioPrestamo3);

        when(usuarioPrestamoRepository.findAll()).thenReturn(usuariosPrestamos);

        // When
        ArrayList<UsuarioPrestamoEntity> resultado = usuarioPrestamoService.getUsuariosPrestamos();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(3);
        assertThat(resultado).contains(usuarioPrestamo1, usuarioPrestamo2, usuarioPrestamo3);
        verify(usuarioPrestamoRepository, times(1)).findAll();
    }

    @Test
    void whenGetUsuarioPrestamoById_thenReturnUsuarioPrestamo() {
        // Given
        Long id = 1L;
        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity(
                id,
                1L,
                1L
        );

        when(usuarioPrestamoRepository.findById(id)).thenReturn(Optional.of(usuarioPrestamo));

        // When
        UsuarioPrestamoEntity resultado = usuarioPrestamoService.getUsuarioPrestamoById(id);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.getIdPrestamo()).isEqualTo(1L);
        verify(usuarioPrestamoRepository, times(1)).findById(id);
    }

    @Test
    void whenSaveUsuarioPrestamo_thenReturnSavedUsuarioPrestamo() {
        // Given
        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity(
                null,
                3L,
                3L
        );
        UsuarioPrestamoEntity usuarioPrestamoGuardado = new UsuarioPrestamoEntity(
                3L,
                3L,
                3L
        );

        when(usuarioPrestamoRepository.save(usuarioPrestamo)).thenReturn(usuarioPrestamoGuardado);

        // When
        UsuarioPrestamoEntity resultado = usuarioPrestamoService.saveUsuarioPrestamo(usuarioPrestamo);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getIdUsuario()).isEqualTo(3L);
        assertThat(resultado.getIdPrestamo()).isEqualTo(3L);
        verify(usuarioPrestamoRepository, times(1)).save(usuarioPrestamo);
    }

    @Test
    void whenUpdateUsuarioPrestamo_thenReturnUpdatedUsuarioPrestamo() {
        // Given
        Long id = 1L;
        UsuarioPrestamoEntity usuarioPrestamoActualizado = new UsuarioPrestamoEntity(
                id,
                1L,
                2L // Cambio en idPrestamo
        );

        when(usuarioPrestamoRepository.save(usuarioPrestamoActualizado)).thenReturn(usuarioPrestamoActualizado);

        // When
        UsuarioPrestamoEntity resultado = usuarioPrestamoService.updateUsuarioPrestamo(usuarioPrestamoActualizado);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.getIdPrestamo()).isEqualTo(2L); // Verificar el cambio
        verify(usuarioPrestamoRepository, times(1)).save(usuarioPrestamoActualizado);
    }


    @Test
    void whenDeleteUsuarioPrestamo_thenReturnTrue() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(usuarioPrestamoRepository).deleteById(id);

        // When
        boolean resultado = usuarioPrestamoService.deleteUsuarioPrestamo(id);

        // Then
        assertThat(resultado).isTrue();
        verify(usuarioPrestamoRepository, times(1)).deleteById(id);
    }

    @Test
    void whenDeleteUsuarioPrestamoWithException_thenThrowException() {
        // Given
        Long id = 1L;
        doThrow(new RuntimeException("Error al eliminar")).when(usuarioPrestamoRepository).deleteById(id);

        // When / Then
        try {
            usuarioPrestamoService.deleteUsuarioPrestamo(id);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Error al eliminar");
        }
        verify(usuarioPrestamoRepository, times(1)).deleteById(id);
    }
}
