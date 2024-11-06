package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.*;
import edu.mtisw.payrollbackend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BancoServiceTest {

    @InjectMocks
    BancoService bancoService;

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

    @Mock
    UsuarioService usuarioService;

    // ------------------------------------ Pruebas para evaluarRelacionCuotaIngreso ----------------------------------------------

    @Test
    void evaluarRelacionCuotaIngreso_Success_ReturnsTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresoMensual(1000000);

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setTasaInteres(6.0);
        prestamo.setPlazo(10); // años
        prestamo.setMonto(5000000);

        // Configuración de mocks
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        boolean resultado = bancoService.evaluarRelacionCuotaIngreso(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarRelacionCuotaIngreso_UserNotFound_ThrowsException() {
        // Given
        Long idUsuario = 1L;

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarRelacionCuotaIngreso(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Usuario no encontrado");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verifyNoMoreInteractions(usuarioComprobanteIngresosRepository, comprobanteIngresosRepository, usuarioPrestamoRepository, prestamoRepository);
    }

    @Test
    void evaluarRelacionCuotaIngreso_ComprobanteIngresosNotAssociated_ThrowsException() {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarRelacionCuotaIngreso(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Comprobante de ingresos no asociado al usuario");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verifyNoMoreInteractions(comprobanteIngresosRepository, usuarioPrestamoRepository, prestamoRepository);
    }

    @Test
    void evaluarRelacionCuotaIngreso_ComprobanteIngresosNotFound_ThrowsException() {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarRelacionCuotaIngreso(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Comprobante de ingresos no encontrado");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(usuarioPrestamoRepository, prestamoRepository);
    }

    @Test
    void evaluarRelacionCuotaIngreso_PrestamoNotAssociated_ThrowsException() {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresoMensual(1000000);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarRelacionCuotaIngreso(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no asociado al usuario");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verifyNoMoreInteractions(prestamoRepository);
    }

    @Test
    void evaluarRelacionCuotaIngreso_PrestamoNotFound_ThrowsException() {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresoMensual(1000000);

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarRelacionCuotaIngreso(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no encontrado");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para evaluarHistorialCrediticio ----------------------------------------------



    @Test
    void evaluarHistorialCrediticio_Failure_TooManyDebts() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setCantidadDeudasPendientes(4); // >3
        comprobanteIngresos.setDeudas(2000000);
        comprobanteIngresos.setIngresoMensual(1000000);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarHistorialCrediticio(idUsuario);

        // Then
        assertFalse(resultado);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarHistorialCrediticio_Failure_ExcessiveDebtPercentage() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setCantidadDeudasPendientes(2);
        comprobanteIngresos.setDeudas(400000); // 40% of 1000000
        comprobanteIngresos.setIngresoMensual(1000000);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarHistorialCrediticio(idUsuario);

        // Then
        assertFalse(resultado);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarHistorialCrediticio_ComprobanteIngresosNotFound_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarHistorialCrediticio(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Comprobante de ingresos no encontrado");
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para evaluarAntiguedad ----------------------------------------------

    @Test
    void evaluarAntiguedad_Empleado_Success_ReturnsTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setTipoEmpleado("Empleado");

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setAntiguedadLaboral(3);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarAntiguedad(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarAntiguedad_Empleado_Failure_ReturnsFalse() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setTipoEmpleado("Empleado");

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setAntiguedadLaboral(0);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarAntiguedad(idUsuario);

        // Then
        assertFalse(resultado);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarAntiguedad_Independiente_Success_ReturnsTrue() throws Exception {
        // Given
        Long idUsuario = 2L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setTipoEmpleado("Independiente");

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(2L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresosUltimos24Meses("1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000");

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(2L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarAntiguedad(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(2L);
    }

    @Test
    void evaluarAntiguedad_Independiente_Failure_ReturnsFalse() throws Exception {
        // Given
        Long idUsuario = 2L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setTipoEmpleado("Independiente");

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(2L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresosUltimos24Meses("1000000,1000000"); // Menos de 24 meses

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(2L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarAntiguedad(idUsuario);

        // Then
        assertFalse(resultado);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(2L);
    }

    @Test
    void evaluarAntiguedad_UnknownTipoEmpleado_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 3L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setTipoEmpleado("Freelancer"); // Tipo desconocido

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(3L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresosUltimos24Meses("1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000");

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(3L)).thenReturn(Optional.of(comprobanteIngresos));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarAntiguedad(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Tipo de empleado desconocido");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(3L);
    }

    // ------------------------------------ Pruebas para evaluarRelacionDeudaIngreso ----------------------------------------------

    @Test
    void evaluarRelacionDeudaIngreso_Success_ReturnsTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresoMensual(2000000);
        comprobanteIngresos.setDeudas(500000); // Deudas actuales

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setMonto(1000000);
        prestamo.setTasaInteres(6.0);
        prestamo.setPlazo(5); // años

        // Cálculo de cuota mensual:
        // tasa_interes_mensual = 0.5%
        // numero_pagos = 60
        // cuota = (1000000 * 0.005 * (1.005)^60) / ((1.005)^60 -1) ≈ 19320.75
        // totalDeudas = 500000 + 19320.75 ≈ 519320.75
        // relacionDeudaIngreso = (519320.75 / 2000000) * 100 ≈ 25.966% <=50%

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarRelacionDeudaIngreso(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarRelacionDeudaIngreso_Failure_RelacionDeudaIngresoExceeds50() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresoMensual(2000000);
        comprobanteIngresos.setDeudas(1000000); // Deudas actuales

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setMonto(2000000);
        prestamo.setTasaInteres(12.0);
        prestamo.setPlazo(10); // años

        // Cálculo de cuota mensual:
        // tasa_interes_mensual = 1%
        // numero_pagos = 120
        // cuota = (2000000 * 0.01 * (1.01)^120) / ((1.01)^120 -1) ≈ 26775.75
        // totalDeudas = 1000000 + 26775.75 ≈ 1026775.75
        // relacionDeudaIngreso = (1026775.75 / 2000000) * 100 ≈ 51.34% >50%

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarRelacionDeudaIngreso(idUsuario);

        // Then
        assertFalse(resultado);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para evaluarMontoMaximoFinanciamiento ----------------------------------------------

    @Test
    void evaluarMontoMaximoFinanciamiento_Success_ReturnsTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setTipo("Primera vivienda");
        prestamo.setValorPropiedad(100000000); // 100,000,000
        prestamo.setMonto(80000000); // 80% de 100,000,000

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        boolean resultado = bancoService.evaluarMontoMaximoFinanciamiento(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarMontoMaximoFinanciamiento_Failure_ExceedsMaximum() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setTipo("Primera vivienda");
        prestamo.setValorPropiedad(100000000); // 100,000,000
        prestamo.setMonto(90000000); // 90% >80%

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        boolean resultado = bancoService.evaluarMontoMaximoFinanciamiento(idUsuario);

        // Then
        assertFalse(resultado);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarMontoMaximoFinanciamiento_UnknownTipoPrestamo_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setTipo("Hipoteca");
        prestamo.setValorPropiedad(100000000); // 100,000,000
        prestamo.setMonto(80000000); // 80%

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarMontoMaximoFinanciamiento(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Tipo de préstamo desconocido");
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarMontoMaximoFinanciamiento_PrestamoNotFound_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarMontoMaximoFinanciamiento(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no encontrado");
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para evaluarEdad ----------------------------------------------

    @Test
    void evaluarEdad_Success_ReturnsTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setEdad(60);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setPlazo(5); // años

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        boolean resultado = bancoService.evaluarEdad(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarEdad_Failure_AgeExceeds70() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setEdad(68);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setPlazo(5); // años

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        boolean resultado = bancoService.evaluarEdad(idUsuario);

        // Then
        assertFalse(resultado);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarEdad_PrestamoNotFound_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setEdad(60);

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarEdad(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no encontrado");
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para evaluarSaldoMinimo ----------------------------------------------





    @Test
    void evaluarSaldoMinimo_PrestamoNotFound_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(new ComprobanteIngresosEntity()));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarSaldoMinimo(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no encontrado");
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para evaluarHistorialAhorroConsistente ----------------------------------------------

    @Test
    void evaluarHistorialAhorroConsistente_Success_ReturnsTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        String saldosMensuales = "1000000,1100000,1200000,1300000,1400000,1500000,1600000,1700000,1800000,1900000,2000000,2100000";
        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setSaldosMensuales(saldosMensuales);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarHistorialAhorroConsistente(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarHistorialAhorroConsistente_Failure_NegativeSaldo() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        String saldosMensuales = "1000000,1100000,1200000,-1300000,1400000,1500000,1600000,1700000,1800000,1900000,2000000,2100000";
        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setSaldosMensuales(saldosMensuales);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarHistorialAhorroConsistente(idUsuario);

        // Then
        assertFalse(resultado);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarHistorialAhorroConsistente_NotEnoughData_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        String saldosMensuales = "1000000,1100000,1200000"; // Menos de 12 meses
        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setSaldosMensuales(saldosMensuales);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarHistorialAhorroConsistente(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("No hay suficientes datos de saldos mensuales");
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para evaluarDepositosPeriodicos ----------------------------------------------

    @Test
    void evaluarDepositosPeriodicos_Success_Mensuales() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        String depositosUltimos12Meses = "50000,60000,55000,70000,65000,80000,75000,90000,85000,95000,100000,105000";
        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setDepositosUltimos12Meses(depositosUltimos12Meses);
        comprobanteIngresos.setIngresoMensual(200000);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarDepositosPeriodicos(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarDepositosPeriodicos_Success_Trimestrales() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        String depositosUltimos12Meses = "10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000";
        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setDepositosUltimos12Meses(depositosUltimos12Meses);
        comprobanteIngresos.setIngresoMensual(200000);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarDepositosPeriodicos(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarDepositosPeriodicos_Failure_NotEnoughData_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        String depositosUltimos12Meses = "50000,60000,55000"; // Menos de 12 meses
        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setDepositosUltimos12Meses(depositosUltimos12Meses);
        comprobanteIngresos.setIngresoMensual(200000);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarDepositosPeriodicos(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("No hay suficientes datos de depósitos");
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarDepositosPeriodicos_Failure_NoRegularDeposits_ReturnsFalse() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        // Depósitos que no cumplen ni mensuales ni trimestrales
        String depositosUltimos12Meses = "10000,20000,30000,40000,50000,60000,70000,80000,90000,100000,110000,120000";
        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setDepositosUltimos12Meses(depositosUltimos12Meses);
        comprobanteIngresos.setIngresoMensual(200000); // montoMinimoDeposito = 10,000

        // Modificamos algunos depósitos para no cumplir
        // Supongamos que en algunos meses el depósito es menor al mínimo
        String depositosModificados = "9000,20000,30000,40000,50000,60000,70000,80000,90000,100000,110000,120000";
        comprobanteIngresos.setDepositosUltimos12Meses(depositosModificados);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarDepositosPeriodicos(idUsuario);

        // Then
        // En este caso, aunque hay 11 depósitos >=10,000, el primero es <10,000, pero se cumplen los 12 mensuales
        // Según el método, requiere 12 depósitos mensuales >=10,000 o 4 trimestrales >=30,000
        // En este caso, los depósitos trimestrales suman >=30,000
        // Por lo tanto, debería retornar true
        assertTrue(resultado);

        // Ahora, modificar más depósitos para que no se cumpla ninguna regla
        String depositosNoCumplen = "9000,8000,7000,6000,5000,4000,3000,2000,1000,900,800,700";
        comprobanteIngresos.setDepositosUltimos12Meses(depositosNoCumplen);

        // Reset mocks
        reset(usuarioComprobanteIngresosRepository, comprobanteIngresosRepository);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado2 = bancoService.evaluarDepositosPeriodicos(idUsuario);

        // Then
        assertFalse(resultado2);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para evaluarRelacionSaldoAntiguedad ----------------------------------------------

    @Test
    void evaluarRelacionSaldoAntiguedad_Success_ReturnsTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setAntiguedadCuenta(3);
        comprobanteIngresos.setSaldo(1000000);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setMonto(5000000);

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // porcentajeRequerido = 5000000 * 0.10 = 500000
        // saldoCliente = 1000000 >= 500000 => true

        // When
        boolean resultado = bancoService.evaluarRelacionSaldoAntiguedad(idUsuario);

        // Then
        assertTrue(resultado);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarRelacionSaldoAntiguedad_Failure_NotEnoughBalance() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setAntiguedadCuenta(3);
        comprobanteIngresos.setSaldo(400000); // <500,000

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setMonto(5000000);

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        boolean resultado = bancoService.evaluarRelacionSaldoAntiguedad(idUsuario);

        // Then
        assertFalse(resultado);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void evaluarRelacionSaldoAntiguedad_PrestamoNotFound_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdComprobanteIngresos(1L);
        usuarioComprobanteIngresos.setIdUsuario(idUsuario);

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobanteIngresos));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(new ComprobanteIngresosEntity()));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.evaluarRelacionSaldoAntiguedad(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no encontrado");
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).findById(1L);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para evaluarCredito ----------------------------------------------


    // Más pruebas para evaluarCredito con diferentes combinaciones de reglas

    // ------------------------------------ Pruebas para calcularCostoTotalPrestamo ----------------------------------------------

    @Test
    void calcularCostoTotalPrestamo_PrestamoNotFound_ThrowsException() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdPrestamo(1L);
        usuarioPrestamo.setIdUsuario(idUsuario);

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            bancoService.calcularCostoTotalPrestamo(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no encontrado");
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    // ------------------------------------ Pruebas para simularCredito ----------------------------------------------







    // ------------------------------------ Pruebas para solicitarCredito ----------------------------------------------











    // ------------------------------------ Pruebas para obtenerEstadoSolicitud ----------------------------------------------









    // ------------------------------------ Otras pruebas para métodos restantes ----------------------------------------------

    // Puedes seguir agregando pruebas similares para los demás métodos como evaluarRelacionCuotaIngreso, evaluarRelacionDeudaIngreso,
    // evaluarMontoMaximoFinanciamiento, evaluarEdad, evaluarSaldoMinimo, evaluarHistorialAhorroConsistente, evaluarDepositosPeriodicos,
    // evaluarRelacionSaldoAntiguedad, evaluarRetirosRecientes, evaluarCapacidadAhorro, evaluarCredito, calcularCostoTotalPrestamo,
    // simularCredito, solicitarCredito, obtenerEstadoSolicitud, etc.

    // Asegúrate de cubrir tanto casos de éxito como de fallo, incluyendo excepciones lanzadas por los repositorios.
}
