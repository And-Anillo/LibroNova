package com.libronova.service;

import com.libronova.dao.SocioDAO;
import com.libronova.Model.Socio;

import java.util.List;

public class SocioService {
    private final SocioDAO socioDAO;

    public SocioService(SocioDAO socioDAO) {
        this.socioDAO = socioDAO;
    }

    public void crearSocio(Socio socio) {
        if (socio.getEstado() == null || socio.getEstado().isEmpty()) {
            socio.setEstado("ACTIVO");
        }
        socioDAO.crear(socio);
        System.out.println("[POST] Socio creado: " + socio.getNombreCompleto());
    }

    public Socio obtenerSocio(Long id) {
        Socio s = socioDAO.buscarPorId(id);
        if (s == null) {
            throw new RuntimeException("Socio con ID " + id + " no encontrado.");
        }
        return s;
    }

    public List<Socio> listarSocios() {
        System.out.println("[GET] Listando todos los socios");
        return socioDAO.listarTodos();
    }

    public void actualizarSocio(Socio socio) {
        socioDAO.actualizar(socio);
        System.out.println("[PATCH] Socio actualizado: " + socio.getId());
    }
}