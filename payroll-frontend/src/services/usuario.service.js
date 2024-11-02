import httpClient from "../http-common";

const getAllUsuarios = () => {
    return httpClient.get('/api/v1/usuarios/');
};

const getUsuarioById = (id) => {
    return httpClient.get(`/api/v1/usuarios/${id}`);
};

const saveUsuario = (data) => {
    return httpClient.post('/api/v1/usuarios/', data);
};

const updateUsuario = (data) => {
    return httpClient.put('/api/v1/usuarios/', data);
};

const deleteUsuarioById = (id) => {
    return httpClient.delete(`/api/v1/usuarios/${id}`);
};

const simularCredito = (id) => {
    return httpClient.get(`/api/v1/usuarios/${id}/simular-credito`);
};

const solicitarCredito = (id, data) => {
    return httpClient.post(`/api/v1/usuarios/${id}/solicitar-credito`, data);
};

const obtenerEstadoSolicitud = (id) => {
    return httpClient.get(`/api/v1/usuarios/${id}/estado-solicitud`);
};

export default {
    getAllUsuarios,
    getUsuarioById,
    saveUsuario,
    updateUsuario,
    deleteUsuarioById,
    simularCredito,
    solicitarCredito,
    obtenerEstadoSolicitud
};
