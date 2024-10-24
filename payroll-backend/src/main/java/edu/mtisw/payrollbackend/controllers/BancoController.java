package edu.mtisw.payrollbackend.controllers;

import edu.mtisw.payrollbackend.entities.BancoEntity;
import edu.mtisw.payrollbackend.entities.UsuarioEntity;
import edu.mtisw.payrollbackend.services.BancoService;
import edu.mtisw.payrollbackend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bancos")
@CrossOrigin("*")
public class BancoController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    BancoService bancoService;
    //------------------------------------CRUD----------------------------------------------
    //------------------------------------PRINCIPALES---------------------------------------
    //evaluarCredito()(P4)
    @GetMapping("/evaluar-credito/{idUsuario}/{idPrestamo}")
    public ResponseEntity<Map<String, Object>> evaluarCredito(
            @PathVariable Long idUsuario,
            @PathVariable Long idPrestamo) {
        try {
            Map<String, Object> resultado = bancoService.evaluarCredito(idUsuario, idPrestamo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarRelacionCuotaIngreso()(R1)
    @GetMapping("/evaluar-relacion-cuota-ingreso/{idUsuario}/{idPrestamo}")
    public ResponseEntity<Boolean> evaluarRelacionCuotaIngreso(
            @PathVariable Long idUsuario,
            @PathVariable Long idPrestamo) {
        try {
            boolean resultado = bancoService.evaluarRelacionCuotaIngreso(idUsuario, idPrestamo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarDeudas()(R2)
    @GetMapping("/evaluar-historial-crediticio/{idUsuario}")
    public ResponseEntity<Boolean> evaluarHistorialCrediticio(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarHistorialCrediticio(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarAntiguedad()(R3)
    @GetMapping("/evaluar-antiguedad/{idUsuario}")
    public ResponseEntity<Boolean> evaluarAntiguedad(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarAntiguedad(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarRelacionDeudaIngreso()(R4)
    @GetMapping("/evaluar-relacion-deuda-ingreso/{idUsuario}/{idPrestamo}")
    public ResponseEntity<Boolean> evaluarRelacionDeudaIngreso(
            @PathVariable Long idUsuario,
            @PathVariable Long idPrestamo) {
        try {
            boolean resultado = bancoService.evaluarRelacionDeudaIngreso(idUsuario, idPrestamo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarMontoMaximoFinanciamiento()(R5)
    @GetMapping("/evaluar-monto-maximo/{idPrestamo}")
    public ResponseEntity<Boolean> evaluarMontoMaximoFinanciamiento(
            @PathVariable Long idPrestamo) {
        try {
            boolean resultado = bancoService.evaluarMontoMaximoFinanciamiento(idPrestamo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarEdad()(R6)
    @GetMapping("/evaluar-edad/{idUsuario}/{idPrestamo}")
    public ResponseEntity<Boolean> evaluarEdad(
            @PathVariable Long idUsuario,
            @PathVariable Long idPrestamo) {
        try {
            boolean resultado = bancoService.evaluarEdad(idUsuario, idPrestamo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarSaldoMinimo()(R71)
    @GetMapping("/evaluar-saldo-minimo/{idUsuario}/{idPrestamo}")
    public ResponseEntity<Boolean> evaluarSaldoMinimo(
            @PathVariable Long idUsuario,
            @PathVariable Long idPrestamo) {
        try {
            boolean resultado = bancoService.evaluarSaldoMinimo(idUsuario, idPrestamo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarHistorialAhorroConsistente(R72)
    @GetMapping("/evaluar-historial-ahorro/{idUsuario}")
    public ResponseEntity<Boolean> evaluarHistorialAhorroConsistente(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarHistorialAhorroConsistente(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarDepositosPeriodicos(R73)
    @GetMapping("/evaluar-depositos-periodicos/{idUsuario}")
    public ResponseEntity<Boolean> evaluarDepositosPeriodicos(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarDepositosPeriodicos(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarRelacionSaldoAntiguedad(R74)
    @GetMapping("/evaluar-relacion-saldo-antiguedad/{idUsuario}/{idPrestamo}")
    public ResponseEntity<Boolean> evaluarRelacionSaldoAntiguedad(
            @PathVariable Long idUsuario,
            @PathVariable Long idPrestamo) {
        try {
            boolean resultado = bancoService.evaluarRelacionSaldoAntiguedad(idUsuario, idPrestamo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarRetiroReciente(R75)
    @GetMapping("/evaluar-retiros-recientes/{idUsuario}")
    public ResponseEntity<Boolean> evaluarRetirosRecientes(
            @PathVariable Long idUsuario) {
        try {
            boolean resultado = bancoService.evaluarRetirosRecientes(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    //evaluarCapacidadAhorro()(R7)
    @GetMapping("/evaluar-capacidad-ahorro/{idUsuario}/{idPrestamo}")
    public ResponseEntity<Map<String, Object>> evaluarCapacidadAhorro(
            @PathVariable Long idUsuario,
            @PathVariable Long idPrestamo) {
        try {
            Map<String, Object> resultado = bancoService.evaluarCapacidadAhorro(idUsuario, idPrestamo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    //calcularCostoTotales()(P6)
    @GetMapping("/calcular-costo-total/{idPrestamo}")
    public ResponseEntity<Map<String, Object>> calcularCostoTotalPrestamo(
            @PathVariable Long idPrestamo) {
        try {
            Map<String, Object> resultado = bancoService.calcularCostoTotalPrestamo(idPrestamo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
}
