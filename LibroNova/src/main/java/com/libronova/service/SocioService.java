package com.libronova.service;

import com.libronova.dao.SocioDAO;
import com.libronova.Model.Socio;

import java.util.List;

/**
 * Service class for managing library member (socio) business logic.
 * Acts as an intermediary between the controller and the SocioDAO,
 * providing validation, default values, and error handling.
 */
public class SocioService {
    private final SocioDAO socioDAO;

    /**
     * Constructor with dependency injection: accepts a SocioDAO implementation.
     * This design supports testability and loose coupling.
     */
    public SocioService(SocioDAO socioDAO) {
        this.socioDAO = socioDAO;
    }

    /**
     * Creates a new library member (socio).
     * If no status (estado) is provided, defaults to "ACTIVO".
     * Delegates persistence to the DAO layer.
     */
    public void crearSocio(Socio socio) {
        if (socio.getEstado() == null || socio.getEstado().isEmpty()) {
            socio.setEstado("ACTIVO");
        }
        socioDAO.crear(socio);
        System.out.println("[POST] Socio creado: " + socio.getNombreCompleto());
    }

    /**
     * Retrieves a member by their unique ID.
     * Throws a runtime exception if the member is not found.
     */
    public Socio obtenerSocio(Long id) {
        Socio s = socioDAO.buscarPorId(id);
        if (s == null) {
            throw new RuntimeException("Socio con ID " + id + " no encontrado.");
        }
        return s;
    }

    /**
     * Returns a list of all registered members, ordered by creation date (newest first).
     * Logs the operation for traceability.
     */
    public List<Socio> listarSocios() {
        System.out.println("[GET] Listando todos los socios");
        return socioDAO.listarTodos();
    }

    /**
     * Updates an existing member's information (e.g., contact details or status).
     * Assumes the member exists; validation is typically handled at the controller level.
     */
    public void actualizarSocio(Socio socio) {
        socioDAO.actualizar(socio);
        System.out.println("[PATCH] Socio actualizado: " + socio.getId());
    }
}