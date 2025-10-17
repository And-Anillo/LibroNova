package com.libronova.dao.impl;

import com.libronova.dao.UsuarioDAO;
import com.libronova.Model.Usuario;
import com.libronova.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of the UsuarioDAO interface.
 * Handles database operations for system users (e.g., administrators and assistants),
 * including authentication, creation, and listing.
 */
public class JDBCUsuarioDAO implements UsuarioDAO {

    /**
     * Authenticates a user by email and password.
     * Only returns a user if credentials match AND the account is active ('ACTIVO').
     * Returns null if authentication fails or the user is inactive.
     */
    @Override
    public Usuario login(String email, String password) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ? AND estado = 'ACTIVO'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getString("estado"));
                u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return u;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en login", e);
        }
        return null;
    }

    /**
     * Inserts a new system user into the 'usuarios' table.
     * The caller is expected to set all fields (including role and status) before calling this method.
     */
    @Override
    public void crear(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nombre, email, password, rol, estado, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getRol());
            stmt.setString(5, usuario.getEstado());
            stmt.setTimestamp(6, Timestamp.valueOf(usuario.getCreatedAt()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear usuario", e);
        }
    }

    /**
     * Retrieves a list of all system users, ordered by creation date (newest first).
     * Includes all user details: ID, name, email, role, status, and creation timestamp.
     */
    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getString("estado"));
                u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                usuarios.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }
        return usuarios;
    }
}