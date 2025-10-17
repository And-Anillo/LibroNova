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

/**
 * Service class responsible for managing loan (préstamo) business logic.
 * Coordinates between PrestamoDAO, LibroDAO, and SocioDAO to enforce business rules,
 * maintain data consistency, and handle transactions (e.g., stock updates on loan/return).
 */
public class PrestamoService {
    private final PrestamoDAO prestamoDAO;
    private final LibroDAO libroDAO;
    private final SocioDAO socioDAO;

    /**
     * Constructor with dependency injection for all required DAOs.
     * Enables testability and decoupling from concrete data access implementations.
     */
    public PrestamoService(PrestamoDAO prestamoDAO, LibroDAO libroDAO, SocioDAO socioDAO) {
        this.prestamoDAO = prestamoDAO;
        this.libroDAO = libroDAO;
        this.socioDAO = socioDAO;
    }

    /**
     * Performs a new book loan operation within a database transaction.
     * Validates that:
     *   - The member (socio) exists and is ACTIVE.
     *   - The book exists, is active, and has available copies.
     * If all checks pass:
     *   - Records the loan.
     *   - Decreases the available stock of the book.
     * Rolls back the transaction on any error to ensure data consistency.
     */
    public void realizarPrestamo(String isbn, Long socioId) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Validate member (socio)
            Socio socio = socioDAO.buscarPorId(socioId);
            if (socio == null || !"ACTIVO".equals(socio.getEstado())) {
                throw new SocioInactivoException("El socio no está activo o no existe.");
            }

            // Validate book availability and stock
            Libro libro = libroDAO.buscarPorISBN(isbn);
            if (libro == null || !libro.isActivo()) {
                throw new RuntimeException("Libro no disponible.");
            }
            if (libro.getEjemplaresDisponibles() <= 0) {
                throw new InsufficientStockException("No hay ejemplares disponibles para el ISBN " + isbn);
            }

            // Register the loan
            Prestamo p = new Prestamo();
            p.setIsbn(isbn);
            p.setSocioId(socioId);
            p.setFechaPrestamo(LocalDate.now());
            p.setEstado("PENDIENTE");
            prestamoDAO.registrarPrestamo(p);

            // Decrease available stock
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
                    // Ignore close errors
                }
            }
        }
    }

    /**
     * Processes the return of a borrowed book within a database transaction.
     * Calculates fine (multa) based on overdue days using system configuration.
     * Note: This implementation contains a simplification—the actual loan date
     * should be retrieved from the database, not simulated.
     * In a real system, the ISBN would also be fetched to restore book stock.
     * Currently, stock restoration is omitted for brevity.
     */
    public void realizarDevolucion(Long prestamoId, LocalDate fechaDevolucion) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Simulate loan data retrieval (in practice, this should come from the DB)
            // TODO: Fetch actual Prestamo with fecha_prestamo and ISBN from DAO

            // Retrieve system configuration
            int diasPrestamo = DBConnection.getDiasPrestamo();
            double multaPorDia = DBConnection.getMultaPorDia();

            // ⚠️ SIMPLIFICATION: assumes loan was made 10 days before return date
            // In reality, you must query the actual fecha_prestamo from the database
            LocalDate fechaPrestamo = fechaDevolucion.minusDays(10);
            long diasRetraso = java.time.temporal.ChronoUnit.DAYS.between(
                    fechaPrestamo.plusDays(diasPrestamo), fechaDevolucion
            );
            double multa = diasRetraso > 0 ? diasRetraso * multaPorDia : 0.0;

            // Update loan record with return date and fine
            Prestamo p = new Prestamo();
            p.setId(prestamoId);
            p.setFechaDevolucion(fechaDevolucion);
            p.setMulta(multa);
            prestamoDAO.registrarDevolucion(p);

            // ⚠️ STOCK RESTORATION OMITTED: In a complete implementation,
            // you would retrieve the ISBN from the loan and increment ejemplares_disponibles

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
                    // Ignore close errors
                }
            }
        }
    }

    /**
     * Retrieves a list of all overdue loans.
     * Delegates to the DAO, which performs a JOIN to include book title and member name.
     */
    public List<Prestamo> obtenerPrestamosVencidos() {
        System.out.println("[GET] Listando préstamos vencidos");
        return prestamoDAO.listarPrestamosVencidos();
    }
    
    /**
     * Retrieves a complete list of all loans (both pending and returned).
     * Useful for administrative reporting or audit purposes.
     */
    public List<Prestamo> obtenerTodosLosPrestamos() {
        System.out.println("[GET] Listando todos los préstamos");
        return prestamoDAO.listarTodosLosPrestamos();
    }
}