package edu.mtisw.payrollbackend.controllers;

import edu.mtisw.payrollbackend.entities.UsuarioComprobanteIngresosEntity;
import edu.mtisw.payrollbackend.entities.UsuarioEntity;
import edu.mtisw.payrollbackend.services.UsuarioComprobanteIngresosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios-comprobantes-ingresos")
@CrossOrigin("*")
public class UsuarioComprobanteIngresosController {
    @Autowired
    UsuarioComprobanteIngresosService usuarioComprobanteIngresosService;
    //------------------------------------CRUD----------------------------------------------
    // Obtener usuarios comprobantes ingresos
    @GetMapping("/")
    public ResponseEntity<List<UsuarioComprobanteIngresosEntity>> listUsuariosComprobantesIngresos(){
        List<UsuarioComprobanteIngresosEntity> usuariosComprobantesIngresos = usuarioComprobanteIngresosService.getUsuariosComprobanteIngresos();
        return ResponseEntity.ok(usuariosComprobantesIngresos);
    }

    // Obtener un usuario comprobante de ingresos por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioComprobanteIngresosEntity> getUsuarioComprobanteIngresosById(@PathVariable Integer id){
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosService.getUsuarioComprobanteIngresosById(id);
        return ResponseEntity.ok(usuarioComprobanteIngresos);
    }

    // Guardar un usuario comprobante de ingresos
    @PostMapping("/")
    public ResponseEntity<UsuarioComprobanteIngresosEntity> saveUsuarioComprobanteIngresos(@RequestBody UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos){
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresosSaved = usuarioComprobanteIngresosService.saveUsuarioComprobanteIngresos(usuarioComprobanteIngresos);
        return ResponseEntity.ok(usuarioComprobanteIngresosSaved);
    }

    // Actualizar un usuario comprobante de ingresos
    @PutMapping("/")
    public ResponseEntity<UsuarioComprobanteIngresosEntity> updateUsuarioComprobanteIngresos(@RequestBody UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos){
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresosUpdated = usuarioComprobanteIngresosService.updateUsuarioComprobanteIngresos(usuarioComprobanteIngresos);
        return ResponseEntity.ok(usuarioComprobanteIngresosUpdated);
    }

    // Eliminar un usuario comprobante de ingresos
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUsuarioComprobanteIngresos(@PathVariable Integer id){
        try {
            boolean isDeleted = usuarioComprobanteIngresosService.deleteUsuarioComprobanteIngresos(id);
            return ResponseEntity.ok(isDeleted);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //------------------------------------PRINCIPALES---------------------------------------
}



