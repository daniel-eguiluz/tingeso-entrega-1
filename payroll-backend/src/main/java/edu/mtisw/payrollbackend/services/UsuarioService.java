package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.*;
import edu.mtisw.payrollbackend.repositories.*;
import edu.mtisw.payrollbackend.repositories.UsuarioRepository;
import jakarta.persistence.Id;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ComprobanteIngresosRepository comprobanteIngresosRepository;
    @Autowired
    PrestamoRepository prestamoRepository;
    @Autowired
    UsuarioPrestamoRepository usuarioPrestamoRepository;
    @Autowired
    UsuarioComprobanteIngresosRepository usuarioComprobanteIngresosRepository;
    //------------------------------------CRUD----------------------------------------------
    // Obtener todos los usuarios
    public ArrayList<UsuarioEntity> getUsuarios(){
        return (ArrayList<UsuarioEntity>) usuarioRepository.findAll();
    }
    // Obtener un usuario por id
    public UsuarioEntity getUsuarioById(Integer id){
        return usuarioRepository.findById(id).get();
    }
    // Guardar un usuario
    public UsuarioEntity saveUsuario(UsuarioEntity usuario){
        return usuarioRepository.save(usuario);
    }
    // Actualizar un usuario
    public UsuarioEntity updateUsuario(UsuarioEntity usuario){
        return usuarioRepository.save(usuario);
    }
    // Eliminar un usuario
    public boolean deleteUsuario(Integer id) throws Exception {
        try{
            usuarioRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    //------------------------------------PRINCIPALES---------------------------------------
    //simularCredito()(P1)
    public Map<String, Object> simularCredito(Long idUsuario, Long idPrestamo) throws Exception {
        // Buscar el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(Math.toIntExact(idUsuario))
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Buscar el prestamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(Math.toIntExact(idPrestamo))
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));

        // Datos del préstamo para la simulación
        double tasaInteresAnual = prestamo.getTasaInteres();
        int plazoEnAnios = prestamo.getPlazo();
        int monto = prestamo.getMonto();

        // Calcular la tasa de interés mensual
        double tasaInteresMensual = (tasaInteresAnual / 12) / 100;

        // Número de pagos (meses)
        int numeroPagos = plazoEnAnios * 12;

        // Cálculo del pago mensual usando la fórmula de amortización
        double pagoMensual = (monto * tasaInteresMensual * Math.pow(1 + tasaInteresMensual, numeroPagos)) /
                (Math.pow(1 + tasaInteresMensual, numeroPagos) - 1);

        // Cálculo del interés total a pagar
        double totalPagos = pagoMensual * numeroPagos;
        double interesesTotales = totalPagos - monto;

        // Crear un mapa con los resultados de la simulación
        Map<String, Object> simulacionResultado = new HashMap<>();
        simulacionResultado.put("monto", monto);
        simulacionResultado.put("plazo", plazoEnAnios);
        simulacionResultado.put("tasaInteres", tasaInteresAnual);
        simulacionResultado.put("pagoMensual", pagoMensual);
        simulacionResultado.put("interesesTotales", interesesTotales);
        simulacionResultado.put("totalPagos", totalPagos);

        return simulacionResultado;
    }

    // Registrar usuario(implementado en el CRUD)

    // Solicitar Credito (P3) (por implementar)
    // Consiste en aplicar las funciones saveComprobanteIngresos de la entidad
    // y sus id en la tabla intermedia usuario_comprobante_ingresos
    // ComprobanteIngresos savePrestamo de la entidad Prestamo
    // y sus id en la tabla intermedia usuario_prestamo
    public PrestamoEntity solicitarCredito(Long idUsuario, PrestamoEntity prestamo, ComprobanteIngresosEntity comprobanteIngresos) throws Exception {
        // Obtener el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(Math.toIntExact(idUsuario)).orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Guardar el comprobante de ingresos
        ComprobanteIngresosEntity comprobanteIngresosGuardado = comprobanteIngresosRepository.save(comprobanteIngresos);

        // Guardar el prestamo
        prestamo.setEstado("En proceso");  // Establecer el estado inicial como "En proceso"
        PrestamoEntity prestamoGuardado = prestamoRepository.save(prestamo);

        // Crear y guardar la relación en UsuarioPrestamo
        UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
        usuarioPrestamo.setIdUsuario(usuario.getId());
        usuarioPrestamo.setIdPrestamo(prestamoGuardado.getId());
        usuarioPrestamoRepository.save(usuarioPrestamo);

        // También podríamos relacionar el comprobante de ingresos si es necesario.
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
        usuarioComprobanteIngresos.setIdUsuario(usuario.getId());
        usuarioComprobanteIngresos.setIdComprobanteIngresos(comprobanteIngresosGuardado.getId());
        // Guardar en la tabla intermedia
        usuarioComprobanteIngresosRepository.save(usuarioComprobanteIngresos);

        // Retornar el prestamo guardado
        return prestamoGuardado;
    }


    // Obtener estado solicitud (P5) (por implementar)
    // Método para obtener los estados de los préstamos de un usuario
    public List<PrestamoEntity> obtenerEstadoSolicitudes(Long idUsuario) throws Exception {
        // Verificar si el usuario existe
        UsuarioEntity usuario = usuarioRepository.findById(Math.toIntExact(idUsuario))
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Buscar todas las relaciones usuario-prestamo
        List<UsuarioPrestamoEntity> usuarioPrestamos = usuarioPrestamoRepository.findByIdUsuario(idUsuario);

        // Extraer todos los préstamos asociados
        List<PrestamoEntity> prestamos = new ArrayList<>();
        for (UsuarioPrestamoEntity usuarioPrestamo : usuarioPrestamos) {
            PrestamoEntity prestamo = prestamoRepository.findById(Math.toIntExact(usuarioPrestamo.getIdPrestamo()))
                    .orElseThrow(() -> new Exception("Préstamo no encontrado"));
            prestamos.add(prestamo);
        }

        return prestamos;
    }
}


