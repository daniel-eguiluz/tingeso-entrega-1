package edu.mtisw.payrollbackend.repositories;

import edu.mtisw.payrollbackend.entities.UsuarioPrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioPrestamoRepository extends JpaRepository<UsuarioPrestamoEntity, Integer> {
    List<UsuarioPrestamoEntity> findByIdUsuario(Long idUsuario);
}
