import { useState } from "react";
import { useNavigate } from "react-router-dom";
import usuarioService from "../services/usuario.service.js";

export default function AddUsuario() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    rut: "",
    nombre: "",
    apellido: "",
    edad: "",
    tipoEmpleado: ""
  });

  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  // Manejar cambios en los campos del formulario
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Manejar el envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    // Validación básica (puedes mejorarla según tus necesidades)
    if (!formData.rut || !formData.nombre || !formData.apellido || !formData.edad || !formData.tipoEmpleado) {
      setError("Todos los campos son obligatorios.");
      return;
    }

    try {
      // Crear objeto de usuario para enviar al backend
      const newUsuario = {
        rut: formData.rut,
        nombre: formData.nombre,
        apellido: formData.apellido,
        edad: parseInt(formData.edad),
        tipoEmpleado: formData.tipoEmpleado
      };

      // Enviar solicitud al backend para guardar el nuevo usuario
      const response = await usuarioService.saveUsuario(newUsuario);
      setSuccess("Usuario agregado exitosamente.");
      
      // Opcional: Navegar de vuelta a la lista de usuarios después de un retraso
      setTimeout(() => navigate("/usuario/listado"), 2000);
    } catch (error) {
      console.error("Error al agregar el usuario:", error);
      if (error.response) {
        setError(`Error: ${error.response.status} - ${error.response.data}`);
      } else if (error.request) {
        setError("Error: No se recibió respuesta del servidor.");
      } else {
        setError(`Error: ${error.message}`);
      }
    }
  };

  return (
    <div className="container">
      <h1>Agregar Nuevo Usuario</h1>

      {/* Mensajes de Error y Éxito */}
      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      {/* Formulario de Agregar Usuario */}
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="rut" className="form-label">RUT</label>
          <input
            type="text"
            className="form-control"
            id="rut"
            name="rut"
            value={formData.rut}
            onChange={handleChange}
            placeholder="12345678-9"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="nombre" className="form-label">Nombre</label>
          <input
            type="text"
            className="form-control"
            id="nombre"
            name="nombre"
            value={formData.nombre}
            onChange={handleChange}
            placeholder="Juan"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="apellido" className="form-label">Apellido</label>
          <input
            type="text"
            className="form-control"
            id="apellido"
            name="apellido"
            value={formData.apellido}
            onChange={handleChange}
            placeholder="Pérez"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="edad" className="form-label">Edad</label>
          <input
            type="number"
            className="form-control"
            id="edad"
            name="edad"
            value={formData.edad}
            onChange={handleChange}
            placeholder="35"
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="tipoEmpleado" className="form-label">Tipo de Empleado</label>
          <select
            className="form-select"
            id="tipoEmpleado"
            name="tipoEmpleado"
            value={formData.tipoEmpleado}
            onChange={handleChange}
            required
          >
            <option value="">Seleccione...</option>
            <option value="Empleado">Empleado</option>
            <option value="Independiente">Independiente</option>
          </select>
        </div>

        <button type="submit" className="btn btn-primary">Agregar Usuario</button>
      </form>
    </div>
  );
}
