package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.*;
import edu.mtisw.payrollbackend.repositories.*;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BancoService {
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
    @Autowired
    UsuarioService usuarioService;

    // ------------------------------------CRUD----------------------------------------------
    // ------------------------------------PRINCIPALES---------------------------------------

    // Evaluar Relación Cuota/Ingreso (R1)
    public boolean evaluarRelacionCuotaIngreso(Long idUsuario) throws Exception {
        System.out.println("Evaluando relación cuota ingreso para usuario: " + idUsuario);

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        System.out.println("Usuario encontrado: " + usuario);

        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));
        System.out.println("Comprobante de ingresos encontrado: " + usuarioComprobanteIngresos);

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(usuarioComprobanteIngresos.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));
        System.out.println("Ingreso mensual: " + comprobanteIngresos.getIngresoMensual());

        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));

        PrestamoEntity prestamo = prestamoRepository.findById(usuarioPrestamo.getIdPrestamo())
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));
        System.out.println("Préstamo encontrado: " + prestamo);

        // Cálculo de la relación cuota ingreso
        double tasaInteresMensual = (prestamo.getTasaInteres() / 12) / 100;
        int numeroPagos = prestamo.getPlazo() * 12;
        double pagoMensual = (prestamo.getMonto() * tasaInteresMensual * Math.pow(1 + tasaInteresMensual, numeroPagos)) /
                (Math.pow(1 + tasaInteresMensual, numeroPagos) - 1);
        double relacionCuotaIngreso = (pagoMensual / comprobanteIngresos.getIngresoMensual()) * 100;
        System.out.println("Relación cuota ingreso: " + relacionCuotaIngreso);

        return relacionCuotaIngreso <= 35;
    }

    // Evaluar Historial Crediticio del Cliente (R2)
    public boolean evaluarHistorialCrediticio(Long idUsuario) throws Exception {
        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));
        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(usuarioComprobanteIngresos.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        int cantidadDeudasPendientes = comprobanteIngresos.getCantidadDeudasPendientes(); // Número de deudas pendientes
        int montoTotalDeudasPendientes = comprobanteIngresos.getDeudas(); // Monto total de deudas pendientes

        // Obtener el ingreso mensual del cliente
        int ingresoMensual = comprobanteIngresos.getIngresoMensual();

        // Calcular el porcentaje de las deudas pendientes sobre los ingresos mensuales
        double porcentajeDeudasSobreIngresos = ((double) montoTotalDeudasPendientes / ingresoMensual) * 100;

        // Definir umbrales para rechazar la solicitud
        boolean demasiadasDeudasPendientes = cantidadDeudasPendientes > 3; // Umbral de 3 deudas pendientes
        boolean deudasExcesivas = porcentajeDeudasSobreIngresos > 30; // Umbral del 30% sobre ingresos

        if (demasiadasDeudasPendientes || deudasExcesivas) {
            // Si tiene demasiadas deudas pendientes o las deudas superan el 30% de los ingresos, se rechaza la solicitud
            return false;
        }
        // Si las deudas pendientes están dentro de un rango aceptable, se acepta
        return true;
    }

    // Evaluar Antigüedad Laboral y Estabilidad (R3)
    public boolean evaluarAntiguedad(Long idUsuario) throws Exception {
        // Obtener el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));
        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(usuarioComprobanteIngresos.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        if (usuario.getTipoEmpleado().equalsIgnoreCase("Empleado")) {
            // Si es empleado, verificar que tenga al menos 1 año de antigüedad laboral
            int antiguedadLaboral = comprobanteIngresos.getAntiguedadLaboral();
            if (antiguedadLaboral >= 1) {
                return true; // Cumple con la antigüedad requerida
            } else {
                return false; // No cumple con la antigüedad requerida
            }
        } else if (usuario.getTipoEmpleado().equalsIgnoreCase("Independiente")) {
            // Si es independiente, revisar ingresos de los últimos 2 años
            String ingresosUltimos24MesesStr = comprobanteIngresos.getIngresosUltimos24Meses().replace("[", "").replace("]", "");
            String[] ingresosArray = ingresosUltimos24MesesStr.split(",");
            if (ingresosArray.length >= 24) {
                // Tiene registros de ingresos de los últimos 24 meses
                // Aquí podrías evaluar la estabilidad financiera según tus criterios
                // Por simplicidad, asumiremos que cumple si tiene ingresos en los últimos 24 meses
                return true;
            } else {
                return false; // No tiene suficientes datos de ingresos
            }
        } else {
            throw new Exception("Tipo de empleado desconocido");
        }
    }

    // Evaluar Relación Deuda/Ingreso (R4)
    public boolean evaluarRelacionDeudaIngreso(Long idUsuario) throws Exception {
        // Obtener el préstamo asociado al usuario
        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));
        Long idPrestamo = usuarioPrestamo.getIdPrestamo();

        // Obtener el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));
        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(usuarioComprobanteIngresos.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener el ingreso mensual del usuario
        int ingresoMensual = comprobanteIngresos.getIngresoMensual();

        // Obtener las deudas actuales del usuario
        int deudasActuales = comprobanteIngresos.getDeudas();

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));

        // Calcular la cuota mensual del nuevo préstamo
        double tasaInteresAnual = prestamo.getTasaInteres();
        int plazoEnAnios = prestamo.getPlazo();
        int monto = prestamo.getMonto();

        // Calcular la tasa de interés mensual
        double tasaInteresMensual = (tasaInteresAnual / 12) / 100;

        // Número de pagos (meses)
        int numeroPagos = plazoEnAnios * 12;

        // Cálculo del pago mensual usando la fórmula de amortización
        double cuotaNueva = (monto * tasaInteresMensual * Math.pow(1 + tasaInteresMensual, numeroPagos)) /
                (Math.pow(1 + tasaInteresMensual, numeroPagos) - 1);

        // Sumar la cuota nueva a las deudas actuales
        double totalDeudas = deudasActuales + cuotaNueva;

        // Calcular la relación deuda/ingreso en porcentaje
        double relacionDeudaIngreso = (totalDeudas / ingresoMensual) * 100;

        // Verificar si la relación es mayor que el 50%
        if (relacionDeudaIngreso > 50) {
            // La solicitud debe ser rechazada
            return false;
        } else {
            // La solicitud puede continuar
            return true;
        }
    }

    // Evaluar Monto Máximo de Financiamiento (R5)
    public boolean evaluarMontoMaximoFinanciamiento(Long idUsuario) throws Exception {
        // Obtener el préstamo asociado al usuario
        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));
        Long idPrestamo = usuarioPrestamo.getIdPrestamo();

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));
        String tipoPrestamo = prestamo.getTipo();
        int valorPropiedad = prestamo.getValorPropiedad();
        int montoSolicitado = prestamo.getMonto();

        // Definir el porcentaje máximo según el tipo de préstamo
        double porcentajeMaximo;
        switch (tipoPrestamo.toLowerCase()) {
            case "primera vivienda":
                porcentajeMaximo = 0.80; // 80%
                break;
            case "segunda vivienda":
                porcentajeMaximo = 0.70; // 70%
                break;
            case "propiedades comerciales":
                porcentajeMaximo = 0.60; // 60%
                break;
            case "remodelacion":
                porcentajeMaximo = 0.50; // 50%
                break;
            default:
                throw new Exception("Tipo de préstamo desconocido");
        }

        // Calcular el monto máximo permitido
        double montoMaximoPermitido = valorPropiedad * porcentajeMaximo;

        // Verificar si el monto solicitado excede el máximo permitido
        if (montoSolicitado <= montoMaximoPermitido) {
            // La solicitud puede continuar
            return true;
        } else {
            // La solicitud debe ser rechazada
            return false;
        }
    }

    // Evaluar Edad del Solicitante (R6)
    public boolean evaluarEdad(Long idUsuario) throws Exception {
        // Obtener el préstamo asociado al usuario
        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));
        Long idPrestamo = usuarioPrestamo.getIdPrestamo();

        // Obtener el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));

        int edadActual = usuario.getEdad();
        int plazoPrestamo = prestamo.getPlazo(); // En años

        // Calcular la edad al finalizar el préstamo
        int edadAlFinalizarPrestamo = edadActual + plazoPrestamo;

        // Verificar si la edad al finalizar el préstamo excede el límite
        if (edadAlFinalizarPrestamo >= 70) {
            // La solicitud debe ser rechazada
            return false;
        } else {
            // La solicitud puede continuar
            return true;
        }
    }

    // Evaluar Saldo Mínimo Requerido (R71)
    public boolean evaluarSaldoMinimo(Long idUsuario) throws Exception {
        // Obtener el préstamo asociado al usuario
        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));
        Long idPrestamo = usuarioPrestamo.getIdPrestamo();

        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));
        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(usuarioComprobanteIngresos.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener el saldo del cliente
        int saldoCliente = comprobanteIngresos.getSaldo();

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));

        // Obtener el monto del préstamo solicitado
        int montoPrestamo = prestamo.getMonto();

        // Calcular el 10% del monto del préstamo
        double montoRequerido = montoPrestamo * 0.10;

        // Verificar si el saldo del cliente cumple con el mínimo requerido
        if (saldoCliente >= montoRequerido) {
            // El cliente cumple con el saldo mínimo requerido
            return true;
        } else {
            // El cliente no cumple con el saldo mínimo requerido
            return false;
        }
    }

    //evaluarHistorialAhorroConsistente(R72)
    public boolean evaluarHistorialAhorroConsistente(Long idUsuario) throws Exception {
        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));
        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(usuarioComprobanteIngresos.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));
        // Obtener los saldos mensuales
        String saldosMensualesStr = comprobanteIngresos.getSaldosMensuales();
        String[] saldosArray = saldosMensualesStr.split(",");
        if (saldosArray.length < 12) {
            throw new Exception("No hay suficientes datos de saldos mensuales");
        }
        // Convertir a una lista de enteros
        List<Double> saldosMensuales = new ArrayList<>();
        for (String saldoStr : saldosArray) {
            saldosMensuales.add(Double.parseDouble(saldoStr));
        }
        // Verificar que el saldo haya sido positivo durante los últimos 12 meses
        for (Double saldo : saldosMensuales) {
            if (saldo <= 0) {
                // Se encontró un saldo no positivo
                return false;
            }
        }
        // No se encontraron saldos negativos en los últimos 12 meses
        return true;
    }

    // Evaluar Depósitos Periódicos (R73)
    public boolean evaluarDepositosPeriodicos(Long idUsuario) throws Exception {
        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));
        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(usuarioComprobanteIngresos.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener los depósitos de los últimos 12 meses y limpiar la cadena
        String depositosStr = comprobanteIngresos.getDepositosUltimos12Meses().replace("[", "").replace("]", "");
        String[] depositosArray = depositosStr.split(",");
        if (depositosArray.length < 12) {
            throw new Exception("No hay suficientes datos de depósitos");
        }

        // Convertir a una lista de Double
        List<Double> depositosMensuales = new ArrayList<>();
        for (String depositoStr : depositosArray) {
            depositoStr = depositoStr.trim(); // Eliminar espacios en blanco
            depositosMensuales.add(Double.parseDouble(depositoStr));
        }

        // Obtener el ingreso mensual
        int ingresoMensual = comprobanteIngresos.getIngresoMensual();
        double montoMinimoDeposito = ingresoMensual * 0.05;

        // Variables para contar depósitos regulares
        int depositosMensualesCount = 0;
        int depositosTrimestralesCount = 0;

        // Verificar depósitos mensuales
        for (Double deposito : depositosMensuales) {
            if (deposito >= montoMinimoDeposito) {
                depositosMensualesCount++;
            }
        }

        // Verificar si hay al menos 12 depósitos mensuales
        if (depositosMensualesCount >= 12) {
            return true; // Cumple con depósitos mensuales regulares
        }

        // Verificar depósitos trimestrales
        for (int i = 0; i < depositosMensuales.size(); i += 3) {
            double sumaTrimestre = depositosMensuales.get(i);
            if (i + 1 < depositosMensuales.size()) sumaTrimestre += depositosMensuales.get(i + 1);
            if (i + 2 < depositosMensuales.size()) sumaTrimestre += depositosMensuales.get(i + 2);
            if (sumaTrimestre >= montoMinimoDeposito * 3) {
                depositosTrimestralesCount++;
            }
        }

        // Verificar si hay al menos 4 depósitos trimestrales
        if (depositosTrimestralesCount >= 4) {
            return true; // Cumple con depósitos trimestrales regulares
        }

        // No cumple con la regularidad o monto mínimo
        return false;
    }

    // Evaluar Relación Saldo/Antigüedad (R74)
    public boolean evaluarRelacionSaldoAntiguedad(Long idUsuario) throws Exception {
        // Obtener el préstamo asociado al usuario
        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));
        Long idPrestamo = usuarioPrestamo.getIdPrestamo();

        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));
        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(usuarioComprobanteIngresos.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener la antigüedad de la cuenta y el saldo acumulado
        int antiguedadCuenta = comprobanteIngresos.getAntiguedadCuenta();
        int saldoAcumulado = comprobanteIngresos.getSaldo();

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));

        // Obtener el monto del préstamo solicitado
        int montoPrestamo = prestamo.getMonto();

        // Determinar el porcentaje requerido según la antigüedad
        double porcentajeRequerido = (antiguedadCuenta < 2) ? 0.20 : 0.10;

        // Calcular el monto requerido
        double montoRequerido = montoPrestamo * porcentajeRequerido;

        // Verificar si el saldo acumulado cumple con el monto requerido
        if (saldoAcumulado >= montoRequerido) {
            // El cliente cumple con la relación saldo/antigüedad
            return true;
        } else {
            // No cumple con los porcentajes requeridos
            return false;
        }
    }

    // Evaluar Retiros Recientes (R75)
    public boolean evaluarRetirosRecientes(Long idUsuario) throws Exception {
        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));
        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(usuarioComprobanteIngresos.getIdComprobanteIngresos())
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener los retiros de los últimos 6 meses y limpiar la cadena
        String retirosStr = comprobanteIngresos.getRetirosUltimos6Meses().replace("[", "").replace("]", "");
        String[] retirosArray = retirosStr.split(",");
        if (retirosArray.length < 6) {
            throw new Exception("No hay suficientes datos de retiros");
        }

        // Obtener los saldos mensuales y limpiar la cadena
        String saldosMensualesStr = comprobanteIngresos.getSaldosMensuales().replace("[", "").replace("]", "");
        String[] saldosArray = saldosMensualesStr.split(",");
        if (saldosArray.length < 6) {
            throw new Exception("No hay suficientes datos de saldos mensuales");
        }

        // Tomar los últimos 6 saldos y retiros
        List<Double> retirosMensuales = new ArrayList<>();
        List<Double> saldosMensuales = new ArrayList<>();
        for (int i = saldosArray.length - 6; i < saldosArray.length; i++) {
            String saldoStr = saldosArray[i].trim(); // Eliminar espacios en blanco
            saldosMensuales.add(Double.parseDouble(saldoStr));
        }
        for (int i = retirosArray.length - 6; i < retirosArray.length; i++) {
            String retiroStr = retirosArray[i].trim(); // Eliminar espacios en blanco
            retirosMensuales.add(Double.parseDouble(retiroStr));
        }

        // Verificar si hay algún retiro superior al 30% del saldo correspondiente
        for (int i = 0; i < 6; i++) {
            double saldo = saldosMensuales.get(i);
            double retiro = retirosMensuales.get(i);
            if (saldo == 0) {
                // Evitar división por cero
                continue;
            }
            double porcentajeRetiro = (retiro / saldo) * 100;
            if (porcentajeRetiro > 30) {
                // Se ha realizado un retiro superior al 30% del saldo
                return false;
            }
        }

        // No se encontraron retiros superiores al 30% del saldo en los últimos 6 meses
        return true;
    }

    // Evaluar Capacidad de Ahorro (R7)
    public Map<String, Object> evaluarCapacidadAhorro(Long idUsuario) throws Exception {
        Map<String, Object> resultado = new HashMap<>();
        int reglasCumplidas = 0;

        // Evaluar R71: Saldo Mínimo Requerido
        boolean r71 = evaluarSaldoMinimo(idUsuario);
        if (r71) reglasCumplidas++;

        // Evaluar R72: Historial de Ahorro Consistente
        boolean r72 = evaluarHistorialAhorroConsistente(idUsuario);
        if (r72) reglasCumplidas++;

        // Evaluar R73: Depósitos Periódicos
        boolean r73 = evaluarDepositosPeriodicos(idUsuario);
        if (r73) reglasCumplidas++;

        // Evaluar R74: Relación Saldo/Años de Antigüedad
        boolean r74 = evaluarRelacionSaldoAntiguedad(idUsuario);
        if (r74) reglasCumplidas++;

        // Evaluar R75: Retiros Recientes
        boolean r75 = evaluarRetirosRecientes(idUsuario);
        if (r75) reglasCumplidas++;

        String capacidadAhorro;
        if (reglasCumplidas == 5) {
            capacidadAhorro = "sólida";
        } else if (reglasCumplidas >= 3) {
            capacidadAhorro = "moderada";
        } else {
            capacidadAhorro = "insuficiente";
        }

        resultado.put("capacidadAhorro", capacidadAhorro);
        resultado.put("reglasCumplidas", reglasCumplidas);
        resultado.put("detalles", Map.of(
                "R71", r71,
                "R72", r72,
                "R73", r73,
                "R74", r74,
                "R75", r75
        ));
        return resultado;
    }

    // Evaluar Crédito (P4)
    public Map<String, Object> evaluarCredito(Long idUsuario) throws Exception {
        Map<String, Object> resultado = new HashMap<>();
        Map<String, Boolean> reglasCumplidas = new HashMap<>();
        boolean aprobado = true;

        // Obtener el préstamo asociado al usuario
        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));
        Long idPrestamo = usuarioPrestamo.getIdPrestamo();

        // Evaluar R1: Relación Cuota/Ingreso
        boolean r1 = evaluarRelacionCuotaIngreso(idUsuario);
        reglasCumplidas.put("Relación Cuota/Ingreso Aprobada", r1);
        if (!r1) aprobado = false;

        // Evaluar R2: Historial Crediticio del Cliente
        boolean r2 = evaluarHistorialCrediticio(idUsuario);
        reglasCumplidas.put("Historial Crediticio del Cliente Aprobado", r2);
        if (!r2) aprobado = false;

        // Evaluar R3: Antigüedad Laboral y Estabilidad
        boolean r3 = evaluarAntiguedad(idUsuario);
        reglasCumplidas.put("Antigüedad Laboral y Estabilidad Aprobada", r3);
        if (!r3) aprobado = false;

        // Evaluar R4: Relación Deuda/Ingreso
        boolean r4 = evaluarRelacionDeudaIngreso(idUsuario);
        reglasCumplidas.put("Relación Deuda/Ingreso Aprobada", r4);
        if (!r4) aprobado = false;

        // Evaluar R5: Monto Máximo de Financiamiento
        boolean r5 = evaluarMontoMaximoFinanciamiento(idUsuario);
        reglasCumplidas.put("Monto Máximo de Financiamiento Aprobado", r5);
        if (!r5) aprobado = false;

        // Evaluar R6: Edad del Solicitante
        boolean r6 = evaluarEdad(idUsuario);
        reglasCumplidas.put("Edad del Solicitante Aprobada", r6);
        if (!r6) aprobado = false;

        // Evaluar R7: Capacidad de Ahorro
        Map<String, Object> evaluacionAhorro = evaluarCapacidadAhorro(idUsuario);
        String capacidadAhorro = (String) evaluacionAhorro.get("capacidadAhorro");
        int reglasAhorroCumplidas = (int) evaluacionAhorro.get("reglasCumplidas");

        // Si la capacidad de ahorro es "insuficiente", se rechaza la solicitud
        if ("insuficiente".equals(capacidadAhorro)) {
            aprobado = false;
        }

        // Agregar los resultados al mapa de reglas cumplidas
        reglasCumplidas.put("Capacidad de Ahorro Aprobada", !"insuficiente".equals(capacidadAhorro));

        // Agregar toda la información al resultado
        resultado.put("aprobado", aprobado);
        resultado.put("reglasCumplidas", reglasCumplidas);
        resultado.put("capacidadAhorro", capacidadAhorro);
        resultado.put("detallesAhorro", evaluacionAhorro.get("detalles"));
        return resultado;
    }

    // Calcular Costos Totales (P6)
    public Map<String, Object> calcularCostoTotalPrestamo(Long idUsuario) throws Exception {
        Map<String, Object> resultado = new HashMap<>();

        // Obtener el préstamo asociado al usuario
        UsuarioPrestamoEntity usuarioPrestamo = usuarioPrestamoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Préstamo no asociado al usuario"));
        PrestamoEntity prestamo = prestamoRepository.findById(usuarioPrestamo.getIdPrestamo())
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));

        double montoPrestamo = prestamo.getMonto();
        int plazoAnios = prestamo.getPlazo();
        double tasaInteresAnual = prestamo.getTasaInteres();

        // Paso 1: Cálculo de la Cuota Mensual del Préstamo
        double tasaInteresMensual = tasaInteresAnual / 12 / 100; // Convertir a decimal
        int numeroPagos = plazoAnios * 12;

        // Fórmula de amortización
        double cuotaMensual = (montoPrestamo * tasaInteresMensual * Math.pow(1 + tasaInteresMensual, numeroPagos)) /
                (Math.pow(1 + tasaInteresMensual, numeroPagos) - 1);

        // Paso 2: Cálculo de los Seguros
        // Seguro de desgravamen: 0.03% del monto del préstamo por mes
        double seguroDesgravamenMensual = montoPrestamo * 0.0003; // 0.03% = 0.0003

        // Seguro de incendio: $20,000 mensuales
        double seguroIncendioMensual = 20000;

        // Paso 3: Cálculo de la Comisión por Administración
        // Comisión por administración: 1% del monto del préstamo
        double comisionAdministracion = montoPrestamo * 0.01; // 1% = 0.01

        // Paso 4: Cálculo del Costo Total del Préstamo
        // Costo mensual total: Cuota mensual + Seguros
        double costoMensualTotal = cuotaMensual + seguroDesgravamenMensual + seguroIncendioMensual;

        // Costo total durante la vida del préstamo
        double costoTotal = (costoMensualTotal * numeroPagos) + comisionAdministracion;

        // Redondear los valores a dos decimales
        cuotaMensual = Math.round(cuotaMensual * 100.0) / 100.0;
        seguroDesgravamenMensual = Math.round(seguroDesgravamenMensual * 100.0) / 100.0;
        costoMensualTotal = Math.round(costoMensualTotal * 100.0) / 100.0;
        costoTotal = Math.round(costoTotal * 100.0) / 100.0;
        comisionAdministracion = Math.round(comisionAdministracion * 100.0) / 100.0;

        // Paso 5: Preparar el resultado
        resultado.put("montoPrestamo", montoPrestamo);
        resultado.put("plazoAnios", plazoAnios);
        resultado.put("tasaInteresAnual", tasaInteresAnual);
        resultado.put("tasaInteresMensual", tasaInteresMensual * 100); // Convertir a porcentaje
        resultado.put("cuotaMensual", cuotaMensual);
        resultado.put("seguroDesgravamenMensual", seguroDesgravamenMensual);
        resultado.put("seguroIncendioMensual", seguroIncendioMensual);
        resultado.put("comisionAdministracion", comisionAdministracion);
        resultado.put("costoMensualTotal", costoMensualTotal);
        resultado.put("costoTotal", costoTotal);
        resultado.put("numeroPagos", numeroPagos);
        return resultado;
    }
}
