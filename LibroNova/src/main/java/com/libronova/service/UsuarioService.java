package com.libronova.service;

import com.libronova.dao.UsuarioDAO;
import com.libronova.Model.Usuario;

/**
 * Core service class for managing system users (e.g., administrators and assistants).
 * Handles user creation and authentication logic.
 * Designed to be extended (e.g., by decorators) — hence the 'protected' DAO field.
 */
public class UsuarioService {
    protected final UsuarioDAO usuarioDAO;

    /**
     * Constructor with dependency injection: accepts a UsuarioDAO implementation.
     * This promotes loose coupling and facilitates testing or decoration.
     */
    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Registers a new system user.
     * The caller is expected to set all necessary fields (e.g., role, status) before invoking this method.
     * Delegates persistence to the DAO layer.
     */
    public void crearUsuario(Usuario usuario) {
        usuarioDAO.crear(usuario);
        System.out.println("[POST] Usuario creado: " + usuario.getEmail());
    }

    /**
     * Authenticates a user by email and password.
     * Only active users ('ACTIVO' estado) can log in (enforced in DAO).
     * Logs successful and failed login attempts for auditing/debugging.
     * @return the authenticated Usuario object if credentials are valid and user is active; null otherwise.
     */
    public Usuario login(String email, String password) {
        Usuario u = usuarioDAO.login(email, password);
        if (u != null) {
            System.out.println("[GET] Login exitoso: " + email + " (" + u.getRol() + ")");
        } else {
            System.out.println("[GET] Login fallido: " + email);
        }
        return u;
    }
}