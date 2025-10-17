package com.libronova.dao.impl;

import com.libronova.dao.PrestamoDAO;
import com.libronova.Model.Prestamo;
import com.libronova.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCPrestamoDAO implements PrestamoDAO {

    @Override
    public void registrarPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos(isbn, socio_id, fecha_prestamo, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prestamo.getIsbn());
            stmt.setLong(2, prestamo.getSocioId());
            stmt.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            stmt.setString(4, prestamo.getEstado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar préstamo", e);
        }
    }

    @Override
    public void registrarDevolucion(Prestamo prestamo) {
        String sql = "UPDATE prestamos SET fecha_devolucion = ?, multa = ?, estado = 'DEVUELTO' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(prestamo.getFechaDevolucion()));
            stmt.setDouble(2, prestamo.getMulta());
            stmt.setLong(3, prestamo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar devolución", e);
        }
    }

    @Override
    public List<Prestamo> listarPrestamosVencidos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = """
            SELECT p.id, p.isbn, l.titulo, s.nombre_completo, p.fecha_prestamo, p.fecha_devolucion, p.multa
            FROM prestamos p
            JOIN libros l ON p.isbn = l.isbn
            JOIN socios s ON p.socio_id = s.id
            WHERE p.estado = 'PENDIENTE'
              AND p.fecha_prestamo < CURDATE() - INTERVAL ? DAY
            """;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, com.libronova.util.DBConnection.getDiasPrestamo());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setId(rs.getLong("id"));
                p.setIsbn(rs.getString("isbn"));
                p.setTituloLibro(rs.getString("titulo"));
                p.setNombreSocio(rs.getString("nombre_completo"));
                p.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                p.setFechaDevolucion(rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null);
                p.setMulta(rs.getDouble("multa"));
                p.setEstado("PENDIENTE");
                prestamos.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar préstamos vencidos", e);
        }
        return prestamos;
    }

    @Override
    public List<Prestamo> listarTodosLosPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = """
        SELECT p.id, p.isbn, l.titulo, s.nombre_completo, p.fecha_prestamo, p.fecha_devolucion, p.multa, p.estado
        FROM prestamos p
        JOIN libros l ON p.isbn = l.isbn
        JOIN socios s ON p.socio_id = s.id
        ORDER BY p.created_at DESC
        """;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setId(rs.getLong("id"));
                p.setIsbn(rs.getString("isbn"));
                p.setTituloLibro(rs.getString("titulo"));
                p.setNombreSocio(rs.getString("nombre_completo"));
                p.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                p.setFechaDevolucion(rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null);
                p.setMulta(rs.getDouble("multa"));
                p.setEstado(rs.getString("estado"));
                prestamos.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar préstamos", e);
        }
        return prestamos;
    }
}
