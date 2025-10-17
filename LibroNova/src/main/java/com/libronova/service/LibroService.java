package com.libronova.service;

import com.libronova.dao.LibroDAO;
import com.libronova.exception.ISBNAlreadyExistsException;
import com.libronova.Model.Libro;

import java.util.List;

/**
 * Service class for managing book-related business logic.
 * Acts as an intermediary between the controller and the data access layer (DAO).
 * Enforces business rules such as ISBN uniqueness.
 */
public class LibroService {
    private final LibroDAO libroDAO;

    /**
     * Constructor with dependency injection: accepts a LibroDAO implementation.
     * This promotes loose coupling and testability.
     */
    public LibroService(LibroDAO libroDAO) {
        this.libroDAO = libroDAO;
    }

    /**
     * Registers a new book in the system.
     * Validates that the ISBN is not already in use before saving.
     * Throws ISBNAlreadyExistsException if the ISBN is duplicated.
     */
    public void registrarLibro(Libro libro) {
        if (libroDAO.existeISBN(libro.getIsbn())) {
            throw new ISBNAlreadyExistsException("El ISBN " + libro.getIsbn() + " ya existe.");
        }
        libroDAO.crear(libro);
        System.out.println("[POST] Libro registrado: " + libro.getIsbn());
    }

    /**
     * Updates an existing book's information.
     * Assumes the book already exists (no ISBN validation needed here).
     */
    public void actualizarLibro(Libro libro) {
        libroDAO.actualizar(libro);
        System.out.println("[PATCH] Libro actualizado: " + libro.getIsbn());
    }

    /**
     * Retrieves a list of all books in the catalog.
     * Delegates to the DAO and logs the operation.
     */
    public List<Libro> listarLibros() {
        System.out.println("[GET] Listando todos los libros");
        return libroDAO.listarTodos();
    }

    /**
     * Filters books by author name (case-insensitive partial match).
     * Logs the search criteria for debugging or auditing.
     */
    public List<Libro> filtrarPorAutor(String autor) {
        System.out.println("[GET] Filtrando por autor: " + autor);
        return libroDAO.filtrarPorAutor(autor);
    }

    /**
     * Filters books by exact category match.
     * Logs the category being queried.
     */
    public List<Libro> filtrarPorCategoria(String categoria) {
        System.out.println("[GET] Filtrando por categoría: " + categoria);
        return libroDAO.filtrarPorCategoria(categoria);
    }
}