package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.ComprobanteIngresosEntity;
import edu.mtisw.payrollbackend.entities.UsuarioEntity;
import edu.mtisw.payrollbackend.repositories.ComprobanteIngresosRepository;
import edu.mtisw.payrollbackend.repositories.UsuarioRepository;
import jakarta.persistence.Id;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ComprobanteIngresosService {
    @Autowired
    ComprobanteIngresosRepository comprobanteIngresosRepository;

    //------------------------------------CRUD----------------------------------------------
    // Obtener todos los comprobantes de ingresos
    public ArrayList<ComprobanteIngresosEntity> getComprobanteIngresos(){
        return (ArrayList<ComprobanteIngresosEntity>) comprobanteIngresosRepository.findAll();
    }

    // Obtener un comprobante de ingresos por id
    public ComprobanteIngresosEntity getComprobanteIngresosById(Integer id){
        return comprobanteIngresosRepository.findById(id).get();
    }

    // Guardar un comprobante de ingresos
    public ComprobanteIngresosEntity saveComprobanteIngresos(ComprobanteIngresosEntity comprobanteIngresos){
        return comprobanteIngresosRepository.save(comprobanteIngresos);
    }

    // Actualizar un comprobante de ingresos
    public ComprobanteIngresosEntity updateComprobanteIngresos(ComprobanteIngresosEntity comprobanteIngresos){
        return comprobanteIngresosRepository.save(comprobanteIngresos);
    }

    // Eliminar un comprobante de ingresos
    public boolean deleteComprobanteIngresos(Integer id) throws Exception {
        try{
            comprobanteIngresosRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //------------------------------------PRINCIPALES---------------------------------------



}
