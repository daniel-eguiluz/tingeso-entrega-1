import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import usuarioService from "../services/usuario.service.js";
import { useNavigate } from "react-router-dom";

export default function SolicitarCredito() {
  const { id } = useParams(); // Obtener el ID del usuario desde la URL
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    tipo: "",
    plazo: "",
    tasaInteres: "",
    monto: "",
    valorPropiedad: "",
    antiguedadLaboral: "",
    ingresoMensual: "",
    saldo: "",
    deudas: "",
    cantidadDeudasPendientes: "",
    ingresosUltimos24Meses: "",
    saldosMensuales: "",
    depositosUltimos12Meses: "",
    antiguedadCuenta: "",
    retirosUltimos6Meses: ""
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

    try {
      // Convertir las cadenas de listas a arrays
      const requestBody = {
        ...formData,
        ingresosUltimos24Meses: formData.ingresosUltimos24Meses.split(",").map(item => Number(item.trim())),
        saldosMensuales: formData.saldosMensuales.split(",").map(item => Number(item.trim())),
        depositosUltimos12Meses: formData.depositosUltimos12Meses.split(",").map(item => Number(item.trim())),
        retirosUltimos6Meses: formData.retirosUltimos6Meses.split(",").map(item => Number(item.trim()))
      };

      // Enviar la solicitud al backend
      const response = await usuarioService.solicitarCredito(id, requestBody);
      setSuccess("Crédito solicitado exitosamente.");
      // Opcional: Navegar de vuelta a la lista de usuarios después de un retraso
      setTimeout(() => navigate("/usuario/listado"), 2000);
    } catch (error) {
      console.error("Error al solicitar el crédito:", error);
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
      <h1>Solicitar Crédito para Usuario ID: {id}</h1>

      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form onSubmit={handleSubmit}>
        {/* Información del Préstamo */}
        <h3>Datos del Préstamo</h3>
        <div className="mb-3">
          <label htmlFor="tipo" className="form-label">Tipo de Crédito</label>
          <input
            type="text"
            className="form-control"
            id="tipo"
            name="tipo"
            value={formData.tipo}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="plazo" className="form-label">Plazo (años)</label>
          <input
            type="number"
            className="form-control"
            id="plazo"
            name="plazo"
            value={formData.plazo}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="tasaInteres" className="form-label">Tasa de Interés (%)</label>
          <input
            type="number"
            step="0.01"
            className="form-control"
            id="tasaInteres"
            name="tasaInteres"
            value={formData.tasaInteres}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="monto" className="form-label">Monto del Préstamo</label>
          <input
            type="number"
            className="form-control"
            id="monto"
            name="monto"
            value={formData.monto}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="valorPropiedad" className="form-label">Valor de la Propiedad</label>
          <input
            type="number"
            className="form-control"
            id="valorPropiedad"
            name="valorPropiedad"
            value={formData.valorPropiedad}
            onChange={handleChange}
            required
          />
        </div>

        {/* Comprobante de Ingresos */}
        <h3>Comprobante de Ingresos</h3>
        <div className="mb-3">
          <label htmlFor="antiguedadLaboral" className="form-label">Antigüedad Laboral (años)</label>
          <input
            type="number"
            className="form-control"
            id="antiguedadLaboral"
            name="antiguedadLaboral"
            value={formData.antiguedadLaboral}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="ingresoMensual" className="form-label">Ingreso Mensual</label>
          <input
            type="number"
            className="form-control"
            id="ingresoMensual"
            name="ingresoMensual"
            value={formData.ingresoMensual}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="saldo" className="form-label">Saldo</label>
          <input
            type="number"
            className="form-control"
            id="saldo"
            name="saldo"
            value={formData.saldo}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="deudas" className="form-label">Deudas</label>
          <input
            type="number"
            className="form-control"
            id="deudas"
            name="deudas"
            value={formData.deudas}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="cantidadDeudasPendientes" className="form-label">Cantidad de Deudas Pendientes</label>
          <input
            type="number"
            className="form-control"
            id="cantidadDeudasPendientes"
            name="cantidadDeudasPendientes"
            value={formData.cantidadDeudasPendientes}
            onChange={handleChange}
            required
          />
        </div>

        {/* Campos de Listas */}
        <div className="mb-3">
          <label htmlFor="ingresosUltimos24Meses" className="form-label">Ingresos Últimos 24 Meses (separados por comas)</label>
          <input
            type="text"
            className="form-control"
            id="ingresosUltimos24Meses"
            name="ingresosUltimos24Meses"
            value={formData.ingresosUltimos24Meses}
            onChange={handleChange}
            placeholder="e.g., 850000,820000,840000,..."
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="saldosMensuales" className="form-label">Saldos Mensuales (separados por comas)</label>
          <input
            type="text"
            className="form-control"
            id="saldosMensuales"
            name="saldosMensuales"
            value={formData.saldosMensuales}
            onChange={handleChange}
            placeholder="e.g., 1500000,1550000,1600000,..."
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="depositosUltimos12Meses" className="form-label">Depósitos Últimos 12 Meses (separados por comas)</label>
          <input
            type="text"
            className="form-control"
            id="depositosUltimos12Meses"
            name="depositosUltimos12Meses"
            value={formData.depositosUltimos12Meses}
            onChange={handleChange}
            placeholder="e.g., 200000,220000,230000,..."
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="antiguedadCuenta" className="form-label">Antigüedad de la Cuenta (meses)</label>
          <input
            type="number"
            className="form-control"
            id="antiguedadCuenta"
            name="antiguedadCuenta"
            value={formData.antiguedadCuenta}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="retirosUltimos6Meses" className="form-label">Retiros Últimos 6 Meses (separados por comas)</label>
          <input
            type="text"
            className="form-control"
            id="retirosUltimos6Meses"
            name="retirosUltimos6Meses"
            value={formData.retirosUltimos6Meses}
            onChange={handleChange}
            placeholder="e.g., 50000,45000,60000,..."
            required
          />
        </div>

        <button type="submit" className="btn btn-success">Enviar Solicitud</button>
      </form>
    </div>
  );
}
