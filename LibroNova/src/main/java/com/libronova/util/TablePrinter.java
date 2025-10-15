package com.libronova.util;

import com.libronova.Model.Libro;
import com.libronova.Model.Prestamo;
import com.libronova.Model.Socio;
import com.libronova.Model.Usuario;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TablePrinter {

    public static String printLibros(List<Libro> libros) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %-30s %-20s %-15s %-8s %-8s %-10s %s\n",
                "ISBN", "Título", "Autor", "Categoría", "Tot", "Disp", "Activo", "Creado"));
        sb.append("=".repeat(120)).append("\n");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Libro l : libros) {
            sb.append(String.format("%-15s %-30s %-20s %-15s %-8d %-8d %-10s %s\n",
                    l.getIsbn(),
                    l.getTitulo().length() > 30 ? l.getTitulo().substring(0, 27) + "..." : l.getTitulo(),
                    l.getAutor(),
                    l.getCategoria(),
                    l.getEjemplaresTotales(),
                    l.getEjemplaresDisponibles(),
                    l.isActivo() ? "[ACTIVO]" : "[INACTIVO]",
                    l.getCreatedAt().toLocalDate().format(fmt)));
        }
        return sb.toString();
    }

    public static String printSocios(List<Socio> socios) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-25s %-25s %-12s %s\n", "ID", "Nombre", "Email", "Teléfono", "Estado"));
        sb.append("=".repeat(90)).append("\n");
        for (Socio s : socios) {
            sb.append(String.format("%-5d %-25s %-25s %-12s %s\n",
                    s.getId(),
                    s.getNombreCompleto(),
                    s.getEmail() != null ? s.getEmail() : "",
                    s.getTelefono() != null ? s.getTelefono() : "",
                    s.getEstado()));
        }
        return sb.toString();
    }
}