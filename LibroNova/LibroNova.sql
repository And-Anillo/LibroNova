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