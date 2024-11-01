package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.PrestamoEntity;
import edu.mtisw.payrollbackend.repositories.PrestamoRepository;
import edu.mtisw.payrollbackend.repositories.ExtraHoursRepository;
import edu.mtisw.payrollbackend.repositories.PrestamoRepository;
import jakarta.persistence.Id;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PrestamoService {
    @Autowired
    PrestamoRepository prestamoRepository;
    //------------------------------------CRUD----------------------------------------------
    // Obtener todos los prestamos
    public ArrayList<PrestamoEntity> getPrestamos(){
        return (ArrayList<PrestamoEntity>) prestamoRepository.findAll();
    }

    // Obtener un prestamo por id
    public PrestamoEntity getPrestamoById(Long id){
        return prestamoRepository.findById(id).get();
    }

    // Guardar un prestamo
    public PrestamoEntity savePrestamo(PrestamoEntity prestamo){
        return prestamoRepository.save(prestamo);
    }

    // Actualizar un prestamo
    public PrestamoEntity updatePrestamo(PrestamoEntity prestamo){
        return prestamoRepository.save(prestamo);
    }

    // Eliminar un prestamo
    public boolean deletePrestamo(Long id) throws Exception {
        try{
            prestamoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //------------------------------------PRINCIPALES---------------------------------------
}
