package edu.mtisw.payrollbackend.controllers;

import edu.mtisw.payrollbackend.services.BancoService;
import edu.mtisw.payrollbackend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bancos")
@CrossOrigin("*")
public class BancoController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    BancoService bancoService;

    // Evaluar Crédito (P4)
    @GetMapping("/evaluar-credito/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarCredito(
            @PathVariable Long idUsuario) {
        try {
            Map<String, Object> resultado = bancoService.evaluarCredito(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Relación Cuota/Ingreso (R1)
    @GetMapping("/evaluar-relacion-cuota-ingreso/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarRelacionCuotaIngreso(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarRelacionCuotaIngreso(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Resto de los métodos, ajustados para usar idUsuario

    // Evaluar Historial Crediticio del Cliente (R2)
    @GetMapping("/evaluar-historial-crediticio/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarHistorialCrediticio(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarHistorialCrediticio(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Antigüedad Laboral y Estabilidad (R3)
    @GetMapping("/evaluar-antiguedad/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarAntiguedad(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarAntiguedad(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Relación Deuda/Ingreso (R4)
    @GetMapping("/evaluar-relacion-deuda-ingreso/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarRelacionDeudaIngreso(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarRelacionDeudaIngreso(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Monto Máximo de Financiamiento (R5)
    @GetMapping("/evaluar-monto-maximo/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarMontoMaximoFinanciamiento(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarMontoMaximoFinanciamiento(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Edad del Solicitante (R6)
    @GetMapping("/evaluar-edad/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarEdad(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarEdad(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Saldo Mínimo Requerido (R71)
    @GetMapping("/evaluar-saldo-minimo/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarSaldoMinimo(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarSaldoMinimo(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Historial de Ahorro Consistente (R72)
    @GetMapping("/evaluar-historial-ahorro/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarHistorialAhorroConsistente(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarHistorialAhorroConsistente(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Depósitos Periódicos (R73)
    @GetMapping("/evaluar-depositos-periodicos/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarDepositosPeriodicos(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarDepositosPeriodicos(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Relación Saldo/Antigüedad (R74)
    @GetMapping("/evaluar-relacion-saldo-antiguedad/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarRelacionSaldoAntiguedad(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarRelacionSaldoAntiguedad(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Retiros Recientes (R75)
    @GetMapping("/evaluar-retiros-recientes/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarRetirosRecientes(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarRetirosRecientes(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Evaluar Capacidad de Ahorro (R7)
    @GetMapping("/evaluar-capacidad-ahorro/{idUsuario}")
    public ResponseEntity<Map<String, Object>> evaluarCapacidadAhorro(
            @PathVariable Long idUsuario) {
        try {
            Map<String, Object> resultado = bancoService.evaluarCapacidadAhorro(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Calcular Costos Totales (P6)
    @GetMapping("/calcular-costo-total/{idUsuario}")
    public ResponseEntity<Map<String, Object>> calcularCostoTotalPrestamo(
            @PathVariable Long idUsuario) {
        try {
            Map<String, Object> resultado = bancoService.calcularCostoTotalPrestamo(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
