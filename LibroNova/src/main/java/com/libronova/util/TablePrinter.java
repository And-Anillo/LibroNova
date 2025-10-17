package com.libronova.util;

import com.libronova.Model.Libro;
import com.libronova.Model.Prestamo;
import com.libronova.Model.Socio;
import com.libronova.Model.Usuario;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for formatting domain objects (e.g., Libro, Socio) into human-readable text tables.
 * Used primarily for console output or simple GUI displays (e.g., in JOptionPane).
 */
public class TablePrinter {

    /**
     * Formats a list of books into a fixed-width text table.
     * Truncates long titles to fit within column width and appends "..." if needed.
     * Displays active status as readable labels and formats the creation date as YYYY-MM-DD.
     */
    public static String printLibros(List<Libro> libros) {
        StringBuilder sb = new StringBuilder();
        // Table header
        sb.append(String.format("%-15s %-30s %-20s %-15s %-8s %-8s %-10s %s\n",
                "ISBN", "Título", "Autor", "Categoría", "Tot", "Disp", "Activo", "Creado"));
        sb.append("=".repeat(120)).append("\n");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Libro l : libros) {
            sb.append(String.format("%-15s %-30s %-20s %-15s %-8d %-8d %-10s %s\n",
                    l.getIsbn(),
                    l.getTitulo().length() > 30 ? l.getTitulo().substring(0, 27) + "..." : l.getTitulo(), // Truncate long titles
                    l.getAutor(),
                    l.getCategoria(),
                    l.getEjemplaresTotales(),
                    l.getEjemplaresDisponibles(),
                    l.isActivo() ? "[ACTIVO]" : "[INACTIVO]", // Human-readable status
                    l.getCreatedAt().toLocalDate().format(fmt))); // Format date
        }
        return sb.toString();
    }

    /**
     * Formats a list of library members (socios) into a fixed-width text table.
     * Handles optional fields (email, phone) by displaying empty strings if null.
     */
    public static String printSocios(List<Socio> socios) {
        StringBuilder sb = new StringBuilder();
        // Table header
        sb.append(String.format("%-5s %-25s %-25s %-12s %s\n", "ID", "Nombre", "Email", "Teléfono", "Estado"));
        sb.append("=".repeat(90)).append("\n");
        for (Socio s : socios) {
            sb.append(String.format("%-5d %-25s %-25s %-12s %s\n",
                    s.getId(),
                    s.getNombreCompleto(),
                    s.getEmail() != null ? s.getEmail() : "",      // Handle null email
                    s.getTelefono() != null ? s.getTelefono() : "", // Handle null phone
                    s.getEstado()));
        }
        return sb.toString();
    }
}