package com.libronova.service;

import com.libronova.dao.LibroDAO;
import com.libronova.exception.ISBNAlreadyExistsException;
import com.libronova.Model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibroServiceTest {

    private LibroDAO mockDAO;
    private LibroService service;

    @BeforeEach
    void setUp() {
        mockDAO = Mockito.mock(LibroDAO.class);
        service = new LibroService(mockDAO);
    }

    @Test
    void registrarLibro_ISBNExistente_LanzaExcepcion() {
        Libro libro = new Libro("123", "Test", "Autor", "Ficción", 5, 100.0);
        when(mockDAO.existeISBN("123")).thenReturn(true);

        ISBNAlreadyExistsException ex = assertThrows(ISBNAlreadyExistsException.class,
                () -> service.registrarLibro(libro));
        assertEquals("El ISBN 123 ya existe.", ex.getMessage());
    }

    @Test
    void registrarLibro_ISBNNuevo_RegistraCorrectamente() {
        Libro libro = new Libro("123", "Test", "Autor", "Ficción", 5, 100.0);
        when(mockDAO.existeISBN("123")).thenReturn(false);

        service.registrarLibro(libro);

        verify(mockDAO).crear(libro);
    }
}