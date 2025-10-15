package com.libronova.service;

import com.libronova.dao.LibroDAO;
import com.libronova.dao.PrestamoDAO;
import com.libronova.dao.SocioDAO;
import com.libronova.exception.InsufficientStockException;
import com.libronova.Model.Libro;
import com.libronova.Model.Socio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrestamoServiceTest {

    private PrestamoDAO mockPrestamoDAO;
    private LibroDAO mockLibroDAO;
    private SocioDAO mockSocioDAO;
    private PrestamoService service;

    @BeforeEach
    void setUp() {
        mockPrestamoDAO = Mockito.mock(PrestamoDAO.class);
        mockLibroDAO = Mockito.mock(LibroDAO.class);
        mockSocioDAO = Mockito.mock(SocioDAO.class);
        service = new PrestamoService(mockPrestamoDAO, mockLibroDAO, mockSocioDAO);
    }

    @Test
    void realizarPrestamo_StockInsuficiente_LanzaExcepcion() {
        Libro libro = new Libro();
        libro.setEjemplaresDisponibles(0);
        Socio socio = new Socio();
        socio.setEstado("ACTIVO");

        when(mockLibroDAO.buscarPorISBN("123")).thenReturn(libro);
        when(mockSocioDAO.buscarPorId(1L)).thenReturn(socio);

        InsufficientStockException ex = assertThrows(InsufficientStockException.class,
                () -> service.realizarPrestamo("123", 1L));
        assertEquals("No hay ejemplares disponibles para el ISBN 123", ex.getMessage());
    }
}