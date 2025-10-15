package com.libronova.Model;

import java.time.LocalDateTime;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private int ejemplaresTotales;
    private int ejemplaresDisponibles;
    private double precioReferencia;
    private boolean isActivo;
    private LocalDateTime createdAt;

    // Constructores
    public Libro() {}

    public Libro(String isbn, String titulo, String autor, String categoria,
                 int ejemplaresTotales, double precioReferencia) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.ejemplaresTotales = ejemplaresTotales;
        this.ejemplaresDisponibles = ejemplaresTotales;
        this.precioReferencia = precioReferencia;
        this.isActivo = true;
        this.createdAt = LocalDateTime.now();
    }

    // Getters y Setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getEjemplaresTotales() { return ejemplaresTotales; }
    public void setEjemplaresTotales(int ejemplaresTotales) {
        this.ejemplaresTotales = ejemplaresTotales;
    }

    public int getEjemplaresDisponibles() { return ejemplaresDisponibles; }
    public void setEjemplaresDisponibles(int ejemplaresDisponibles) {
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    public double getPrecioReferencia() { return precioReferencia; }
    public void setPrecioReferencia(double precioReferencia) { this.precioReferencia = precioReferencia; }

    public boolean isActivo() { return isActivo; }
    public void setActivo(boolean activo) { isActivo = activo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}