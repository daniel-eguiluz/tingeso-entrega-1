// src/App.js

import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from "./components/Navbar";
import Home from './components/Home';
import EmployeeList from './components/EmployeesList';
import AddEditEmployee from './components/AddEditEmployee';
import ExtraHoursList from './components/ExtraHoursList';
import AddEditExtraHours from './components/AddEditExtraHours';
import NotFound from './components/NotFound';
import PaycheckList from './components/PaycheckList';
import PaycheckCalculate from './components/PaycheckCalculate';
import AnualReport from './components/AnualReport';
import ListadoUsuarios from './components/ListadoUsuarios';
import SolicitarCredito from './components/SolicitarCredito';
import AddUsuario from './components/AddUsuario';
import SimularCredito from './components/SimularCredito';
import EvaluarCredito from './components/EvaluarCredito';
import ObtenerEstadoSolicitud from './components/ObtenerEstadoSolicitud';
import CalcularCostoTotal from './components/CalcularCostoTotal'; // Nuevo componente

function App() {
  return (
      <Router>
          <div className="container">
            <Navbar />
            <Routes>
              <Route path="/home" element={<Home />} />
              <Route path="/employee/list" element={<EmployeeList />} />
              <Route path="/employee/add" element={<AddEditEmployee />} />
              <Route path="/employee/edit/:id" element={<AddEditEmployee />} />
              <Route path="/paycheck/list" element={<PaycheckList />} />
              <Route path="/paycheck/calculate" element={<PaycheckCalculate />} />
              <Route path="/reports/AnualReport" element={<AnualReport />} />
              <Route path="/extraHours/list" element={<ExtraHoursList />} />
              <Route path="/extraHours/add" element={<AddEditExtraHours />} />
              <Route path="/extraHours/edit/:id" element={<AddEditExtraHours />} />
              <Route path="/usuario/listado" element={<ListadoUsuarios />} />
              <Route path="/usuarios/:id/solicitar-credito" element={<SolicitarCredito />} />
              <Route path="/usuarios/add" element={<AddUsuario />} />
              <Route path="/usuarios/:id/simular-credito" element={<SimularCredito />} />
              <Route path="/usuarios/:id/evaluar-credito" element={<EvaluarCredito />} />
              <Route path="/usuarios/:id/estado-solicitud" element={<ObtenerEstadoSolicitud />} />
              <Route path="/usuarios/:id/calcular-costo-total" element={<CalcularCostoTotal />} /> {/* Nueva Ruta */}
              <Route path="*" element={<NotFound />} />
            </Routes>
          </div>
      </Router>
  );
}

export default App;
