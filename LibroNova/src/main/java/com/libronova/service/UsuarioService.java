package com.libronova.service;

import com.libronova.dao.UsuarioDAO;
import com.libronova.Model.Usuario;

public class UsuarioService {
    protected final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void crearUsuario(Usuario usuario) {
        usuarioDAO.crear(usuario);
        System.out.println("[POST] Usuario creado: " + usuario.getEmail());
    }

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