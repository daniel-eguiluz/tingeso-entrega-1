package edu.mtisw.payrollbackend.services;

import edu.mtisw.payrollbackend.entities.*;
import edu.mtisw.payrollbackend.repositories.*;
import edu.mtisw.payrollbackend.services.*;
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
    //------------------------------------CRUD----------------------------------------------
    //------------------------------------PRINCIPALES---------------------------------------
    //evaluarRelacionCuotaIngreso()(R1)
    public boolean evaluarRelacionCuotaIngreso(Long idUsuario, Long idPrestamo) throws Exception {
        // Obtener el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(Math.toIntExact(idUsuario))
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(Math.toIntExact(usuarioComprobanteIngresos.getIdComprobanteIngresos()))
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener el ingreso mensual del usuario
        int ingresoMensual = comprobanteIngresos.getIngresoMensual();

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(Math.toIntExact(idPrestamo))
                .orElseThrow(() -> new Exception("Préstamo no encontrado"));

        // Calcular la cuota mensual del préstamo
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

        // Calcular la relación cuota/ingreso en porcentaje
        double relacionCuotaIngreso = (pagoMensual / ingresoMensual) * 100;

        // Verificar si la relación es mayor que el 35%
        if (relacionCuotaIngreso > 35) {
            // La solicitud debe ser rechazada
            return false;
        } else {
            // La solicitud puede continuar
            return true;
        }
    }

    //evaluarDeudas()(R2)
    public boolean evaluarHistorialCrediticio(Long idUsuario) throws Exception {
        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(Math.toIntExact(usuarioComprobanteIngresos.getIdComprobanteIngresos()))
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        int cantidadDeudasPendientes = comprobanteIngresos.getCantidadDeudasPendientes(); // Número de deudas pendientes
        int montoTotalDeudasPendientes = comprobanteIngresos.getDeudas(); // Usamos el atributo 'deudas' existente

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

    //evaluarAntiguedad()(R3)
    // Evaluar Antigüedad Laboral y Estabilidad (R3)
    public boolean evaluarAntiguedad(Long idUsuario) throws Exception {
        // Obtener el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(Math.toIntExact(idUsuario))
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(Math.toIntExact(usuarioComprobanteIngresos.getIdComprobanteIngresos()))
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
            String ingresosUltimos24MesesStr = comprobanteIngresos.getIngresosUltimos24Meses();
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

    //evaluarRelacionDeudaIngreso()(R4)
    public boolean evaluarRelacionDeudaIngreso(Long idUsuario, Long idPrestamo) throws Exception {
        // Obtener el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(Math.toIntExact(idUsuario))
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(Math.toIntExact(usuarioComprobanteIngresos.getIdComprobanteIngresos()))
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener el ingreso mensual del usuario
        int ingresoMensual = comprobanteIngresos.getIngresoMensual();

        // Obtener las deudas actuales del usuario
        int deudasActuales = comprobanteIngresos.getDeudas();

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(Math.toIntExact(idPrestamo))
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

    //evaluarMontoMaximoFinanciamiento()(R5)
    public boolean evaluarMontoMaximoFinanciamiento(Long idPrestamo) throws Exception {
        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(Math.toIntExact(idPrestamo))
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

    //evaluarEdad()(R6)
    // Evaluar Edad del Solicitante (R6)
    public boolean evaluarEdad(Long idUsuario, Long idPrestamo) throws Exception {
        // Obtener el usuario por ID
        UsuarioEntity usuario = usuarioRepository.findById(Math.toIntExact(idUsuario))
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(Math.toIntExact(idPrestamo))
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

    //evaluarSaldoMinimo()(R71)
    public boolean evaluarSaldoMinimo(Long idUsuario, Long idPrestamo) throws Exception {
        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(Math.toIntExact(usuarioComprobanteIngresos.getIdComprobanteIngresos()))
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener el saldo del cliente
        int saldoCliente = comprobanteIngresos.getSaldo();

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(Math.toIntExact(idPrestamo))
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

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(Math.toIntExact(usuarioComprobanteIngresos.getIdComprobanteIngresos()))
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

        // Verificar si hubo retiros significativos (> 50% del saldo)
        for (int i = 1; i < saldosMensuales.size(); i++) {
            double saldoAnterior = saldosMensuales.get(i - 1);
            double saldoActual = saldosMensuales.get(i);

            double disminucion = saldoAnterior - saldoActual;

            if (disminucion > (saldoAnterior * 0.50)) {
                // Se detectó un retiro significativo
                return false;
            }
        }

        // Cumple con el historial de ahorro consistente
        return true;
    }

    //evaluarDepositosPeriodicos(R73)
    public boolean evaluarDepositosPeriodicos(Long idUsuario) throws Exception {
        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(Math.toIntExact(usuarioComprobanteIngresos.getIdComprobanteIngresos()))
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener los depósitos de los últimos 12 meses
        String depositosStr = comprobanteIngresos.getDepositosUltimos12Meses().replace("[", "").replace("]", "");
        String[] depositosArray = depositosStr.split(",");

        if (depositosArray.length < 12) {
            throw new Exception("No hay suficientes datos de depósitos");
        }

        // Convertir a una lista de Double
        List<Double> depositosMensuales = new ArrayList<>();
        for (String depositoStr : depositosArray) {
            depositoStr = depositoStr.trim();
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

    //evaluarRelacionSaldoAntiguedad(R74)
    public boolean evaluarRelacionSaldoAntiguedad(Long idUsuario, Long idPrestamo) throws Exception {
        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(Math.toIntExact(usuarioComprobanteIngresos.getIdComprobanteIngresos()))
                .orElseThrow(() -> new Exception("Comprobante de ingresos no encontrado"));

        // Obtener la antigüedad de la cuenta y el saldo acumulado
        int antiguedadCuenta = comprobanteIngresos.getAntiguedadCuenta();
        int saldoAcumulado = comprobanteIngresos.getSaldo();

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(Math.toIntExact(idPrestamo))
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

    //evaluarRetiroReciente(R75)
    public boolean evaluarRetirosRecientes(Long idUsuario) throws Exception {
        // Obtener el comprobante de ingresos asociado al usuario
        UsuarioComprobanteIngresosEntity usuarioComprobanteIngresos = usuarioComprobanteIngresosRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("Comprobante de ingresos no asociado al usuario"));

        ComprobanteIngresosEntity comprobanteIngresos = comprobanteIngresosRepository.findById(Math.toIntExact(usuarioComprobanteIngresos.getIdComprobanteIngresos()))
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
            String saldoStr = saldosArray[i].trim();
            saldosMensuales.add(Double.parseDouble(saldoStr));
        }

        for (int i = retirosArray.length - 6; i < retirosArray.length; i++) {
            String retiroStr = retirosArray[i].trim();
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

    //evaluarCapacidadAhorro()(R7)
    public Map<String, Object> evaluarCapacidadAhorro(Long idUsuario, Long idPrestamo) throws Exception {
        Map<String, Object> resultado = new HashMap<>();

        int reglasCumplidas = 0;

        // Evaluar R71: Saldo Mínimo Requerido
        boolean r71 = evaluarSaldoMinimo(idUsuario, idPrestamo);
        if (r71) reglasCumplidas++;

        // Evaluar R72: Historial de Ahorro Consistente
        boolean r72 = evaluarHistorialAhorroConsistente(idUsuario);
        if (r72) reglasCumplidas++;

        // Evaluar R73: Depósitos Periódicos
        boolean r73 = evaluarDepositosPeriodicos(idUsuario);
        if (r73) reglasCumplidas++;

        // Evaluar R74: Relación Saldo/Años de Antigüedad
        boolean r74 = evaluarRelacionSaldoAntiguedad(idUsuario, idPrestamo);
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

    //evaluarCredito()(P4)
    public Map<String, Object> evaluarCredito(Long idUsuario, Long idPrestamo) throws Exception {
        Map<String, Object> resultado = new HashMap<>();
        Map<String, Boolean> reglasCumplidas = new HashMap<>();

        boolean aprobado = true;

        // Evaluar R1: Relación Cuota/Ingreso
        boolean r1 = evaluarRelacionCuotaIngreso(idUsuario, idPrestamo);
        reglasCumplidas.put("R1", r1);
        if (!r1) aprobado = false;

        // Evaluar R2: Historial Crediticio del Cliente
        boolean r2 = evaluarHistorialCrediticio(idUsuario);
        reglasCumplidas.put("R2", r2);
        if (!r2) aprobado = false;

        // Evaluar R3: Antigüedad Laboral y Estabilidad
        boolean r3 = evaluarAntiguedad(idUsuario);
        reglasCumplidas.put("R3", r3);
        if (!r3) aprobado = false;

        // Evaluar R4: Relación Deuda/Ingreso
        boolean r4 = evaluarRelacionDeudaIngreso(idUsuario, idPrestamo);
        reglasCumplidas.put("R4", r4);
        if (!r4) aprobado = false;

        // Evaluar R5: Monto Máximo de Financiamiento
        boolean r5 = evaluarMontoMaximoFinanciamiento(idPrestamo);
        reglasCumplidas.put("R5", r5);
        if (!r5) aprobado = false;

        // Evaluar R6: Edad del Solicitante
        boolean r6 = evaluarEdad(idUsuario, idPrestamo);
        reglasCumplidas.put("R6", r6);
        if (!r6) aprobado = false;

        // Evaluar R7: Capacidad de Ahorro
        Map<String, Object> evaluacionAhorro = evaluarCapacidadAhorro(idUsuario, idPrestamo);
        String capacidadAhorro = (String) evaluacionAhorro.get("capacidadAhorro");
        int reglasAhorroCumplidas = (int) evaluacionAhorro.get("reglasCumplidas");

        // Si la capacidad de ahorro es "insuficiente", se rechaza la solicitud
        if ("insuficiente".equals(capacidadAhorro)) {
            aprobado = false;
        }

        // Agregar los resultados al mapa de reglas cumplidas
        reglasCumplidas.put("R7_CapacidadAhorro", !"insuficiente".equals(capacidadAhorro));

        resultado.put("aprobado", aprobado);
        resultado.put("reglasCumplidas", reglasCumplidas);
        resultado.put("capacidadAhorro", capacidadAhorro);
        resultado.put("detallesAhorro", evaluacionAhorro.get("detalles"));

        return resultado;
    }

    //calcularCostoTotales()(P6)
    public Map<String, Object> calcularCostoTotalPrestamo(Long idPrestamo) throws Exception {
        Map<String, Object> resultado = new HashMap<>();

        // Obtener el préstamo por ID
        PrestamoEntity prestamo = prestamoRepository.findById(Math.toIntExact(idPrestamo))
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
