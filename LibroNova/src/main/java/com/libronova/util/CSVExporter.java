package com.libronova.util;

import com.libronova.Model.Libro;
import com.libronova.Model.Prestamo;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for exporting data to CSV files.
 * Provides static methods to generate CSV reports for books and overdue loans.
 * Handles basic CSV escaping by replacing commas in text fields with semicolons.
 */
public class CSVExporter {

    /**
     * Exports a list of books to a CSV file named "libros_export.csv".
     * Includes headers and formats the creation date consistently.
     * Commas in textual fields (e.g., title, author) are replaced with semicolons to avoid CSV format issues.
     */
    public static void exportarLibros(List<Libro> libros) throws IOException {
        try (FileWriter writer = new FileWriter("libros_export.csv")) {
            // Write CSV header
            writer.append("ISBN,Título,Autor,Categoría,Ejemplares Totales,Ejemplares Disponibles,Precio,Activo,Fecha Creación\n");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Libro l : libros) {
                writer.append(l.getIsbn()).append(",")
                        .append(l.getTitulo().replace(",", ";")).append(",")          // Escape commas in title
                        .append(l.getAutor().replace(",", ";")).append(",")           // Escape commas in author
                        .append(l.getCategoria()).append(",")
                        .append(String.valueOf(l.getEjemplaresTotales())).append(",")
                        .append(String.valueOf(l.getEjemplaresDisponibles())).append(",")
                        .append(String.valueOf(l.getPrecioReferencia())).append(",")
                        .append(String.valueOf(l.isActivo())).append(",")
                        .append(l.getCreatedAt().format(fmt)).append("\n");
            }
        }
    }

    /**
     * Exports a list of overdue loans to a CSV file named "prestamos_vencidos.csv".
     * Includes key loan details such as book info, member name, dates, and fine amount.
     * If a return date is missing (shouldn't happen for overdue loans in practice), it writes "PENDIENTE".
     * Commas in title and member name are replaced with semicolons for CSV safety.
     */
    public static void exportarPrestamosVencidos(List<Prestamo> prestamos) throws IOException {
        try (FileWriter writer = new FileWriter("prestamos_vencidos.csv")) {
            // Write CSV header
            writer.append("ID,ISBN,Título,Socio,Fecha Préstamo,Fecha Devolución,Multa\n");
            for (Prestamo p : prestamos) {
                writer.append(String.valueOf(p.getId())).append(",")
                        .append(p.getIsbn()).append(",")
                        .append(p.getTituloLibro().replace(",", ";")).append(",")    // Escape commas in book title
                        .append(p.getNombreSocio().replace(",", ";")).append(",")     // Escape commas in member name
                        .append(p.getFechaPrestamo().toString()).append(",")
                        .append(p.getFechaDevolucion() != null ? p.getFechaDevolucion().toString() : "PENDIENTE").append(",")
                        .append(String.valueOf(p.getMulta())).append("\n");
            }
        }
    }
}