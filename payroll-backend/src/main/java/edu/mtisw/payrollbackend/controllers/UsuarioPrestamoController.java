package edu.mtisw.payrollbackend.controllers;

import edu.mtisw.payrollbackend.entities.UsuarioPrestamoEntity;
import edu.mtisw.payrollbackend.services.UsuarioPrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios-prestamos")
@CrossOrigin("*")
public class UsuarioPrestamoController {
    @Autowired
    UsuarioPrestamoService usuarioPrestamoService;
    //------------------------------------CRUD----------------------------------------------
    // Obtener usuarios prestamos
    @GetMapping("/")
    public ResponseEntity<List<UsuarioPrestamoEntity>> listUsuarios(){
        List<UsuarioPrestamoEntity> usuarios = usuarioPrestamoService.getUsuariosPrestamos();
        return ResponseEntity.ok(usuarios);
    }

    // Obtener usuario prestamo
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPrestamoEntity> getUsuarioById(@PathVariable Integer id){
        UsuarioPrestamoEntity usuario = usuarioPrestamoService.getUsuarioPrestamoById(id);
        return ResponseEntity.ok(usuario);
    }

    // Guardar usuario prestamo
    @PostMapping("/")
    public ResponseEntity<UsuarioPrestamoEntity> saveUsuario(@RequestBody UsuarioPrestamoEntity usuario){
        UsuarioPrestamoEntity usuarioNuevo = usuarioPrestamoService.saveUsuarioPrestamo(usuario);
        return ResponseEntity.ok(usuarioNuevo);
    }

    // Actualizar usuario prestamo
    @PutMapping("/")
    public ResponseEntity<UsuarioPrestamoEntity> updateUsuario(@RequestBody UsuarioPrestamoEntity usuario){
        UsuarioPrestamoEntity usuarioActualizado =  usuarioPrestamoService.updateUsuarioPrestamo(usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // Eliminar usuario prestamo
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUsuarioById(@PathVariable Integer id) throws Exception {
        var isDeleted = usuarioPrestamoService.deleteUsuarioPrestamo(id);
        return ResponseEntity.noContent().build();
    }

    //------------------------------------PRINCIPALES---------------------------------------
}
