/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.libronova.dao;
import com.libronova.Model.Prestamo;

import java.util.List;
/**
 *
 * @author Coder
 */
public interface PrestamoDAO {
    void registrarPrestamo(Prestamo prestamo);
    void registrarDevolucion(Prestamo prestamo);
    List<Prestamo> listarPrestamosVencidos();
    List<Prestamo> listarTodosLosPrestamos();
}
