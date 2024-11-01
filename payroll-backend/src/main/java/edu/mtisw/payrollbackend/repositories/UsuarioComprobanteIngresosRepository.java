package edu.mtisw.payrollbackend.repositories;

import edu.mtisw.payrollbackend.entities.UsuarioComprobanteIngresosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioComprobanteIngresosRepository extends JpaRepository<UsuarioComprobanteIngresosEntity, Long> {
    Optional<UsuarioComprobanteIngresosEntity> findByIdUsuario(Long idUsuario);
}
