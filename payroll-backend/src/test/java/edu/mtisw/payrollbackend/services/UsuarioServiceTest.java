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
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    // ------------------------------------ Pruebas CRUD ----------------------------------------------

    // Pruebas para getUsuarios()
    @Test
    void whenGetUsuarios_thenReturnListOfUsuarios() {
        // Given
        ArrayList<UsuarioEntity> usuarios = new ArrayList<>();
        UsuarioEntity usuario1 = new UsuarioEntity();
        usuario1.setId(1L);
        usuario1.setNombre("Juan");
        usuarios.add(usuario1);

        UsuarioEntity usuario2 = new UsuarioEntity();
        usuario2.setId(2L);
        usuario2.setNombre("Ana");
        usuarios.add(usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // When
        ArrayList<UsuarioEntity> resultado = usuarioService.getUsuarios();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(2);
        assertThat(resultado).contains(usuario1, usuario2);
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void whenGetUsuariosAndNoUsuarios_thenReturnEmptyList() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        ArrayList<UsuarioEntity> resultado = usuarioService.getUsuarios();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
        verify(usuarioRepository, times(1)).findAll();
    }

    // Pruebas para getUsuarioById(Long id)
    @Test
    void whenGetUsuarioById_thenReturnUsuario() throws Exception {
        // Given
        Long id = 1L;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(id);
        usuario.setNombre("Juan");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // When
        UsuarioEntity resultado = usuarioService.getUsuarioById(id);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void whenGetUsuarioByIdAndNotFound_thenThrowException() {
        // Given
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.getUsuarioById(id);
        });

        assertThat(exception.getMessage()).isEqualTo("No value present");
        verify(usuarioRepository, times(1)).findById(id);
    }

    // Pruebas para saveUsuario(UsuarioEntity usuario)
    @Test
    void whenSaveUsuario_thenReturnSavedUsuario() {
        // Given
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre("Juan");

        UsuarioEntity usuarioGuardado = new UsuarioEntity();
        usuarioGuardado.setId(1L);
        usuarioGuardado.setNombre("Juan");

        when(usuarioRepository.save(usuario)).thenReturn(usuarioGuardado);

        // When
        UsuarioEntity resultado = usuarioService.saveUsuario(usuario);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void whenSaveUsuarioAndException_thenThrowException() {
        // Given
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre("Juan");

        when(usuarioRepository.save(usuario)).thenThrow(new RuntimeException("Error al guardar usuario"));

        // When / Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.saveUsuario(usuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Error al guardar usuario");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    // Pruebas para deleteUsuario(Long id)
    @Test
    void whenDeleteUsuario_thenReturnTrue() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(usuarioRepository).deleteById(id);

        // When
        boolean resultado = usuarioService.deleteUsuario(id);

        // Then
        assertThat(resultado).isTrue();
        verify(usuarioRepository, times(1)).deleteById(id);
    }

    @Test
    void whenDeleteUsuarioAndException_thenThrowException() {
        // Given
        Long id = 1L;
        doThrow(new RuntimeException("Error al eliminar")).when(usuarioRepository).deleteById(id);

        // When / Then
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.deleteUsuario(id);
        });

        assertThat(exception.getMessage()).isEqualTo("Error al eliminar");
        verify(usuarioRepository, times(1)).deleteById(id);
    }

    // ------------------------------------ Pruebas Principales ----------------------------------------------

    // Pruebas para simularCredito(Long idUsuario)
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
    void whenSimularCreditoAndNoPrestamo_thenThrowException() {
        // Given
        Long idUsuario = 1L;

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.simularCredito(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no asociado al usuario");
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(0)).findById(anyLong());
    }

    @Test
    void whenSimularCreditoAndPrestamoNotFound_thenThrowException() {
        // Given
        Long idUsuario = 1L;

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdUsuario(idUsuario);
        usuarioPrestamo.setIdPrestamo(1L);

        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.simularCredito(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no encontrado");
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    // Pruebas para solicitarCredito(Long idUsuario, PrestamoEntity prestamo, ComprobanteIngresosEntity comprobanteIngresos)
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
        prestamo.setTipo("Primera vivienda");

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

        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(1)).findByIdUsuario(idUsuario);
        verify(comprobanteIngresosRepository, times(1)).save(comprobanteIngresos);
        verify(usuarioComprobanteIngresosRepository, times(1)).save(any(UsuarioComprobanteIngresosEntity.class));
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).save(prestamo);
    }

    @Test
    void whenSolicitarCreditoAndUsuarioNotFound_thenThrowException() {
        // Given
        Long idUsuario = 1L;
        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setMonto(100000000);
        prestamo.setPlazo(20);
        prestamo.setTasaInteres(5.0);
        prestamo.setTipo("Primera vivienda");

        ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
        comprobanteIngresos.setIngresoMensual(5000000);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.solicitarCredito(idUsuario, prestamo, comprobanteIngresos);
        });

        assertThat(exception.getMessage()).isEqualTo("Usuario no encontrado");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioComprobanteIngresosRepository, times(0)).findByIdUsuario(anyLong());
        verify(prestamoRepository, times(0)).save(any(PrestamoEntity.class));
    }


    // Pruebas para obtenerEstadoSolicitud(Long idUsuario)
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
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }

    @Test
    void whenObtenerEstadoSolicitudAndUsuarioNotFound_thenThrowException() {
        // Given
        Long idUsuario = 1L;

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.obtenerEstadoSolicitud(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Usuario no encontrado");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioPrestamoRepository, times(0)).findByIdUsuario(anyLong());
        verify(prestamoRepository, times(0)).findById(anyLong());
    }

    @Test
    void whenObtenerEstadoSolicitudAndPrestamoNotAssociated_thenThrowException() {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.obtenerEstadoSolicitud(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no asociado al usuario");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(0)).findById(anyLong());
    }

    @Test
    void whenObtenerEstadoSolicitudAndPrestamoNotFound_thenThrowException() {
        // Given
        Long idUsuario = 1L;

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(idUsuario);

        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdUsuario(idUsuario);
        usuarioPrestamo.setIdPrestamo(1L);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioPrestamoRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(usuarioPrestamo));
        when(prestamoRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.obtenerEstadoSolicitud(idUsuario);
        });

        assertThat(exception.getMessage()).isEqualTo("Préstamo no encontrado");
        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioPrestamoRepository, times(1)).findByIdUsuario(idUsuario);
        verify(prestamoRepository, times(1)).findById(1L);
    }
}
