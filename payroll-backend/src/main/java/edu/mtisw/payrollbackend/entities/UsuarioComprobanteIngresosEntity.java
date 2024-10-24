package edu.mtisw.payrollbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarioComprobanteIngresos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioComprobanteIngresosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @Column(name = "idUsuario")
    private Long idUsuario;

    @Column(name = "idComprobanteIngresos")
    private Long idComprobanteIngresos;
}
