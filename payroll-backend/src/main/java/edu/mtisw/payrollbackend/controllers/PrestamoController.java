package edu.mtisw.payrollbackend.controllers;

import edu.mtisw.payrollbackend.entities.PrestamoEntity;
import edu.mtisw.payrollbackend.entities.UsuarioEntity;
import edu.mtisw.payrollbackend.services.PrestamoService;
import edu.mtisw.payrollbackend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
@CrossOrigin("*")
public class PrestamoController {
    @Autowired
    PrestamoService prestamoService;

    //------------------------------------CRUD----------------------------------------------
    // Obtener prestamos
    @GetMapping("/")
    public ResponseEntity<List<PrestamoEntity>> listPrestamos(){
        List<PrestamoEntity> prestamos = prestamoService.getPrestamos();
        return ResponseEntity.ok(prestamos);
    }

    // Obtener prestamo
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoEntity> getPrestamoById(@PathVariable Integer id){
        PrestamoEntity prestamo = prestamoService.getPrestamoById(id);
        return ResponseEntity.ok(prestamo);
    }

    // Guardar prestamo
    @PostMapping("/")
    public ResponseEntity<PrestamoEntity> savePrestamo(@RequestBody PrestamoEntity prestamo){
        PrestamoEntity prestamoNuevo = prestamoService.savePrestamo(prestamo);
        return ResponseEntity.ok(prestamoNuevo);
    }

    // Actualizar prestamo
    @PutMapping("/")
    public ResponseEntity<PrestamoEntity> updatePrestamo(@RequestBody PrestamoEntity prestamo){
        PrestamoEntity prestamoActualizado =  prestamoService.updatePrestamo(prestamo);
        return ResponseEntity.ok(prestamoActualizado);
    }

    // Eliminar prestamo
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePrestamoById(@PathVariable Integer id) throws Exception {
        var isDeleted = prestamoService.deletePrestamo(id);
        return ResponseEntity.noContent().build();
    }

    //------------------------------------PRINCIPALES---------------------------------------
}
