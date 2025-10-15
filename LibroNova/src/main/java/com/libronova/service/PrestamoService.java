package com.libronova.service;

import com.libronova.dao.LibroDAO;
import com.libronova.dao.PrestamoDAO;
import com.libronova.dao.SocioDAO;
import com.libronova.exception.InsufficientStockException;
import com.libronova.exception.SocioInactivoException;
import com.libronova.Model.Libro;
import com.libronova.Model.Prestamo;
import com.libronova.Model.Socio;
import com.libronova.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrestamoService {
    private final PrestamoDAO prestamoDAO;
    private final LibroDAO libroDAO;
    private final SocioDAO socioDAO;

    public PrestamoService(PrestamoDAO prestamoDAO, LibroDAO libroDAO, SocioDAO socioDAO) {
        this.prestamoDAO = prestamoDAO;
        this.libroDAO = libroDAO;
        this.socioDAO = socioDAO;
    }

    public void realizarPrestamo(String isbn, Long socioId) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Validar socio
            Socio socio = socioDAO.buscarPorId(socioId);
            if (socio == null || !"ACTIVO".equals(socio.getEstado())) {
                throw new SocioInactivoException("El socio no está activo o no existe.");
            }

            // Validar libro y stock
            Libro libro = libroDAO.buscarPorISBN(isbn);
            if (libro == null || !libro.isActivo()) {
                throw new RuntimeException("Libro no disponible.");
            }
            if (libro.getEjemplaresDisponibles() <= 0) {
                throw new InsufficientStockException("No hay ejemplares disponibles para el ISBN " + isbn);
            }

            // Registrar préstamo
            Prestamo p = new Prestamo();
            p.setIsbn(isbn);
            p.setSocioId(socioId);
            p.setFechaPrestamo(LocalDate.now());
            p.setEstado("PENDIENTE");
            prestamoDAO.registrarPrestamo(p);

            // Reducir stock
            libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
            libroDAO.actualizar(libro);

            conn.commit();
            System.out.println("[POST] Préstamo registrado: ISBN=" + isbn + ", Socio=" + socioId);

        } catch (SQLException | RuntimeException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Error en rollback", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // ignorar
                }
            }
        }
    }

    public void realizarDevolucion(Long prestamoId, LocalDate fechaDevolucion) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Aquí deberías cargar el préstamo completo (con JOINs) → simplificado
            // Para este ejemplo, asumimos que ya tienes los datos necesarios
            // En implementación real, harías un DAO que devuelva Prestamo con título y nombre

            // Simulación: calcular multa
            int diasPrestamo = DBConnection.getDiasPrestamo();
            double multaPorDia = DBConnection.getMultaPorDia();

            // Supongamos que la fecha de préstamo fue hace 10 días
            LocalDate fechaPrestamo = fechaDevolucion.minusDays(10); // esto debe venir de BD
            long diasRetraso = java.time.temporal.ChronoUnit.DAYS.between(
                    fechaPrestamo.plusDays(diasPrestamo), fechaDevolucion
            );
            double multa = diasRetraso > 0 ? diasRetraso * multaPorDia : 0.0;

            // Actualizar préstamo
            Prestamo p = new Prestamo();
            p.setId(prestamoId);
            p.setFechaDevolucion(fechaDevolucion);
            p.setMulta(multa);
            prestamoDAO.registrarDevolucion(p);

            // Aumentar stock (necesitas ISBN → harías un JOIN en DAO real)
            // Aquí omitido por simplicidad; en práctica, recuperas el ISBN del préstamo

            conn.commit();
            System.out.println("[PATCH] Devolución registrada: ID=" + prestamoId + ", Multa=" + multa);

        } catch (SQLException | RuntimeException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Error en rollback", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // ignorar
                }
            }
        }
    }

    public List<Prestamo> obtenerPrestamosVencidos() {
        System.out.println("[GET] Listando préstamos vencidos");
        return prestamoDAO.listarPrestamosVencidos();
    }
}