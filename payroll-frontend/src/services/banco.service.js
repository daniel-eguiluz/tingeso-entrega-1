import httpClient from "../http-common";

const evaluarCredito = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-credito/${idUsuario}`);
};

const evaluarRelacionCuotaIngreso = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-relacion-cuota-ingreso/${idUsuario}`);
};

const evaluarHistorialCrediticio = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-historial-crediticio/${idUsuario}`);
};

const evaluarAntiguedad = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-antiguedad/${idUsuario}`);
};

const evaluarRelacionDeudaIngreso = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-relacion-deuda-ingreso/${idUsuario}`);
};

const evaluarMontoMaximoFinanciamiento = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-monto-maximo/${idUsuario}`);
};

const evaluarEdad = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-edad/${idUsuario}`);
};

const evaluarSaldoMinimo = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-saldo-minimo/${idUsuario}`);
};

const evaluarHistorialAhorroConsistente = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-historial-ahorro/${idUsuario}`);
};

const evaluarDepositosPeriodicos = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-depositos-periodicos/${idUsuario}`);
};

const evaluarRelacionSaldoAntiguedad = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-relacion-saldo-antiguedad/${idUsuario}`);
};

const evaluarRetirosRecientes = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-retiros-recientes/${idUsuario}`);
};

const evaluarCapacidadAhorro = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/evaluar-capacidad-ahorro/${idUsuario}`);
};

const calcularCostoTotalPrestamo = (idUsuario) => {
    return httpClient.get(`/api/v1/bancos/calcular-costo-total/${idUsuario}`);
};

export default {
    evaluarCredito,
    evaluarRelacionCuotaIngreso,
    evaluarHistorialCrediticio,
    evaluarAntiguedad,
    evaluarRelacionDeudaIngreso,
    evaluarMontoMaximoFinanciamiento,
    evaluarEdad,
    evaluarSaldoMinimo,
    evaluarHistorialAhorroConsistente,
    evaluarDepositosPeriodicos,
    evaluarRelacionSaldoAntiguedad,
    evaluarRetirosRecientes,
    evaluarCapacidadAhorro,
    calcularCostoTotalPrestamo
};
