package com.libronova.util;

import com.libronova.Model.Libro;
import com.libronova.Model.Prestamo;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CSVExporter {

    public static void exportarLibros(List<Libro> libros) throws IOException {
        try (FileWriter writer = new FileWriter("libros_export.csv")) {
            writer.append("ISBN,Título,Autor,Categoría,Ejemplares Totales,Ejemplares Disponibles,Precio,Activo,Fecha Creación\n");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Libro l : libros) {
                writer.append(l.getIsbn()).append(",")
                        .append(l.getTitulo().replace(",", ";")).append(",")
                        .append(l.getAutor().replace(",", ";")).append(",")
                        .append(l.getCategoria()).append(",")
                        .append(String.valueOf(l.getEjemplaresTotales())).append(",")
                        .append(String.valueOf(l.getEjemplaresDisponibles())).append(",")
                        .append(String.valueOf(l.getPrecioReferencia())).append(",")
                        .append(String.valueOf(l.isActivo())).append(",")
                        .append(l.getCreatedAt().format(fmt)).append("\n");
            }
        }
    }

    public static void exportarPrestamosVencidos(List<Prestamo> prestamos) throws IOException {
        try (FileWriter writer = new FileWriter("prestamos_vencidos.csv")) {
            writer.append("ID,ISBN,Título,Socio,Fecha Préstamo,Fecha Devolución,Multa\n");
            for (Prestamo p : prestamos) {
                writer.append(String.valueOf(p.getId())).append(",")
                        .append(p.getIsbn()).append(",")
                        .append(p.getTituloLibro().replace(",", ";")).append(",")
                        .append(p.getNombreSocio().replace(",", ";")).append(",")
                        .append(p.getFechaPrestamo().toString()).append(",")
                        .append(p.getFechaDevolucion() != null ? p.getFechaDevolucion().toString() : "PENDIENTE").append(",")
                        .append(String.valueOf(p.getMulta())).append("\n");
            }
        }
    }
}