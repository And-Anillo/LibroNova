package com.libronova.service;

import com.libronova.dao.LibroDAO;
import com.libronova.exception.ISBNAlreadyExistsException;
import com.libronova.Model.Libro;

import java.util.List;

public class LibroService {
    private final LibroDAO libroDAO;

    public LibroService(LibroDAO libroDAO) {
        this.libroDAO = libroDAO;
    }

    public void registrarLibro(Libro libro) {
        if (libroDAO.existeISBN(libro.getIsbn())) {
            throw new ISBNAlreadyExistsException("El ISBN " + libro.getIsbn() + " ya existe.");
        }
        libroDAO.crear(libro);
        System.out.println("[POST] Libro registrado: " + libro.getIsbn());
    }

    public void actualizarLibro(Libro libro) {
        libroDAO.actualizar(libro);
        System.out.println("[PATCH] Libro actualizado: " + libro.getIsbn());
    }

    public List<Libro> listarLibros() {
        System.out.println("[GET] Listando todos los libros");
        return libroDAO.listarTodos();
    }

    public List<Libro> filtrarPorAutor(String autor) {
        System.out.println("[GET] Filtrando por autor: " + autor);
        return libroDAO.filtrarPorAutor(autor);
    }

    public List<Libro> filtrarPorCategoria(String categoria) {
        System.out.println("[GET] Filtrando por categoría: " + categoria);
        return libroDAO.filtrarPorCategoria(categoria);
    }
}