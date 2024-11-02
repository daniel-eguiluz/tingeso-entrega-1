package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.*;
import edu.mtisw.payrollbackend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BancoServiceTest {

    @Spy
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

    @BeforeEach
    void setUp() {
        // No es necesario abrir mocks manualmente con @ExtendWith
    }

    @Test
    void whenEvaluarRelacionCuotaIngreso_thenReturnTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setEdad(30);
        usuario.setTipoEmpleado("Empleado");

        UsuarioComprobanteIngresosEntity usuarioComprobante = new UsuarioComprobanteIngresosEntity();
        usuarioComprobante.setIdUsuario(idUsuario);
        usuarioComprobante.setIdComprobanteIngresos(1L);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setId(1L);
        comprobanteIngresos.setIngresoMensual(5000000);

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdUsuario(idUsuario);
        usuarioPrestamo.setIdPrestamo(1L);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setMonto(100000000);
        prestamo.setPlazo(20);
        prestamo.setTasaInteres(5.0);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobante));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        boolean resultado = bancoService.evaluarRelacionCuotaIngreso(idUsuario);

        // Then
        assertThat(resultado).isTrue();
    }

    @Test
    void whenEvaluarHistorialCrediticio_thenReturnFalse() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioComprobanteIngresosEntity usuarioComprobante = new UsuarioComprobanteIngresosEntity();
        usuarioComprobante.setIdUsuario(idUsuario);
        usuarioComprobante.setIdComprobanteIngresos(1L);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setId(1L);
        comprobanteIngresos.setCantidadDeudasPendientes(4); // Más de 3 deudas pendientes
        comprobanteIngresos.setDeudas(2000000);
        comprobanteIngresos.setIngresoMensual(5000000);

        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobante));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarHistorialCrediticio(idUsuario);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    void whenEvaluarAntiguedadEmpleado_thenReturnTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setTipoEmpleado("Empleado");

        UsuarioComprobanteIngresosEntity usuarioComprobante = new UsuarioComprobanteIngresosEntity();
        usuarioComprobante.setIdUsuario(idUsuario);
        usuarioComprobante.setIdComprobanteIngresos(1L);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setId(1L);
        comprobanteIngresos.setAntiguedadLaboral(2); // Más de 1 año

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobante));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // When
        boolean resultado = bancoService.evaluarAntiguedad(idUsuario);

        // Then
        assertThat(resultado).isTrue();
    }

    @Test
    void whenEvaluarRelacionDeudaIngreso_thenReturnFalse() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        UsuarioComprobanteIngresosEntity usuarioComprobante = new UsuarioComprobanteIngresosEntity();
        usuarioComprobante.setIdUsuario(idUsuario);
        usuarioComprobante.setIdComprobanteIngresos(1L);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setId(1L);
        comprobanteIngresos.setIngresoMensual(5000000);
        comprobanteIngresos.setDeudas(2000000);

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdUsuario(idUsuario);
        usuarioPrestamo.setIdPrestamo(1L);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setMonto(100000000);
        prestamo.setPlazo(30);
        prestamo.setTasaInteres(5.0);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobante));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        boolean resultado = bancoService.evaluarRelacionDeudaIngreso(idUsuario);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    void whenEvaluarEdad_thenReturnTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setEdad(40);

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdUsuario(idUsuario);
        usuarioPrestamo.setIdPrestamo(1L);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setPlazo(20);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));

        // When
        boolean resultado = bancoService.evaluarEdad(idUsuario);

        // Then
        assertThat(resultado).isTrue();
    }

    @Test
    void whenEvaluarCapacidadAhorro_thenReturnModerada() throws Exception {
        // Given
        Long idUsuario = 1L;

        // Simular métodos internos que evaluarCapacidadAhorro utiliza
        doReturn(true).when(bancoService).evaluarSaldoMinimo(idUsuario);
        doReturn(true).when(bancoService).evaluarHistorialAhorroConsistente(idUsuario);
        doReturn(false).when(bancoService).evaluarDepositosPeriodicos(idUsuario);
        doReturn(true).when(bancoService).evaluarRelacionSaldoAntiguedad(idUsuario);
        doReturn(false).when(bancoService).evaluarRetirosRecientes(idUsuario);

        // When
        Map<String, Object> resultado = bancoService.evaluarCapacidadAhorro(idUsuario);

        // Then
        assertThat(resultado.get("capacidadAhorro")).isEqualTo("moderada");
        assertThat(resultado.get("reglasCumplidas")).isEqualTo(3);
        assertThat(resultado.get("detalles")).isInstanceOf(Map.class);
        Map<String, Boolean> detalles = (Map<String, Boolean>) resultado.get("detalles");
        assertThat(detalles).containsEntry("R71", true)
                .containsEntry("R72", true)
                .containsEntry("R73", false)
                .containsEntry("R74", true)
                .containsEntry("R75", false);
    }

    /*
    @Test
    void whenEvaluarCredito_thenAprobadoIsTrue() throws Exception {
        // Given
        Long idUsuario = 1L;

        // Configuración de los mocks para las llamadas al repositorio
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);
        usuario.setEdad(40);
        usuario.setTipoEmpleado("Empleado");

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdUsuario(idUsuario);
        usuarioPrestamo.setIdPrestamo(1L);

        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setId(1L);
        prestamo.setMonto(100000000);
        prestamo.setPlazo(20);
        prestamo.setTasaInteres(5.0);
        prestamo.setTipo("primera vivienda");
        prestamo.setValorPropiedad(125000000); // Para R5: 80% de 125M = 100M

        UsuarioComprobanteIngresosEntity usuarioComprobante = new UsuarioComprobanteIngresosEntity();
        usuarioComprobante.setIdUsuario(idUsuario);
        usuarioComprobante.setIdComprobanteIngresos(1L);

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setId(1L);
        comprobanteIngresos.setIngresoMensual(5000000);
        comprobanteIngresos.setCantidadDeudasPendientes(2);
        comprobanteIngresos.setDeudas(1000000);
        comprobanteIngresos.setSaldo(20000000);
        comprobanteIngresos.setAntiguedadLaboral(5);
        comprobanteIngresos.setIngresosUltimos24Meses("5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000,5000000");
        comprobanteIngresos.setSaldosMensuales("20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000,20000000");
        comprobanteIngresos.setDepositosUltimos12Meses("1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000,1000000");
        comprobanteIngresos.setRetirosUltimos6Meses("500000,500000,500000,500000,500000,500000");
        comprobanteIngresos.setAntiguedadCuenta(3);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo));
        when(usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioComprobante));
        when(comprobanteIngresosRepository.findById(1L)).thenReturn(Optional.of(comprobanteIngresos));

        // Simulación de los métodos evaluados internamente
        doReturn(true).when(bancoService).evaluarRelacionCuotaIngreso(idUsuario);
        doReturn(true).when(bancoService).evaluarHistorialCrediticio(idUsuario);
        doReturn(true).when(bancoService).evaluarAntiguedad(idUsuario);
        doReturn(true).when(bancoService).evaluarRelacionDeudaIngreso(idUsuario);
        doReturn(true).when(bancoService).evaluarMontoMaximoFinanciamiento(idUsuario);
        doReturn(true).when(bancoService).evaluarEdad(idUsuario);

        // Simular evaluarCapacidadAhorro si es necesario
        doReturn(true).when(bancoService).evaluarCapacidadAhorro(idUsuario);

        // When
        Map<String, Object> resultado = bancoService.evaluarCredito(idUsuario);

        // Then
        assertThat(resultado.get("aprobado")).isEqualTo(true);
        assertThat(resultado.get("capacidadAhorro")).isEqualTo("sólida");
        assertThat(resultado.get("detallesAhorro")).isInstanceOf(Map.class);

        // Verificar que evaluarCapacidadAhorro fue llamado
        verify(bancoService).evaluarCapacidadAhorro(idUsuario);
    }
    */
}
