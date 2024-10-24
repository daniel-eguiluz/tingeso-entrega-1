package edu.mtisw.payrollbackend.repositories;

import edu.mtisw.payrollbackend.entities.ComprobanteIngresosEntity;
import edu.mtisw.payrollbackend.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobanteIngresosRepository extends JpaRepository<ComprobanteIngresosEntity, Integer>{
}