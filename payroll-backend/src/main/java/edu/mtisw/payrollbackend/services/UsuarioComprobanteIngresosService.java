package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.UsuarioComprobanteIngresosEntity;
import edu.mtisw.payrollbackend.entities.UsuarioEntity;
import edu.mtisw.payrollbackend.repositories.UsuarioComprobanteIngresosRepository;
import edu.mtisw.payrollbackend.repositories.UsuarioRepository;
import jakarta.persistence.Id;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UsuarioComprobanteIngresosService {
    @Autowired
    UsuarioComprobanteIngresosRepository usuarioComprobanteIngresosRepository;
    //------------------------------------CRUD----------------------------------------------
    // Obtener todos los usuarios comprobante de ingresos
    public ArrayList<UsuarioComprobanteIngresosEntity> getUsuariosComprobanteIngresos(){
        return (ArrayList<UsuarioComprobanteIngresosEntity>) usuarioComprobanteIngresosRepository.findAll();
    }

    // Obtener un usuario comprobante de ingresos por id
    public UsuarioComprobanteIngresosEntity getUsuarioComprobanteIngresosById(Long id){
        return usuarioComprobanteIngresosRepository.findById(id).get();
    }

    // Guardar un usuario comprobante de ingresos
    public UsuarioComprobanteIngresosEntity saveUsuarioComprobanteIngresos(UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos){
        return usuarioComprobanteIngresosRepository.save(usuarioComprobanteIngresos);
    }

    // Actualizar un usuario comprobante de ingresos
    public UsuarioComprobanteIngresosEntity updateUsuarioComprobanteIngresos(UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos){
        return usuarioComprobanteIngresosRepository.save(usuarioComprobanteIngresos);
    }

    // Eliminar un usuario comprobante de ingresos
    public boolean deleteUsuarioComprobanteIngresos(Long id) throws Exception {
        try{
            usuarioComprobanteIngresosRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //------------------------------------PRINCIPALES---------------------------------------
}
