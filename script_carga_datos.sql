-- Insertar datos en la tabla Tecnico
INSERT INTO tecnico (dni, nombre, estado) VALUES 
(12345678, 'Carlos Bianchi', true),
(5432150, 'Miguel Angel Russo', true),
(30789090, 'Fernando Gago', true);

-- Insertar datos en la tabla Cliente
INSERT INTO cliente (dni, nombre, direccion, telefono, email, estado, fecha_ultima_actualizacion, observaciones) VALUES 
(43606060, 'Juan Perez', 'Calle 123, Ciudad', '1234567890', 'juan@example.com', true, '2023-01-01 10:00:00', 'Sin observaciones'),
(43606070, 'Maria Rodriguez', 'Av. Principal, etc.', '9876543210', 'maria@example.com', true, '2023-01-02 15:30:00', 'Cliente preferencial'),
(43606080, 'Carlos Gutierrez', 'Otra direccion', '5555555555', 'carlos@example.com', true, '2023-01-03 10:00:00', 'Sin observaciones');

-- Insertar datos en la tabla Marca
INSERT INTO marca (nombre, estado) VALUES 
('Toyota', true),
('Ford', true),
('Chevrolet', true);

-- Insertar datos en la tabla Modelo
INSERT INTO modelo (nombre, marca_id, estado) VALUES 
('Corolla', 1, true),
('Fiesta', 2, true),
('Cruze', 3, true);

-- Insertar datos en la tabla Auto
INSERT INTO auto (modelo_id, cliente_id, patente, anio, estado) VALUES 
(1, 1, 'AB123CC', '2020', true),
(2, 2, 'XZ987CC', '2018', true),
(3, 3, 'DE456CC', '2022', true);

-- Insertar datos en la tabla Servicio
INSERT INTO servicio (nombre, precio, estado, minutosestimados) VALUES 
('Limpieza', 50.0, true, 50),
('Reparacion', 100.0, true, 120),
('Instalacion', 80.0, true, 60);

-- Insertar datos en la tabla Estado
INSERT INTO estado (descripcion, nombre) VALUES 
('Creacion de orden de trabajo', 'iniciado'),
('Se termina el trabajo de la orden', 'terminado'),
('El cliente cancela la orden', 'cancelado');


-- Insertar datos en la tabla OrdenTrabajo
INSERT INTO orden_trabajo (descripcion, fecha_inicio, fecha_fin, total, tecnico_id, vehiculo_id, estado_id, habilitado) VALUES 
('Reparacion de motor', '2023-12-01', '2023-12-05', 500.0, 1, 1, 1, true),
('Instalacion de sistema de sonido', '2023-12-10', '2023-12-12', 300.0, 2, 2, 2, true);

-- Insertar datos en la tabla DetalleOrdenTrabajo
INSERT INTO detalle_orden_trabajo (descripcion, cantidad, servicio_id, subtotal, estado, minutos_realizados, orden_id) VALUES 
('Cambio de aceite', 1, 1, 100.0, true, 45, 1),
('Instalacion de parlantes', 2, 3, 150.0, true, 60, 2);
