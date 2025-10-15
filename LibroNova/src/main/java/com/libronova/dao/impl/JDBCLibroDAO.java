package com.libronova.dao.impl;

import com.libronova.dao.LibroDAO;
import com.libronova.Model.Libro;
import com.libronova.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCLibroDAO implements LibroDAO {

    @Override
    public void crear(Libro libro) {
        String sql = "INSERT INTO libros(isbn, titulo, autor, categoria, ejemplares_totales, ejemplares_disponibles, precio_referencia, is_activo, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, libro.getIsbn());
            stmt.setString(2, libro.getTitulo());
            stmt.setString(3, libro.getAutor());
            stmt.setString(4, libro.getCategoria());
            stmt.setInt(5, libro.getEjemplaresTotales());
            stmt.setInt(6, libro.getEjemplaresDisponibles());
            stmt.setDouble(7, libro.getPrecioReferencia());
            stmt.setBoolean(8, libro.isActivo());
            stmt.setTimestamp(9, Timestamp.valueOf(libro.getCreatedAt()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear libro", e);
        }
    }

    @Override
    public void actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo=?, autor=?, categoria=?, ejemplares_totales=?, ejemplares_disponibles=?, precio_referencia=?, is_activo=? WHERE isbn=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getCategoria());
            stmt.setInt(4, libro.getEjemplaresTotales());
            stmt.setInt(5, libro.getEjemplaresDisponibles());
            stmt.setDouble(6, libro.getPrecioReferencia());
            stmt.setBoolean(7, libro.isActivo());
            stmt.setString(8, libro.getIsbn());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar libro", e);
        }
    }

    @Override
    public Libro buscarPorISBN(String isbn) {
        String sql = "SELECT * FROM libros WHERE isbn = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLibro(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libro", e);
        }
        return null;
    }

    @Override
    public List<Libro> listarTodos() {
        return ejecutarConsulta("SELECT * FROM libros ORDER BY created_at DESC");
    }

    @Override
    public List<Libro> filtrarPorAutor(String autor) {
        return ejecutarConsultaConParametro("SELECT * FROM libros WHERE autor LIKE ?", "%" + autor + "%");
    }

    @Override
    public List<Libro> filtrarPorCategoria(String categoria) {
        return ejecutarConsultaConParametro("SELECT * FROM libros WHERE categoria = ?", categoria);
    }

    @Override
    public boolean existeISBN(String isbn) {
        String sql = "SELECT 1 FROM libros WHERE isbn = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar ISBN", e);
        }
    }

    private List<Libro> ejecutarConsulta(String sql) {
        List<Libro> libros = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar libros", e);
        }
        return libros;
    }

    private List<Libro> ejecutarConsultaConParametro(String sql, String param) {
        List<Libro> libros = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, param);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al filtrar libros", e);
        }
        return libros;
    }

    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setIsbn(rs.getString("isbn"));
        l.setTitulo(rs.getString("titulo"));
        l.setAutor(rs.getString("autor"));
        l.setCategoria(rs.getString("categoria"));
        l.setEjemplaresTotales(rs.getInt("ejemplares_totales"));
        l.setEjemplaresDisponibles(rs.getInt("ejemplares_disponibles"));
        l.setPrecioReferencia(rs.getDouble("precio_referencia"));
        l.setActivo(rs.getBoolean("is_activo"));
        l.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return l;
    }
}