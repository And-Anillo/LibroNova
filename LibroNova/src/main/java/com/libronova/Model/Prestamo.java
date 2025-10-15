package com.libronova.Model;

import java.time.LocalDate;

public class Prestamo {
    private Long id;
    private String isbn;
    private Long socioId;
    private String tituloLibro;
    private String nombreSocio;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private double multa;
    private String estado; // "PENDIENTE", "DEVUELTO"

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Long getSocioId() { return socioId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }

    public String getTituloLibro() { return tituloLibro; }
    public void setTituloLibro(String tituloLibro) { this.tituloLibro = tituloLibro; }

    public String getNombreSocio() { return nombreSocio; }
    public void setNombreSocio(String nombreSocio) { this.nombreSocio = nombreSocio; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public double getMulta() { return multa; }
    public void setMulta(double multa) { this.multa = multa; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}