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
public class Books {
    private String isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private int ejemplaresTotales;
    private int ejemplaresDisponibles;
    private double precioReferencia;
    private boolean activo;
    private LocalDate createdAt;

    public Books(String isbn, String titulo, String autor, String categoria, int ejemplaresTotales, int ejemplaresDisponibles, double precioReferencia, boolean activo, LocalDate createdAt) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.ejemplaresTotales = ejemplaresTotales;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
        this.precioReferencia = precioReferencia;
        this.activo = activo;
        this.createdAt = createdAt;
    }       

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getEjemplaresTotales() {
        return ejemplaresTotales;
    }

    public void setEjemplaresTotales(int ejemplaresTotales) {
        this.ejemplaresTotales = ejemplaresTotales;
    }

    public int getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public void setEjemplaresDisponibles(int ejemplaresDisponibles) {
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    public double getPrecioReferencia() {
        return precioReferencia;
    }

    public void setPrecioReferencia(double precioReferencia) {
        this.precioReferencia = precioReferencia;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
     @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | %d/%d | %.2f | %s | %s",
                isbn, titulo, autor, categoria,
                ejemplaresDisponibles, ejemplaresTotales,
                precioReferencia,
                activo ? "ACTIVO" : "INACTIVO",
                createdAt);
    }
}
