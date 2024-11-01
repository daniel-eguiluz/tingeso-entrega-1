package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.*;
import edu.mtisw.payrollbackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public UsuarioEntity getUsuarioById(Long id){
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
    public boolean deleteUsuario(Long id) throws Exception {
        try{
            usuarioRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    //------------------------------------PRINCIPALES---------------------------------------

    // Simular Crédito (P1)
    public Map<String, Object> simularCredito(Long idUsuario) throws Exception {
        // Obtener el préstamo asociado al usuario
        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));

        PrestamoEntity prestamo = prestamoRepository.findById(usuarioPrestamo.getIdPrestamo())
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

    // Solicitar Crédito (P3)
    public PrestamoEntity solicitarCredito(Long idUsuario, PrestamoEntity prestamo, ComprobanteIngresosEntity comprobanteIngresos) throws Exception {
        // Obtener el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Manejo del comprobante de ingresos
        Optional<UsuarioComprobanteIngresosEntity> usuarioComprobanteIngresosOpt = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario);

        ComprobanteIngresosEntity comprobanteIngresosGuardado;
        if (usuarioComprobanteIngresosOpt.isPresent()) {
            // Actualizar el comprobante de ingresos existente
            UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosOpt.get();
            comprobanteIngresos.setId(usuarioComprobanteIngresos.getIdComprobanteIngresos());
            comprobanteIngresosGuardado = comprobanteIngresosRepository.save(comprobanteIngresos);
        } else {
            // Guardar el nuevo comprobante de ingresos
            comprobanteIngresosGuardado = comprobanteIngresosRepository.save(comprobanteIngresos);
            // Asociar al usuario
            UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = new UsuarioComprobanteIngresosEntity();
            usuarioComprobanteIngresos.setIdUsuario(usuario.getId());
            usuarioComprobanteIngresos.setIdComprobanteIngresos(comprobanteIngresosGuardado.getId());
            usuarioComprobanteIngresosRepository.save(usuarioComprobanteIngresos);
        }

        // Manejo del préstamo
        Optional<UsuarioPrestamoEntity> usuarioPrestamoOpt = usuarioPrestamoRepository.findByIdUsuario(idUsuario);

        PrestamoEntity prestamoGuardado;
        if (usuarioPrestamoOpt.isPresent()) {
            // Actualizar el préstamo existente
            UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoOpt.get();
            prestamo.setId(usuarioPrestamo.getIdPrestamo());
            prestamoGuardado = prestamoRepository.save(prestamo);
        } else {
            // Guardar el nuevo préstamo
            prestamo.setEstado("En proceso");
            prestamoGuardado = prestamoRepository.save(prestamo);
            // Asociar al usuario
            UsuarioPrestamoEntity usuarioPrestamo = new UsuarioPrestamoEntity();
            usuarioPrestamo.setIdUsuario(usuario.getId());
            usuarioPrestamo.setIdPrestamo(prestamoGuardado.getId());
            usuarioPrestamoRepository.save(usuarioPrestamo);
        }

        // Retornar el préstamo guardado
        return prestamoGuardado;
    }

    // Obtener estado de la solicitud (P5)
    public PrestamoEntity obtenerEstadoSolicitud(Long idUsuario) throws Exception {
        // Verificar si el usuario existe
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Obtener el préstamo asociado al usuario
        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));

        PrestamoEntity prestamo = prestamoRepository.findById(usuarioPrestamo.getIdPrestamo())
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));

        return prestamo;
    }
}
