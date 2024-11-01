package edu.mtisw.payrollbackend.controllers;

import edu.mtisw.payrollbackend.entities.ComprobanteIngresosEntity;
import edu.mtisw.payrollbackend.services.ComprobanteIngresosService;
import edu.mtisw.payrollbackend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comprobante-ingresos")
@CrossOrigin("*")
public class ComprobanteIngresosController {
    @Autowired
    ComprobanteIngresosService comprobanteIngresosService;

    //------------------------------------CRUD----------------------------------------------
    // Obtener comprobantes de ingresos
    @GetMapping("/")
    public ResponseEntity<List<ComprobanteIngresosEntity>> listComprobanteIngresos(){
        List<ComprobanteIngresosEntity> comprobanteIngresos = comprobanteIngresosService.getComprobanteIngresos();
        return ResponseEntity.ok(comprobanteIngresos);
    }

    // Obtener comprobante de ingreso
    @GetMapping("/{id}")
    public ResponseEntity<ComprobanteIngresosEntity> getComprobanteIngresosById(@PathVariable Long id){
        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosService.getComprobanteIngresosById(id);
        return ResponseEntity.ok(comprobanteIngresos);
    }

    // Guardar comprobante de ingreso
    @PostMapping("/")
    public ResponseEntity<ComprobanteIngresosEntity> saveComprobanteIngresos(@RequestBody ComprobanteIngresosEntity comprobanteIngresos){
        ComprobanteIngresosEntity comprobanteIngresosNuevo = comprobanteIngresosService.saveComprobanteIngresos(comprobanteIngresos);
        return ResponseEntity.ok(comprobanteIngresosNuevo);
    }

    // Actualizar comprobante de ingreso
    @PutMapping("/")
    public ResponseEntity<ComprobanteIngresosEntity> updateComprobanteIngresos(@RequestBody ComprobanteIngresosEntity comprobanteIngresos){
        ComprobanteIngresosEntity comprobanteIngresosActualizado =  comprobanteIngresosService.updateComprobanteIngresos(comprobanteIngresos);
        return ResponseEntity.ok(comprobanteIngresosActualizado);
    }

    // Eliminar comprobante de ingreso
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteComprobanteIngresosById(@PathVariable Long id) throws Exception {
        var isDeleted = comprobanteIngresosService.deleteComprobanteIngresos(id);
        return ResponseEntity.noContent().build();
    }

    //------------------------------------PRINCIPALES---------------------------------------

}
