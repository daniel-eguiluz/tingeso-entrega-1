-- Insertar datos en la tabla usuario
INSERT INTO usuario (rut, nombre, apellido, edad) VALUES ('12345678-9', 'Juan', 'Pérez', 35);
INSERT INTO usuario (rut, nombre, apellido, edad) VALUES ('98765432-1', 'María', 'González', 28);

-- Insertar datos en la tabla comprobante_ingresos
INSERT INTO comprobante_ingresos (antiguedad_laboral, ingreso_mensual, saldo) VALUES (5, 1500000, 20000000);
INSERT INTO comprobante_ingresos (antiguedad_laboral, ingreso_mensual, saldo) VALUES (3, 1200000, 15000000);

-- Asociar usuarios con comprobantes de ingresos
INSERT INTO usuario_comprobante_ingresos (id_usuario, id_comprobante_ingresos) VALUES (1, 1);
INSERT INTO usuario_comprobante_ingresos (id_usuario, id_comprobante_ingresos) VALUES (2, 2);

-- Insertar datos en gastos_ultimos_12_meses para comprobante_ingresos_id = 1
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 1, 500000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 2, 520000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 3, 480000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 4, 510000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 5, 530000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 6, 490000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 7, 500000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 8, 520000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 9, 480000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 10, 510000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 11, 530000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (1, 12, 600000);

-- Insertar datos en ingresos_ultimos_12_meses para comprobante_ingresos_id = 1
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 1, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 2, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 3, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 4, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 5, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 6, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 7, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 8, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 9, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 10, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 11, 1500000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (1, 12, 1500000);

-- Insertar datos en gastos_ultimos_12_meses para comprobante_ingresos_id = 2
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 1, 400000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 2, 420000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 3, 380000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 4, 410000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 5, 430000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 6, 390000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 7, 400000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 8, 420000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 9, 380000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 10, 410000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 11, 430000);
INSERT INTO gastos_ultimos_12_meses (comprobante_ingresos_id, mes, gasto) VALUES (2, 12, 500000);

-- Insertar datos en ingresos_ultimos_12_meses para comprobante_ingresos_id = 2
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 1, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 2, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 3, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 4, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 5, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 6, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 7, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 8, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 9, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 10, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 11, 1200000);
INSERT INTO ingresos_ultimos_12_meses (comprobante_ingresos_id, mes, ingreso) VALUES (2, 12, 1200000);

-- Insertar datos en la tabla prestamo
INSERT INTO prestamo (tipo, plazo, tasa_interes, monto, estado) VALUES ('Primera vivienda', 20, 4.5, 100000000, 'En proceso');
INSERT INTO prestamo (tipo, plazo, tasa_interes, monto, estado) VALUES ('Remodelacion', 10, 5.0, 50000000, 'En proceso');

-- Asociar usuarios con préstamos
INSERT INTO usuario_prestamo (id_usuario, id_prestamo) VALUES (1, 1);
INSERT INTO usuario_prestamo (id_usuario, id_prestamo) VALUES (2, 2);
