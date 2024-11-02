package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.*;
import edu.mtisw.payrollbackend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    ComprobanteIngresosRepository comprobanteIngresosRepository;

    @Mock
    PrestamoRepository prestamoRepository;

    @Mock
    UsuarioPrestamoRepository usuarioPrestamoRepository;

    @Mock
    UsuarioComprobanteIngresosRepository usuarioComprobanteIngresosRepository;

    @BeforeEach
    void setUp() {
        // No es necesario abrir mocks manualmente con @ExtendWith
    }

    @Test
    void whenSimularCredito_thenReturnSimulationResults() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdUsuario(idUsuario);
        usuarioPrestamo.setIdPrestamo(1L);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setMonto(100000000);
        prestamo.setPlazo(20);
        prestamo.setTasaInteres(5.0);

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        Map<String, Object> resultado = usuarioService.simularCredito(idUsuario);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.get("monto")).isEqualTo(100000000);
        assertThat(resultado.get("plazo")).isEqualTo(20);
        assertThat(resultado.get("tasaInteres")).isEqualTo(5.0);
        assertThat(resultado.get("pagoMensual")).isNotNull();
        assertThat(resultado.get("interesesTotales")).isNotNull();
        assertThat(resultado.get("totalPagos")).isNotNull();
    }

    @Test
    void whenSolicitarCredito_thenReturnPrestamo() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setMonto(100000000);
        prestamo.setPlazo(20);
        prestamo.setTasaInteres(5.0);
        prestamo.setTipo("primera vivienda");

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresoMensual(5000000);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.empty());
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.empty());
        when(comprobanteIngresosRepository.save(any(ComprobanteIngresosEntity.class))).thenReturn(comprobanteIngresos);
        when(prestamoRepository.save(any(PrestamoEntity.class))).thenReturn(prestamo);

        // When
        PrestamoEntity resultado = usuarioService.solicitarCredito(idUsuario, prestamo, comprobanteIngresos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getMonto()).isEqualTo(100000000);
        assertThat(resultado.getEstado()).isEqualTo("En proceso");
    }

    @Test
    void whenObtenerEstadoSolicitud_thenReturnPrestamo() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdUsuario(idUsuario);
        usuarioPrestamo.setIdPrestamo(1L);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setEstado("Aprobado");

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        PrestamoEntity resultado = usuarioService.obtenerEstadoSolicitud(idUsuario);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("Aprobado");
    }
}
