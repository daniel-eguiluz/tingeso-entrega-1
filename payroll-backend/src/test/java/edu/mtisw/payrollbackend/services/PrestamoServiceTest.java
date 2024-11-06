package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.PrestamoEntity;
import edu.mtisw.payrollbackend.repositories.PrestamoRepository;
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
class PrestamoServiceTest {

    @InjectMocks
    PrestamoService prestamoService;

    @Mock
    PrestamoRepository prestamoRepository;

    @BeforeEach
    void setUp() {
        // No es necesario inicializar los mocks manualmente con @ExtendWith
    }

    @Test
    void whenGetPrestamos_thenReturnListOfPrestamos() {
        // Given
        ArrayList<PrestamoEntity> prestamos = new ArrayList<>();
        PrestamoEntity prestamo1 = new PrestamoEntity(
                1L, // id
                "Primera vivienda", // tipo
                30, // plazo
                4.5, // tasaInteres
                100000000, // monto
                "Aprobado", // estado
                120000000 // valorPropiedad
        );
        PrestamoEntity prestamo2 = new PrestamoEntity(
                2L,
                "Segunda vivienda",
                20,
                5.0,
                70000000,
                "En proceso",
                85000000
        );
        PrestamoEntity prestamo3 = new PrestamoEntity(
                3L,
                "Propiedades comerciales",
                25,
                6.5,
                80000000,
                "Rechazado",
                100000000
        );
        PrestamoEntity prestamo4 = new PrestamoEntity(
                4L,
                "Remodelacion",
                15,
                5.5,
                50000000,
                "Aprobado",
                60000000
        );
        prestamos.add(prestamo1);
        prestamos.add(prestamo2);
        prestamos.add(prestamo3);
        prestamos.add(prestamo4);

        when(prestamoRepository.findAll()).thenReturn(prestamos);

        // When
        ArrayList<PrestamoEntity> resultado = prestamoService.getPrestamos();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(4);
        assertThat(resultado).contains(prestamo1, prestamo2, prestamo3, prestamo4);
        verify(prestamoRepository, times(1)).findAll();
    }

    @Test
    void whenGetPrestamoById_thenReturnPrestamo() {
        // Given
        Long id = 1L;
        PrestamoEntity prestamo = new PrestamoEntity(
                id,
                "Primera vivienda",
                30,
                4.5,
                100000000,
                "Aprobado",
                120000000
        );

        when(prestamoRepository.findById(id)).thenReturn(Optional.of(prestamo));

        // When
        PrestamoEntity resultado = prestamoService.getPrestamoById(id);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getTipo()).isEqualTo("Primera vivienda");
        assertThat(resultado.getPlazo()).isEqualTo(30);
        assertThat(resultado.getTasaInteres()).isEqualTo(4.5);
        assertThat(resultado.getMonto()).isEqualTo(100000000);
        assertThat(resultado.getEstado()).isEqualTo("Aprobado");
        assertThat(resultado.getValorPropiedad()).isEqualTo(120000000);
        verify(prestamoRepository, times(1)).findById(id);
    }

    @Test
    void whenSavePrestamo_thenReturnSavedPrestamo() {
        // Given
        PrestamoEntity prestamo = new PrestamoEntity(
                null,
                "Propiedades comerciales",
                25,
                6.5,
                80000000,
                "Rechazado",
                100000000
        );
        PrestamoEntity prestamoGuardado = new PrestamoEntity(
                3L,
                "Propiedades comerciales",
                25,
                6.5,
                80000000,
                "Rechazado",
                100000000
        );

        when(prestamoRepository.save(prestamo)).thenReturn(prestamoGuardado);

        // When
        PrestamoEntity resultado = prestamoService.savePrestamo(prestamo);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getTipo()).isEqualTo("Propiedades comerciales");
        assertThat(resultado.getPlazo()).isEqualTo(25);
        assertThat(resultado.getTasaInteres()).isEqualTo(6.5);
        assertThat(resultado.getMonto()).isEqualTo(80000000);
        assertThat(resultado.getEstado()).isEqualTo("Rechazado");
        assertThat(resultado.getValorPropiedad()).isEqualTo(100000000);
        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    void whenUpdatePrestamo_thenReturnUpdatedPrestamo() {
        // Given
        Long id = 1L;
        PrestamoEntity prestamoActualizado = new PrestamoEntity(
                id,
                "Primera vivienda",
                35, // Cambio en el plazo
                4.5,
                100000000,
                "Aprobado",
                120000000
        );

        when(prestamoRepository.save(prestamoActualizado)).thenReturn(prestamoActualizado);

        // When
        PrestamoEntity resultado = prestamoService.updatePrestamo(prestamoActualizado);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getPlazo()).isEqualTo(35); // Verificar el cambio
        verify(prestamoRepository, times(1)).save(prestamoActualizado);
    }


    @Test
    void whenDeletePrestamo_thenReturnTrue() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(prestamoRepository).deleteById(id);

        // When
        boolean resultado = prestamoService.deletePrestamo(id);

        // Then
        assertThat(resultado).isTrue();
        verify(prestamoRepository, times(1)).deleteById(id);
    }

    @Test
    void whenDeletePrestamoWithException_thenThrowException() {
        // Given
        Long id = 1L;
        doThrow(new RuntimeException("Error al eliminar")).when(prestamoRepository).deleteById(id);

        // When / Then
        try {
            prestamoService.deletePrestamo(id);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Error al eliminar");
        }
        verify(prestamoRepository, times(1)).deleteById(id);
    }
}
