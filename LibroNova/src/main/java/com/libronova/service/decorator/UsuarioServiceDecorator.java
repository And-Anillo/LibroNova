package com.libronova.service.decorator;

import com.libronova.dao.UsuarioDAO;
import com.libronova.Model.Usuario;
import com.libronova.service.UsuarioService;

import java.time.LocalDateTime;

// Decorador que agrega propiedades por defecto
public class UsuarioServiceDecorator extends UsuarioService {

    public UsuarioServiceDecorator(UsuarioDAO usuarioDAO) {
        super(usuarioDAO);
    }

    @Override
    public void crearUsuario(Usuario usuario) {
        // Aplicar decorador
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("ASISTENTE");
        }
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("ACTIVO");
        }
        usuario.setCreatedAt(LocalDateTime.now());

        super.crearUsuario(usuario); // delegar al servicio real
    }
}