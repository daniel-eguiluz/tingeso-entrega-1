package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.UsuarioPrestamoEntity;
import edu.mtisw.payrollbackend.repositories.UsuarioPrestamoRepository;
import edu.mtisw.payrollbackend.repositories.UsuarioRepository;
import jakarta.persistence.Id;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UsuarioPrestamoService {
    @Autowired
    UsuarioPrestamoRepository usuarioPrestamoRepository;
    //------------------------------------CRUD----------------------------------------------
    // Obtener todos los usuarios-prestamos
    public ArrayList<UsuarioPrestamoEntity> getUsuariosPrestamos(){
        return (ArrayList<UsuarioPrestamoEntity>) usuarioPrestamoRepository.findAll();
    }

    // Obtener un usuario-prestamo por id
    public UsuarioPrestamoEntity getUsuarioPrestamoById(Long id){
        return usuarioPrestamoRepository.findById(id).get();
    }

    // Guardar un usuario-prestamo
    public UsuarioPrestamoEntity saveUsuarioPrestamo(UsuarioPrestamoEntity usuarioPrestamo){
        return usuarioPrestamoRepository.save(usuarioPrestamo);
    }

    // Actualizar un usuario-prestamo
    public UsuarioPrestamoEntity updateUsuarioPrestamo(UsuarioPrestamoEntity usuarioPrestamo){
        return usuarioPrestamoRepository.save(usuarioPrestamo);
    }

    // Eliminar un usuario-prestamo
    public boolean deleteUsuarioPrestamo(Long id) throws Exception {
        try{
            usuarioPrestamoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //------------------------------------PRINCIPALES---------------------------------------
}
