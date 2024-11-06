package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.ComprobanteIngresosEntity;
import edu.mtisw.payrollbackend.repositories.ComprobanteIngresosRepository;
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
class ComprobanteIngresosServiceTest {

    @InjectMocks
    ComprobanteIngresosService comprobanteIngresosService;

    @Mock
    ComprobanteIngresosRepository comprobanteIngresosRepository;

    @BeforeEach
    void setUp() {
        // No es necesario inicializar los mocks manualmente con @ExtendWith
    }

    @Test
    void whenGetComprobanteIngresos_thenReturnListOfComprobantes() {
        // Given
        ArrayList<ComprobanteIngresosEntity> comprobantes = new ArrayList<>();
        ComprobanteIngresosEntity comprobante1 = new ComprobanteIngresosEntity(
                1L, // id
                5, // antiguedadLaboral
                800000, // ingresoMensual
                "850000,820000,840000,830000,860000,870000,880000,900000,890000,860000,870000,890000,850000,820000,840000,830000,860000,870000,880000,900000,890000,860000,870000,890000", // ingresosUltimos24Meses
                1500000, // saldo
                2000000, // deudas
                3, // cantidadDeudasPendientes
                "1500000,1550000,1600000,1580000,1520000,1500000,1490000,1485000,1505000,1525000,1540000,1500000", // saldosMensuales
                "200000,220000,230000,210000,200000,220000,210000,200000,210000,200000,220000,230000", // depositosUltimos12Meses
                10, // antiguedadCuenta
                "50000,45000,60000,55000,70000,65000" // retirosUltimos6Meses
        );
        ComprobanteIngresosEntity comprobante2 = new ComprobanteIngresosEntity(
                2L,
                3,
                900000,
                "920000,910000,900000,890000,880000,870000,860000,850000,840000,830000,820000,810000,920000,910000,900000,890000,880000,870000,860000,850000,840000,830000,820000,810000",
                1800000,
                2500000,
                5,
                "1800000,1820000,1850000,1845000,1830000,1815000,1795000,1800000,1805000,1810000,1825000,1835000",
                "250000,270000,260000,280000,275000,265000,250000,260000,255000,265000,270000,275000",
                7,
                "40000,35000,30000,45000,40000,38000"
        );
        comprobantes.add(comprobante1);
        comprobantes.add(comprobante2);

        when(comprobanteIngresosRepository.findAll()).thenReturn(comprobantes);

        // When
        ArrayList<ComprobanteIngresosEntity> resultado = comprobanteIngresosService.getComprobanteIngresos();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(2);
        assertThat(resultado).contains(comprobante1, comprobante2);
        verify(comprobanteIngresosRepository, times(1)).findAll();
    }

    @Test
    void whenGetComprobanteIngresosById_thenReturnComprobante() {
        // Given
        Long id = 1L;
        ComprobanteIngresosEntity comprobante = new ComprobanteIngresosEntity(
                id,
                5,
                800000,
                "850000,820000,840000,830000,860000,870000,880000,900000,890000,860000,870000,890000,850000,820000,840000,830000,860000,870000,880000,900000,890000,860000,870000,890000",
                1500000,
                2000000,
                3,
                "1500000,1550000,1600000,1580000,1520000,1500000,1490000,1485000,1505000,1525000,1540000,1500000",
                "200000,220000,230000,210000,200000,220000,210000,200000,210000,200000,220000,230000",
                10,
                "50000,45000,60000,55000,70000,65000"
        );

        when(comprobanteIngresosRepository.findById(id)).thenReturn(Optional.of(comprobante));

        // When
        ComprobanteIngresosEntity resultado = comprobanteIngresosService.getComprobanteIngresosById(id);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getAntiguedadLaboral()).isEqualTo(5);
        assertThat(resultado.getIngresoMensual()).isEqualTo(800000);
        assertThat(resultado.getIngresosUltimos24Meses()).isEqualTo("850000,820000,840000,830000,860000,870000,880000,900000,890000,860000,870000,890000,850000,820000,840000,830000,860000,870000,880000,900000,890000,860000,870000,890000");
        verify(comprobanteIngresosRepository, times(1)).findById(id);
    }

    @Test
    void whenSaveComprobanteIngresos_thenReturnSavedComprobante() {
        // Given
        ComprobanteIngresosEntity comprobante = new ComprobanteIngresosEntity(
                null,
                4,
                750000,
                "800000,790000,780000,770000,760000,750000,740000,730000,720000,710000,700000,690000,800000,790000,780000,770000,760000,750000,740000,730000,720000,710000,700000,690000",
                1400000,
                1900000,
                2,
                "1400000,1450000,1500000,1470000,1420000,1400000,1390000,1385000,1405000,1425000,1440000,1400000",
                "190000,210000,220000,200000,190000,210000,200000,190000,200000,190000,210000,220000",
                8,
                "45000,40000,35000,50000,45000,43000"
        );
        ComprobanteIngresosEntity comprobanteGuardado = new ComprobanteIngresosEntity(
                3L,
                4,
                750000,
                "800000,790000,780000,770000,760000,750000,740000,730000,720000,710000,700000,690000,800000,790000,780000,770000,760000,750000,740000,730000,720000,710000,700000,690000",
                1400000,
                1900000,
                2,
                "1400000,1450000,1500000,1470000,1420000,1400000,1390000,1385000,1405000,1425000,1440000,1400000",
                "190000,210000,220000,200000,190000,210000,200000,190000,200000,190000,210000,220000",
                8,
                "45000,40000,35000,50000,45000,43000"
        );

        when(comprobanteIngresosRepository.save(comprobante)).thenReturn(comprobanteGuardado);

        // When
        ComprobanteIngresosEntity resultado = comprobanteIngresosService.saveComprobanteIngresos(comprobante);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getAntiguedadLaboral()).isEqualTo(4);
        assertThat(resultado.getIngresoMensual()).isEqualTo(750000);
        assertThat(resultado.getIngresosUltimos24Meses()).isEqualTo("800000,790000,780000,770000,760000,750000,740000,730000,720000,710000,700000,690000,800000,790000,780000,770000,760000,750000,740000,730000,720000,710000,700000,690000");
        verify(comprobanteIngresosRepository, times(1)).save(comprobante);
    }

    @Test
    void whenUpdateComprobanteIngresos_thenReturnUpdatedComprobante() {
        // Given
        Long id = 1L;
        ComprobanteIngresosEntity comprobanteActualizado = new ComprobanteIngresosEntity(
                id,
                6,
                850000,
                "860000,830000,850000,840000,870000,880000,890000,910000,900000,870000,880000,900000,860000,830000,850000,840000,870000,880000,890000,910000,900000,870000,880000,900000",
                1600000,
                2100000,
                4,
                "1600000,1650000,1700000,1680000,1620000,1600000,1590000,1585000,1605000,1625000,1640000,1600000",
                "210000,230000,240000,220000,210000,230000,220000,210000,220000,210000,230000,240000",
                11,
                "55000,50000,45000,60000,55000,53000"
        );

        when(comprobanteIngresosRepository.save(comprobanteActualizado)).thenReturn(comprobanteActualizado);

        // When
        ComprobanteIngresosEntity resultado = comprobanteIngresosService.updateComprobanteIngresos(comprobanteActualizado);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getAntiguedadLaboral()).isEqualTo(6);
        assertThat(resultado.getIngresoMensual()).isEqualTo(850000);
        assertThat(resultado.getIngresosUltimos24Meses()).isEqualTo("860000,830000,850000,840000,870000,880000,890000,910000,900000,870000,880000,900000,860000,830000,850000,840000,870000,880000,890000,910000,900000,870000,880000,900000");
        verify(comprobanteIngresosRepository, times(1)).save(comprobanteActualizado);
    }


    @Test
    void whenDeleteComprobanteIngresos_thenReturnTrue() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(comprobanteIngresosRepository).deleteById(id);

        // When
        boolean resultado = comprobanteIngresosService.deleteComprobanteIngresos(id);

        // Then
        assertThat(resultado).isTrue();
        verify(comprobanteIngresosRepository, times(1)).deleteById(id);
    }

    @Test
    void whenDeleteComprobanteIngresosWithException_thenThrowException() {
        // Given
        Long id = 1L;
        doThrow(new RuntimeException("Error al eliminar")).when(comprobanteIngresosRepository).deleteById(id);

        // When / Then
        try {
            comprobanteIngresosService.deleteComprobanteIngresos(id);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Error al eliminar");
        }
        verify(comprobanteIngresosRepository, times(1)).deleteById(id);
    }
}
