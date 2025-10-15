/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.libronova.dao;
import com.libronova.Model.Libro;

import java.util.List;
/**
 *
 * @author Coder
 */
public interface LibroDAO {
    void crear(Libro libro);
    void actualizar(Libro libro);
    Libro buscarPorISBN(String isbn);
    List<Libro> listarTodos();
    List<Libro> filtrarPorAutor(String autor);
    List<Libro> filtrarPorCategoria(String categoria);
    boolean existeISBN(String isbn);
}
