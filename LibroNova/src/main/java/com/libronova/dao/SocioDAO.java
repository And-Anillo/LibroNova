/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.libronova.dao;
import com.libronova.Model.Socio;

import java.util.List;
/**
 *
 * @author Coder
 */
public interface SocioDAO {
    void crear(Socio socio);
    Socio buscarPorId(Long id);
    List<Socio> listarTodos();
    void actualizar(Socio socio);
}
