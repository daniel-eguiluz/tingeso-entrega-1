import axios from "axios";

/*
const payrollBackendServer = "localhost"; // O el hostname correspondiente
const payrollBackendPort = "8090"; // Según tu application.properties

console.log(payrollBackendServer); // Debería imprimir 'localhost'
console.log(payrollBackendPort);   // Debería imprimir '8090'

export default axios.create({
    baseURL: `http://${payrollBackendServer}:${payrollBackendPort}`,
    headers: {
        'Content-Type': 'application/json'
    }
});
*/


const payrollBackendServer = import.meta.env.VITE_PAYROLL_BACKEND_SERVER;
const payrollBackendPort = import.meta.env.VITE_PAYROLL_BACKEND_PORT;

console.log(payrollBackendServer)
console.log(payrollBackendPort)

export default axios.create({
    baseURL: `http://${payrollBackendServer}:${payrollBackendPort}`,
    headers: {
        'Content-Type': 'application/json'
    }
});
