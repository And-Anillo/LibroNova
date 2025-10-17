/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Coder
 * Created: 15 oct 2025
 */

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS libronova 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE libronova;

-- Tabla: usuarios (gestiona ADMIN y ASISTENTE)
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- en producción usar hash
    rol ENUM('ADMIN', 'ASISTENTE') NOT NULL DEFAULT 'ASISTENTE',
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: socios (usuarios que piden préstamos)
CREATE TABLE IF NOT EXISTS socios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    direccion TEXT,
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: libros
CREATE TABLE IF NOT EXISTS libros (
    isbn VARCHAR(20) PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    ejemplares_totales INT NOT NULL CHECK (ejemplares_totales >= 0),
    ejemplares_disponibles INT NOT NULL CHECK (ejemplares_disponibles >= 0),
    precio_referencia DECIMAL(10,2) NOT NULL,
    is_activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: préstamos
CREATE TABLE IF NOT EXISTS prestamos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL,
    socio_id BIGINT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE NULL, -- NULL = aún no devuelto
    multa DECIMAL(10,2) DEFAULT 0.00,
    estado ENUM('PENDIENTE', 'DEVUELTO') NOT NULL DEFAULT 'PENDIENTE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (isbn) REFERENCES libros(isbn) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (socio_id) REFERENCES socios(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Índices para rendimiento
CREATE INDEX idx_libros_autor ON libros(autor);
CREATE INDEX idx_libros_categoria ON libros(categoria);
CREATE INDEX idx_prestamos_socio ON prestamos(socio_id);
CREATE INDEX idx_prestamos_fecha_dev ON prestamos(fecha_devolucion);
CREATE INDEX idx_prestamos_estado ON prestamos(estado);

-- Usuario de ejemplo (ADMIN)
INSERT IGNORE INTO usuarios (nombre, email, password, rol, estado)
VALUES ('Admin Principal', 'admin@libronova.com', 'admin123', 'ADMIN', 'ACTIVO');


USE libronova;

-- ========================
-- USUARIOS (1 ADMIN + 2 ASISTENTES)
-- ========================
INSERT IGNORE INTO usuarios (nombre, email, password, rol, estado)
VALUES 
('Admin Principal', 'admin@libronova.com', 'admin123', 'ADMIN', 'ACTIVO'),
('Ana López', 'ana.asistente@libronova.com', 'asistente123', 'ASISTENTE', 'ACTIVO'),
('Carlos Mendoza', 'carlos.asistente@libronova.com', 'asistente456', 'ASISTENTE', 'ACTIVO');

-- ========================
-- SOCIOS (5 registros)
-- ========================
INSERT INTO socios (nombre_completo, email, telefono, direccion, estado)
VALUES 
('María Fernández', 'maria.f@email.com', '3001234567', 'Calle 10 #20-30, Bogotá', 'ACTIVO'),
('Luis Ramírez', 'luis.r@email.com', '3109876543', 'Av. Siempre Viva 742, Medellín', 'ACTIVO'),
('Sofía Castro', 'sofia.c@email.com', '3201122334', 'Carrera 5 #15-80, Cali', 'ACTIVO'),
('Javier Ortiz', 'javier.o@email.com', '3156677889', 'Diag 45 #67-89, Barranquilla', 'INACTIVO'),
('Valentina Gómez', 'vale.g@email.com', '3054455667', 'Transv 12 #34-56, Cartagena', 'INACTIVO');

-- ========================
-- LIBROS (10 registros)
-- ========================
INSERT INTO libros (isbn, titulo, autor, categoria, ejemplares_totales, ejemplares_disponibles, precio_referencia, is_activo)
VALUES 
('978-3-16-148410-0', 'El Principito', 'Antoine de Saint-Exupéry', 'Ficción', 5, 3, 25000.00, TRUE),
('978-0-06-112008-4', 'Cien Años de Soledad', 'Gabriel García Márquez', 'Ficción', 8, 0, 45000.00, TRUE),
('978-0-452-28423-4', '1984', 'George Orwell', 'Ficción', 6, 2, 30000.00, TRUE),
('978-0-7432-7356-5', 'El Código Da Vinci', 'Dan Brown', 'Misterio', 4, 4, 35000.00, TRUE),
('978-0-307-27767-1', 'Sapiens', 'Yuval Noah Harari', 'No Ficción', 7, 5, 50000.00, TRUE),
('978-0-14-312774-1', 'Steve Jobs', 'Walter Isaacson', 'Biografía', 3, 1, 40000.00, TRUE),
('978-0-525-47881-2', 'Educación Emocional', 'Daniel Goleman', 'Psicología', 5, 5, 32000.00, TRUE),
('978-0-385-33378-8', 'El Alquimista', 'Paulo Coelho', 'Ficción', 10, 7, 28000.00, TRUE),
('978-0-06-231500-7', 'Dune', 'Frank Herbert', 'Ciencia Ficción', 4, 2, 42000.00, TRUE),
('978-0-307-58836-4', 'La Teoría del Todo', 'Stephen Hawking', 'Ciencia', 2, 0, 38000.00, TRUE);

-- ========================
-- PRÉSTAMOS (3 registros de ejemplo)
-- ========================
-- Préstamo 1: Libro disponible → se reduce stock
INSERT INTO prestamos (isbn, socio_id, fecha_prestamo, estado)
VALUES ('978-3-16-148410-0', 1, CURDATE() - INTERVAL 2 DAY, 'PENDIENTE');

-- Préstamo 2: Libro ya sin stock (Cien Años) → para prueba de validación
INSERT INTO prestamos (isbn, socio_id, fecha_prestamo, fecha_devolucion, multa, estado)
VALUES ('978-0-06-112008-4', 2, CURDATE() - INTERVAL 10 DAY, CURDATE() - INTERVAL 1 DAY, 10500.00, 'DEVUELTO');

-- Préstamo 3: Vencido (prestado hace 10 días, límite = 7 días)
INSERT INTO prestamos (isbn, socio_id, fecha_prestamo, estado)
VALUES ('978-0-452-28423-4', 3, CURDATE() - INTERVAL 10 DAY, 'PENDIENTE');