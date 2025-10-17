package com.libronova.dao.impl;

import com.libronova.dao.LibroDAO;
import com.libronova.Model.Libro;
import com.libronova.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of the LibroDAO interface.
 * Handles all database operations related to the 'libros' table using SQL queries.
 */
public class JDBCLibroDAO implements LibroDAO {

    /**
     * Inserts a new book into the database.
     * Uses a prepared statement to safely set all book fields.
     */
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

    /**
     * Updates an existing book in the database using its ISBN as the unique identifier.
     */
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

    /**
     * Retrieves a single book from the database by its ISBN.
     * Returns null if no book is found.
     */
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

    /**
     * Returns a list of all books, ordered by creation date (newest first).
     */
    @Override
    public List<Libro> listarTodos() {
        return ejecutarConsulta("SELECT * FROM libros ORDER BY created_at DESC");
    }

    /**
     * Filters books by author name using a case-insensitive partial match (LIKE).
     */
    @Override
    public List<Libro> filtrarPorAutor(String autor) {
        return ejecutarConsultaConParametro("SELECT * FROM libros WHERE autor LIKE ?", "%" + autor + "%");
    }

    /**
     * Filters books by exact category match.
     */
    @Override
    public List<Libro> filtrarPorCategoria(String categoria) {
        return ejecutarConsultaConParametro("SELECT * FROM libros WHERE categoria = ?", categoria);
    }

    /**
     * Checks if a book with the given ISBN already exists in the database.
     * Used to enforce ISBN uniqueness.
     */
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

    /**
     * Executes a parameter-less SQL query and maps the result set to a list of Libro objects.
     */
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

    /**
     * Executes a SQL query with a single string parameter and maps the result set to a list of Libro objects.
     */
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

    /**
     * Maps a single row from a ResultSet to a Libro domain object.
     * Handles type conversion from SQL types to Java types.
     */
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