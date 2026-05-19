
-- =============================================================================
-- 1. CREACIÓN DE TABLAS ESTRUCTURALES
-- =============================================================================

CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

CREATE TABLE veterinario (
    id_veterinario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    correo VARCHAR(100) NOT NULL UNIQUE,
    especialidad VARCHAR(100),
    num_licencia VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

CREATE TABLE recepcionista (
    id_recepcionista SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

CREATE TABLE administrador (
    id_administrador SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

CREATE TABLE mascota (
    id_mascota SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50),
    raza VARCHAR(50),
    edad INT,
    peso FLOAT,
    sexo VARCHAR(10)
);

CREATE TABLE mascota_adopcion (
    id_mascota_adopcion SERIAL PRIMARY KEY,
    id_recepcionista INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50),
    raza VARCHAR(50),
    edad INT,
    estado_salud VARCHAR(100),
    estado_adopcion VARCHAR(50),
    fecha_ingreso DATE
);

CREATE TABLE cita (
    id_cita SERIAL PRIMARY KEY,
    id_mascota INT NOT NULL,
    id_veterinario INT NOT NULL,
    id_recepcionista INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    motivo VARCHAR(50),
    estado VARCHAR(50)
);

CREATE TABLE servicio (
    id_servicio SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion TEXT,
    precio FLOAT NOT NULL
);

CREATE TABLE cita_servicio (
    id_cita INT NOT NULL,
    id_servicio INT NOT NULL,
    PRIMARY KEY (id_cita, id_servicio)
);

CREATE TABLE medicamento (
    id_medicamento SERIAL PRIMARY KEY,
    id_administrador INT NOT NULL,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    precio FLOAT NOT NULL,
    stock INT NOT NULL,
    fecha_vencimiento DATE
);

CREATE TABLE historial_medico (
    id_historial SERIAL PRIMARY KEY,
    id_mascota INT NOT NULL,
    id_veterinario INT NOT NULL,
    fecha DATE NOT NULL,
    diagnostico TEXT,
    observaciones TEXT
);

CREATE TABLE tratamiento (
    id_tratamiento SERIAL PRIMARY KEY,
    id_historial INT NOT NULL,
    id_medicamento INT NOT NULL,
    descripcion TEXT,
    dosis VARCHAR(100),
    fecha_inicio DATE,
    fecha_fin DATE
);

CREATE TABLE producto (
    id_producto SERIAL PRIMARY KEY,
    id_administrador INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio FLOAT NOT NULL,
    stock INT NOT NULL,
    fecha_vencimiento DATE
);

CREATE TABLE adoptante (
    id_adoptante SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    correo VARCHAR(100)
);

CREATE TABLE adopcion (
    id_adoptante INT NOT NULL,
    id_mascota_adopcion INT NOT NULL,
    fecha_adopcion DATE NOT NULL,
    PRIMARY KEY (id_adoptante, id_mascota_adopcion)
);

-- =============================================================================
-- 2. RESTRICCIONES
-- =============================================================================

ALTER TABLE cliente
    ADD CONSTRAINT chk_cliente_documento CHECK (LENGTH(TRIM(documento)) >= 6),
    ADD CONSTRAINT chk_cliente_correo CHECK (correo ~ '^[^@\s]+@[^@\s]+\.[^@\s]+$'),
    ADD CONSTRAINT chk_cliente_nombre CHECK (LENGTH(TRIM(nombre)) >= 2),
    ADD CONSTRAINT chk_cliente_contrasena CHECK (LENGTH(contrasena) >= 8);

ALTER TABLE veterinario
    ADD CONSTRAINT chk_vet_documento CHECK (LENGTH(TRIM(documento)) >= 6),
    ADD CONSTRAINT chk_vet_correo CHECK (correo ~ '^[^@\s]+@[^@\s]+\.[^@\s]+$'),
    ADD CONSTRAINT chk_vet_nombre CHECK (LENGTH(TRIM(nombre)) >= 2),
    ADD CONSTRAINT chk_vet_especialidad CHECK (especialidad IN (
        'Medicina Interna', 'Cirugía', 'Dermatología', 'Odontología',
        'Traumatología', 'Oncología', 'Cardiología', 'Oftalmología', 'General'
    )),
    ADD CONSTRAINT chk_vet_contrasena CHECK (LENGTH(contrasena) >= 8);

ALTER TABLE recepcionista
    ADD CONSTRAINT chk_rec_documento CHECK (LENGTH(TRIM(documento)) >= 6),
    ADD CONSTRAINT chk_rec_correo CHECK (correo ~ '^[^@\s]+@[^@\s]+\.[^@\s]+$'),
    ADD CONSTRAINT chk_rec_nombre CHECK (LENGTH(TRIM(nombre)) >= 2),
    ADD CONSTRAINT chk_rec_contrasena CHECK (LENGTH(contrasena) >= 8);

ALTER TABLE administrador
    ADD CONSTRAINT chk_adm_documento CHECK (LENGTH(TRIM(documento)) >= 6),
    ADD CONSTRAINT chk_adm_correo CHECK (correo ~ '^[^@\s]+@[^@\s]+\.[^@\s]+$'),
    ADD CONSTRAINT chk_adm_nombre CHECK (LENGTH(TRIM(nombre)) >= 2),
    ADD CONSTRAINT chk_adm_contrasena CHECK (LENGTH(contrasena) >= 8);

ALTER TABLE mascota
    ADD CONSTRAINT fk_mascota_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT chk_mascota_especie CHECK (especie IN ('Perro', 'Gato', 'Ave', 'Reptil', 'Roedor', 'Otro')),
    ADD CONSTRAINT chk_mascota_sexo CHECK (sexo IN ('Macho', 'Hembra', 'Desconocido')),
    ADD CONSTRAINT chk_mascota_edad CHECK (edad >= 0 AND edad <= 50),
    ADD CONSTRAINT chk_mascota_peso CHECK (peso > 0 AND peso <= 300),
    ADD CONSTRAINT chk_mascota_nombre CHECK (LENGTH(TRIM(nombre)) >= 1);

ALTER TABLE mascota_adopcion
    ADD CONSTRAINT fk_mascota_adopcion_recepcionista FOREIGN KEY (id_recepcionista) REFERENCES recepcionista(id_recepcionista) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT chk_ma_especie CHECK (especie IN ('Perro', 'Gato', 'Ave', 'Reptil', 'Roedor', 'Otro')),
    ADD CONSTRAINT chk_ma_estado_salud CHECK (estado_salud IN ('Saludable', 'En tratamiento', 'Recuperación', 'Crónico', 'Crítico', 'Fallecido')),
    ADD CONSTRAINT chk_ma_estado_adopcion CHECK (estado_adopcion IN ('Disponible', 'En proceso', 'Adoptado', 'No disponible')),
    ADD CONSTRAINT chk_ma_edad CHECK (edad >= 0 AND edad <= 50),
    ADD CONSTRAINT chk_ma_fecha_ingreso CHECK (fecha_ingreso <= CURRENT_DATE);

ALTER TABLE cita
    ADD CONSTRAINT fk_cita_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT fk_cita_veterinario FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT fk_cita_recepcionista FOREIGN KEY (id_recepcionista) REFERENCES recepcionista(id_recepcionista) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT chk_cita_estado CHECK (estado IN ('Pendiente', 'Completada', 'Cancelada', 'No asistió')),
    ADD CONSTRAINT chk_cita_fecha CHECK (fecha >= '2000-01-01'),
    ADD CONSTRAINT chk_cita_hora CHECK (hora >= '07:00' AND hora <= '20:00'),
    ADD CONSTRAINT chk_cita_motivo CHECK (LENGTH(TRIM(motivo)) >= 3);

ALTER TABLE servicio
    ADD CONSTRAINT chk_servicio_precio CHECK (precio > 0),
    ADD CONSTRAINT chk_servicio_nombre CHECK (LENGTH(TRIM(nombre)) >= 3);

ALTER TABLE cita_servicio
    ADD CONSTRAINT fk_cs_cita FOREIGN KEY (id_cita) REFERENCES cita(id_cita) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT fk_cs_servicio FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio) ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE medicamento
    ADD CONSTRAINT fk_medicamento_admin FOREIGN KEY (id_administrador) REFERENCES administrador(id_administrador) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT chk_med_precio CHECK (precio > 0),
    ADD CONSTRAINT chk_med_stock CHECK (stock >= 0),
    ADD CONSTRAINT chk_med_fecha_vencimiento CHECK (fecha_vencimiento > '2000-01-01'),
    ADD CONSTRAINT chk_med_nombre CHECK (LENGTH(TRIM(nombre)) >= 3);

ALTER TABLE historial_medico
    ADD CONSTRAINT fk_historial_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT fk_historial_veterinario FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT chk_hist_fecha CHECK (fecha >= '2000-01-01' AND fecha <= CURRENT_DATE),
    ADD CONSTRAINT chk_hist_diagnostico CHECK (LENGTH(TRIM(diagnostico)) >= 5);

ALTER TABLE tratamiento
    ADD CONSTRAINT fk_tratamiento_historial FOREIGN KEY (id_historial) REFERENCES historial_medico(id_historial) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT fk_tratamiento_medicamento FOREIGN KEY (id_medicamento) REFERENCES medicamento(id_medicamento) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT chk_trat_fechas CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio),
    ADD CONSTRAINT chk_trat_fecha_inicio CHECK (fecha_inicio >= '2000-01-01'),
    ADD CONSTRAINT chk_trat_dosis CHECK (LENGTH(TRIM(dosis)) >= 2);

ALTER TABLE producto
    ADD CONSTRAINT fk_producto_admin FOREIGN KEY (id_administrador) REFERENCES administrador(id_administrador) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT chk_prod_precio CHECK (precio > 0),
    ADD CONSTRAINT chk_prod_stock CHECK (stock >= 0),
    ADD CONSTRAINT chk_prod_nombre CHECK (LENGTH(TRIM(nombre)) >= 3),
    ADD CONSTRAINT chk_prod_fecha_vencimiento CHECK (fecha_vencimiento IS NULL OR fecha_vencimiento > '2000-01-01');

ALTER TABLE adoptante
    ADD CONSTRAINT chk_adoptante_documento CHECK (LENGTH(TRIM(documento)) >= 6),
    ADD CONSTRAINT chk_adoptante_correo CHECK (correo IS NULL OR correo ~ '^[^@\s]+@[^@\s]+\.[^@\s]+$'),
    ADD CONSTRAINT chk_adoptante_nombre CHECK (LENGTH(TRIM(nombre)) >= 2);

ALTER TABLE adopcion
    ADD CONSTRAINT fk_adopcion_adoptante FOREIGN KEY (id_adoptante) REFERENCES adoptante(id_adoptante) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT fk_adopcion_mascota FOREIGN KEY (id_mascota_adopcion) REFERENCES mascota_adopcion(id_mascota_adopcion) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT chk_adopcion_fecha CHECK (fecha_adopcion >= '2000-01-01' AND fecha_adopcion <= CURRENT_DATE);

-- =============================================================================
-- 3. DATOS DE PRUEBA
-- =============================================================================

INSERT INTO administrador (nombre, documento, telefono, correo, contrasena) VALUES
('Carlos Admin', '1234567', '555-0101', 'admin@veterinaria.com', 'admin12345');

INSERT INTO recepcionista (nombre, documento, telefono, correo, contrasena) VALUES
('Ana Recepcion', '2345678', '555-0202', 'ana.recep@veterinaria.com', 'recep12345'),
('Luis Front', '3456789', '555-0203', 'luis.front@veterinaria.com', 'front12345');

INSERT INTO veterinario (nombre, documento, telefono, correo, especialidad, num_licencia, contrasena) VALUES
('Dr. Roberto Perez', '4567890', '555-0301', 'roberto.vet@vet.com', 'General', 'VET-001', 'vet12345'),
('Dra. Maria Lopez', '5678901', '555-0302', 'maria.vet@vet.com', 'Cirugía', 'VET-002', 'vet12345');

INSERT INTO cliente (nombre, documento, telefono, direccion, correo, contrasena) VALUES
('Juan Minero', '1111111', '555-1010', 'Calle Falsa 123', 'juan.m@gmail.com', 'juan12345'),
('Laura Galvis', '2222222', '555-2020', 'Av. Siempre Viva 742', 'laura.g@gmail.com', 'laura12345');

INSERT INTO mascota (id_cliente, nombre, especie, raza, edad, peso, sexo) VALUES
(1, 'Firulais', 'Perro', 'Labrador', 3, 25.5, 'Macho'),
(1, 'Michi', 'Gato', 'Siamés', 2, 4.2, 'Hembra'),
(2, 'Poli', 'Ave', 'Loro', 10, 0.5, 'Macho');

INSERT INTO mascota_adopcion (id_recepcionista, nombre, especie, raza, edad, estado_salud, estado_adopcion, fecha_ingreso) VALUES
(1, 'Rex', 'Perro', 'Criollo', 1, 'Saludable', 'Disponible', '2024-01-15'),
(2, 'Luna', 'Gato', 'Persa', 4, 'Recuperación', 'En proceso', '2024-02-01');

INSERT INTO servicio (nombre, descripcion, precio) VALUES
('Consulta General', 'Revisión de rutina', 30.0),
('Vacunación', 'Aplicación de vacunas anuales', 20.0),
('Cirugía Menor', 'Procedimiento ambulatorio', 150.0);

INSERT INTO producto (id_administrador, nombre, descripcion, precio, stock, fecha_vencimiento) VALUES
(1, 'Concentrado Pro-Plan', 'Bulto de 10kg', 60.0, 50, '2025-12-31'),
(1, 'Shampoo Antipulgas', 'Frasco 250ml', 15.0, 20, '2026-06-30');

INSERT INTO medicamento (id_administrador, nombre, descripcion, precio, stock, fecha_vencimiento) VALUES
(1, 'Amoxicilina', 'Antibiótico', 12.0, 100, '2025-05-20'),
(1, 'Meloxicam', 'Antiinflamatorio', 8.5, 80, '2025-08-15');

INSERT INTO cita (id_mascota, id_veterinario, id_recepcionista, fecha, hora, motivo, estado) VALUES
(1, 1, 1, '2024-03-20', '09:00:00', 'Chequeo anual', 'Completada'),
(2, 2, 2, '2024-03-21', '15:30:00', 'Dolor abdominal', 'Pendiente');

INSERT INTO cita_servicio (id_cita, id_servicio) VALUES
(1, 1),
(1, 2),
(2, 1);

INSERT INTO historial_medico (id_mascota, id_veterinario, fecha, diagnostico, observaciones) VALUES
(1, 1, '2024-03-20', 'Paciente sano', 'Se recomienda dieta baja en grasas');

INSERT INTO tratamiento (id_historial, id_medicamento, descripcion, dosis, fecha_inicio, fecha_fin) VALUES
(1, 1, 'Refuerzo inmunológico', '1 pastilla diaria', '2024-03-20', '2024-03-27');

INSERT INTO adoptante (nombre, documento, telefono, direccion, correo) VALUES
('Pedro Picapiedra', '9999999', '555-9090', 'Rocadura 1', 'pedro@piedra.com');

INSERT INTO adopcion (id_adoptante, id_mascota_adopcion, fecha_adopcion) VALUES
(1, 1, '2024-02-10');

-- =============================================================================
-- 4. VISTAS
-- =============================================================================

CREATE OR REPLACE VIEW vista_agenda_diaria AS
SELECT
    c.id_cita,
    c.fecha,
    c.hora,
    v.nombre AS veterinario,
    m.nombre AS mascota,
    cl.nombre AS cliente,
    r.nombre AS recepcionista,
    c.motivo,
    c.estado
FROM cita c
JOIN veterinario v ON c.id_veterinario = v.id_veterinario
JOIN mascota m ON c.id_mascota = m.id_mascota
JOIN cliente cl ON m.id_cliente = cl.id_cliente
JOIN recepcionista r ON c.id_recepcionista = r.id_recepcionista;

CREATE OR REPLACE VIEW vista_reporte_ocupacion AS
SELECT
    v.id_veterinario,
    v.nombre AS nombre_veterinario,
    v.especialidad,
    COUNT(c.id_cita) AS total_citas_asignadas,
    SUM(CASE WHEN c.estado = 'Pendiente' THEN 1 ELSE 0 END) AS citas_pendientes,
    SUM(CASE WHEN c.estado = 'Completada' THEN 1 ELSE 0 END) AS citas_completadas,
    SUM(CASE WHEN c.estado = 'Cancelada' THEN 1 ELSE 0 END) AS citas_canceladas
FROM veterinario v
LEFT JOIN cita c ON v.id_veterinario = c.id_veterinario
GROUP BY v.id_veterinario, v.nombre, v.especialidad
ORDER BY total_citas_asignadas DESC;

CREATE OR REPLACE VIEW vista_clientes_contacto AS
SELECT nombre, telefono, correo
FROM cliente;

CREATE OR REPLACE VIEW vista_todas_mascotas AS
SELECT
    m.id_mascota AS id,
    m.nombre,
    m.especie,
    m.raza,
    m.sexo,
    c.nombre AS responsable,
    'Mascota de Cliente' AS tipo_registro,
    m.edad
FROM mascota m
JOIN cliente c ON m.id_cliente = c.id_cliente
UNION ALL
SELECT
    ma.id_mascota_adopcion AS id,
    ma.nombre,
    ma.especie,
    ma.raza,
    'Desconocido' AS sexo,
    'Recepcionista: ' || r.nombre AS responsable,
    'En Adopción' AS tipo_registro,
    ma.edad
FROM mascota_adopcion ma
JOIN recepcionista r ON ma.id_recepcionista = r.id_recepcionista;

-- =============================================================================
-- 5. ROLES, USUARIOS Y PERMISOS
-- =============================================================================

DROP ROLE IF EXISTS rol_recepcionista;
DROP ROLE IF EXISTS rol_veterinario;
DROP ROLE IF EXISTS rol_admin_general;

DROP USER IF EXISTS usuario_recepcion;
DROP USER IF EXISTS usuario_medico;
DROP USER IF EXISTS usuario_root_admin;

CREATE ROLE rol_recepcionista NOLOGIN;
CREATE ROLE rol_veterinario NOLOGIN;
CREATE ROLE rol_admin_general NOLOGIN;

CREATE USER usuario_recepcion WITH LOGIN PASSWORD 'Recep2024*';
CREATE USER usuario_medico WITH LOGIN PASSWORD 'Medica2024*';
CREATE USER usuario_root_admin WITH LOGIN PASSWORD 'Admin2024*';

GRANT rol_recepcionista TO CURRENT_USER;
GRANT rol_veterinario TO CURRENT_USER;
GRANT rol_admin_general TO CURRENT_USER;

GRANT rol_recepcionista TO usuario_recepcion;
GRANT rol_veterinario TO usuario_medico;
GRANT rol_admin_general TO usuario_root_admin;

GRANT USAGE ON SCHEMA public TO rol_recepcionista, rol_veterinario, rol_admin_general;

GRANT SELECT, INSERT, UPDATE ON cliente, mascota, cita TO rol_recepcionista;
GRANT SELECT ON vista_agenda_diaria TO rol_recepcionista;

GRANT SELECT ON vista_agenda_diaria TO rol_veterinario;
GRANT SELECT ON vista_reporte_ocupacion TO rol_veterinario;
GRANT SELECT, INSERT ON historial_medico TO rol_veterinario;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO rol_admin_general;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO rol_admin_general;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO rol_recepcionista;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO rol_veterinario;

-- =============================================================================
-- 6. VERIFICACIÓN DE PERMISOS
-- =============================================================================

-- Las siguientes consultas son solo para verificar los permisos si deseas ejecutarlas manualmente.
-- SELECT grantee, table_name, privilege_type
-- FROM information_schema.role_table_grants
-- WHERE grantee IN ('rol_recepcionista', 'rol_veterinario', 'rol_admin_general')
--   AND table_schema = 'public'
-- ORDER BY grantee, table_name;

-- SELECT u.rolname AS usuario_fisico, r.rolname AS rol_asignado
-- FROM pg_auth_members m
-- JOIN pg_roles r ON m.roleid = r.oid
-- JOIN pg_roles u ON m.member = u.oid
-- WHERE u.rolname IN ('usuario_recepcion', 'usuario_medico', 'usuario_root_admin');
