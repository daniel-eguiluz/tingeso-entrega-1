package edu.mtisw.payrollbackend.repositories;

import edu.mtisw.payrollbackend.entities.BancoEntity;
import edu.mtisw.payrollbackend.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BancoRepository extends JpaRepository<BancoEntity, Integer>{
}
