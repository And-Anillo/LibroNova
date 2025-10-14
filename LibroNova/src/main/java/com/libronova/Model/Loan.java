/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.Model;

import java.time.LocalDate;
/**
 *
 * @author Coder
 */
public class Loan {
    private int id;
    private String isbnLibro;
    private int idSocio;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto;
    private double multa;
}
