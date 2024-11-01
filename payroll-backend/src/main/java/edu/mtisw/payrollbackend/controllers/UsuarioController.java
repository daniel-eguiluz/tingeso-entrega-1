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
        UsuarioEntity usuarioActualizado = usuarioService.updateUsuario(usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }
    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUsuarioById(@PathVariable Long id) throws Exception {
        var isDeleted = usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
    //------------------------------------PRINCIPALES---------------------------------------

    // Simular Crédito (P1)
    @GetMapping("/{id}/simular-credito")
    public ResponseEntity<Map<String, Object>> simularCredito(
            @PathVariable Long id) {
        try {
            Map<String, Object> resultadoSimulacion = usuarioService.simularCredito(id);
            return ResponseEntity.ok(resultadoSimulacion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Solicitar Crédito (P3)
    @PostMapping("/{id}/solicitar-credito")
    public ResponseEntity<PrestamoEntity> solicitarCredito(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Extracción de datos para PrestamoEntity
            String tipo = (String) requestBody.get("tipo");
            int plazo = ((Number) requestBody.get("plazo")).intValue();
            double tasaInteres = ((Number) requestBody.get("tasaInteres")).doubleValue();
            int monto = ((Number) requestBody.get("monto")).intValue();
            int valorPropiedad = ((Number) requestBody.get("valorPropiedad")).intValue();

            // Crear PrestamoEntity
            PrestamoEntity prestamo = new PrestamoEntity();
            prestamo.setTipo(tipo);
            prestamo.setPlazo(plazo);
            prestamo.setTasaInteres(tasaInteres);
            prestamo.setMonto(monto);
            prestamo.setEstado("En proceso");
            prestamo.setValorPropiedad(valorPropiedad);

            // Extracción de datos para ComprobanteIngresosEntity
            int antiguedadLaboral = ((Number) requestBody.get("antiguedadLaboral")).intValue();
            int ingresoMensual = ((Number) requestBody.get("ingresoMensual")).intValue();
            int saldo = ((Number) requestBody.get("saldo")).intValue();
            int deudas = ((Number) requestBody.get("deudas")).intValue();
            int cantidadDeudasPendientes = ((Number) requestBody.get("cantidadDeudasPendientes")).intValue();
            int antiguedadCuenta = ((Number) requestBody.get("antiguedadCuenta")).intValue();

            // Extraer listas y convertir a cadenas
            List<Number> ingresosNumbers = (List<Number>) requestBody.get("ingresosUltimos24Meses");
            String ingresosUltimos24Meses = ingresosNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            List<Number> saldosMensualesNumbers = (List<Number>) requestBody.get("saldosMensuales");
            String saldosMensuales = saldosMensualesNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            List<Number> depositosNumbers = (List<Number>) requestBody.get("depositosUltimos12Meses");
            String depositosUltimos12Meses = depositosNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            List<Number> retirosNumbers = (List<Number>) requestBody.get("retirosUltimos6Meses");
            String retirosUltimos6Meses = retirosNumbers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            // Crear ComprobanteIngresosEntity
            ComprobanteIngresosEntity comprobanteIngresos = new ComprobanteIngresosEntity();
            comprobanteIngresos.setAntiguedadLaboral(antiguedadLaboral);
            comprobanteIngresos.setIngresoMensual(ingresoMensual);
            comprobanteIngresos.setIngresosUltimos24Meses(ingresosUltimos24Meses);
            comprobanteIngresos.setSaldo(saldo);
            comprobanteIngresos.setDeudas(deudas);
            comprobanteIngresos.setCantidadDeudasPendientes(cantidadDeudasPendientes);
            comprobanteIngresos.setSaldosMensuales(saldosMensuales);
            comprobanteIngresos.setDepositosUltimos12Meses(depositosUltimos12Meses);
            comprobanteIngresos.setAntiguedadCuenta(antiguedadCuenta);
            comprobanteIngresos.setRetirosUltimos6Meses(retirosUltimos6Meses);

            // Llamar al servicio para procesar la solicitud de crédito
            PrestamoEntity prestamoSolicitado = usuarioService.solicitarCredito(id, prestamo, comprobanteIngresos);
            return ResponseEntity.ok(prestamoSolicitado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Obtener estado de la solicitud (P5)
    @GetMapping("/{id}/estado-solicitud")
    public ResponseEntity<PrestamoEntity> obtenerEstadoSolicitud(@PathVariable Long id) {
        try {
            PrestamoEntity prestamo = usuarioService.obtenerEstadoSolicitud(id);
            return ResponseEntity.ok(prestamo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
