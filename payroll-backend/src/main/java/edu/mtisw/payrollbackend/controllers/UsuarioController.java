package edu.mtisw.payrollbackend.controllers;

import edu.mtisw.payrollbackend.entities.ComprobanteIngresosEntity;
import edu.mtisw.payrollbackend.entities.PrestamoEntity;
import edu.mtisw.payrollbackend.entities.UsuarioEntity;
import edu.mtisw.payrollbackend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin("*")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;
    //------------------------------------CRUD----------------------------------------------
    // Obtener usuarios
    @GetMapping("/")
    public ResponseEntity<List<UsuarioEntity>> listUsuarios(){
        List<UsuarioEntity> usuarios = usuarioService.getUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // Obtener usuario
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> getUsuarioById(@PathVariable Long id){
        UsuarioEntity usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    // Guardar usuario
    @PostMapping("/")
    public ResponseEntity<UsuarioEntity> saveUsuario(@RequestBody UsuarioEntity usuario){
        UsuarioEntity usuarioNuevo = usuarioService.saveUsuario(usuario);
        return ResponseEntity.ok(usuarioNuevo);
    }

    // Actualizar usuario
    @PutMapping("/")
    public ResponseEntity<UsuarioEntity> updateUsuario(@RequestBody UsuarioEntity usuario){
        UsuarioEntity usuarioActualizado =  usuarioService.updateUsuario(usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUsuarioById(@PathVariable Long id) throws Exception {
        var isDeleted = usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    //------------------------------------PRINCIPALES---------------------------------------
    //simularCredito()(P1)
    @GetMapping("/{id}/simular-credito/{idPrestamo}")
    public ResponseEntity<Map<String, Object>> simularCredito(
            @PathVariable Long id,
            @PathVariable Long idPrestamo) {
        try {
            // Llamar al servicio para simular el crédito
            Map<String, Object> resultadoSimulacion = usuarioService.simularCredito(id, idPrestamo);
            return ResponseEntity.ok(resultadoSimulacion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Registrar usuario(implementado en el CRUD)

    // Solicitar Credito (P3) (por implementar)
    // Registrar usuario (P2) (implementado en el CRUD)
    @PostMapping("/{id}/solicitar-credito")
    public ResponseEntity<PrestamoEntity> solicitarCredito(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody) {

        try {
            // Extracción segura de datos
            String tipo = (String) requestBody.get("tipo");
            int plazo = ((Number) requestBody.get("plazo")).intValue();
            double tasaInteres = ((Number) requestBody.get("tasaInteres")).doubleValue();
            int monto = ((Number) requestBody.get("monto")).intValue();
            int valorPropiedad = ((Number) requestBody.get("valorPropiedad")).intValue(); // Nuevo código

            int antiguedadLaboral = ((Number) requestBody.get("antiguedadLaboral")).intValue();
            int ingresoMensual = ((Number) requestBody.get("ingresoMensual")).intValue();
            int saldo = ((Number) requestBody.get("saldo")).intValue();

            // Manejo de listas (solo ingresos)
            List<Number> ingresosNumbers = (List<Number>) requestBody.get("ingresosUltimos24Meses");
            List<Integer> ingresosUltimos24Meses = ingresosNumbers.stream()
                    .map(Number::intValue)
                    .collect(Collectors.toList());

            // Crear los objetos PrestamoEntity y ComprobanteIngresosEntity
            PrestamoEntity prestamo = new PrestamoEntity();
            prestamo.setTipo(tipo);
            prestamo.setPlazo(plazo);
            prestamo.setTasaInteres(tasaInteres);
            prestamo.setMonto(monto);
            prestamo.setEstado("En proceso");
            prestamo.setValorPropiedad(valorPropiedad); // Nuevo código

            ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
            comprobanteIngresos.setAntiguedadLaboral(antiguedadLaboral);
            comprobanteIngresos.setIngresoMensual(ingresoMensual);
            comprobanteIngresos.setSaldo(saldo);

            // Convertir lista de ingresos a cadena y asignar
            String ingresosString = ingresosUltimos24Meses.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            comprobanteIngresos.setIngresosUltimos24Meses(ingresosString);

            // Llamar al servicio para procesar la solicitud de crédito
            PrestamoEntity prestamoSolicitado = usuarioService.solicitarCredito(id, prestamo, comprobanteIngresos);

            return ResponseEntity.ok(prestamoSolicitado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Obtener estado solicitud (P5) (por implementar)
    @GetMapping("/{id}/estado-solicitudes")
    public ResponseEntity<List<PrestamoEntity>> obtenerEstadoSolicitudes(@PathVariable Long id) {
        try {
            List<PrestamoEntity> estadosPrestamos = usuarioService.obtenerEstadoSolicitudes(id);
            return ResponseEntity.ok(estadosPrestamos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
