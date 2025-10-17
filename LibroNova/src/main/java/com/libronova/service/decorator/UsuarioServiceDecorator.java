package com.libronova.service.decorator;

import com.libronova.dao.UsuarioDAO;
import com.libronova.Model.Usuario;
import com.libronova.service.UsuarioService;

import java.time.LocalDateTime;

/**
 * Decorator for UsuarioService that automatically sets default values
 * when creating a new user, such as role, status, and creation timestamp.
 * This ensures consistent user initialization without duplicating logic in the controller.
 */
public class UsuarioServiceDecorator extends UsuarioService {

    /**
     * Constructor that passes the DAO to the parent UsuarioService.
     */
    public UsuarioServiceDecorator(UsuarioDAO usuarioDAO) {
        super(usuarioDAO);
    }

    /**
     * Overrides the base user creation method to inject default values:
     * - Role defaults to "ASISTENTE" if not specified.
     * - Status defaults to "ACTIVO" if not specified.
     * - Creation timestamp is set to the current date and time.
     * After applying defaults, delegates to the parent service to persist the user.
     */
    @Override
    public void crearUsuario(Usuario usuario) {
        // Apply default role if not provided
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("ASISTENTE");
        }
        // Apply default status if not provided
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("ACTIVO");
        }
        // Set creation timestamp
        usuario.setCreatedAt(LocalDateTime.now());

        super.crearUsuario(usuario); // Delegate to the real service for persistence
    }
}