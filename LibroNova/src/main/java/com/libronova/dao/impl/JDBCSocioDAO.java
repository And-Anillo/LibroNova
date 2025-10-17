package com.libronova.dao.impl;

import com.libronova.dao.SocioDAO;
import com.libronova.Model.Socio;
import com.libronova.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of the SocioDAO interface.
 * Manages all database operations for library members (socios), including creation,
 * retrieval, listing, and updates.
 */
public class JDBCSocioDAO implements SocioDAO {

    /**
     * Inserts a new member (socio) into the 'socios' table.
     * Automatically sets the creation timestamp to the current time.
     * Retrieves and assigns the auto-generated database ID to the socio object.
     */
    @Override
    public void crear(Socio socio) {
        String sql = "INSERT INTO socios(nombre_completo, email, telefono, direccion, estado, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, socio.getNombreCompleto());
            stmt.setString(2, socio.getEmail());
            stmt.setString(3, socio.getTelefono());
            stmt.setString(4, socio.getDireccion());
            stmt.setString(5, socio.getEstado());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();

            // Retrieve the auto-generated ID and set it on the socio object
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    socio.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear socio", e);
        }
    }

    /**
     * Retrieves a single member by their unique ID.
     * Returns null if no member is found with the given ID.
     */
    @Override
    public Socio buscarPorId(Long id) {
        String sql = "SELECT * FROM socios WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToSocio(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar socio", e);
        }
        return null;
    }

    /**
     * Returns a list of all members, ordered by creation date (newest first).
     */
    @Override
    public List<Socio> listarTodos() {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM socios ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                socios.add(mapResultSetToSocio(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar socios", e);
        }
        return socios;
    }

    /**
     * Updates an existing member's information in the database using their ID.
     * All fields except the ID are updated (including status: ACTIVO/INACTIVO).
     */
    @Override
    public void actualizar(Socio socio) {
        String sql = "UPDATE socios SET nombre_completo = ?, email = ?, telefono = ?, direccion = ?, estado = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, socio.getNombreCompleto());
            stmt.setString(2, socio.getEmail());
            stmt.setString(3, socio.getTelefono());
            stmt.setString(4, socio.getDireccion());
            stmt.setString(5, socio.getEstado());
            stmt.setLong(6, socio.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar socio", e);
        }
    }

    /**
     * Maps a row from the ResultSet to a Socio domain object.
     * Converts SQL types (e.g., TIMESTAMP) to appropriate Java types (e.g., LocalDateTime).
     */
    private Socio mapResultSetToSocio(ResultSet rs) throws SQLException {
        Socio s = new Socio();
        s.setId(rs.getLong("id"));
        s.setNombreCompleto(rs.getString("nombre_completo"));
        s.setEmail(rs.getString("email"));
        s.setTelefono(rs.getString("telefono"));
        s.setDireccion(rs.getString("direccion"));
        s.setEstado(rs.getString("estado"));
        s.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return s;
    }
}