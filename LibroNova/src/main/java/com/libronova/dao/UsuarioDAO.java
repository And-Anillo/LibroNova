/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.libronova.dao;
import com.libronova.Model.Usuario;

import java.util.List;
/**
 *
 * @author Coder
 */
public interface UsuarioDAO {
    Usuario login(String email, String password);
    void crear(Usuario usuario);
    List<Usuario> listarTodos();
}
