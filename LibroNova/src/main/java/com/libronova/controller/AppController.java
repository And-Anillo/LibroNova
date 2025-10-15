package com.libronova.controller;

import com.libronova.dao.impl.*;
import com.libronova.exception.InsufficientStockException;
import com.libronova.exception.SocioInactivoException;
import com.libronova.Model.*;
import com.libronova.service.*;
import com.libronova.service.decorator.UsuarioServiceDecorator;
import com.libronova.util.CSVExporter;
import com.libronova.util.TablePrinter;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class AppController {
    private static final Logger logger = Logger.getLogger(AppController.class.getName());
    private Usuario usuarioLogueado;

    private final LibroService libroService;
    private final UsuarioService usuarioService;
    private final SocioService socioService;
    private final PrestamoService prestamoService;

    public AppController() {
        this.libroService = new LibroService(new JDBCLibroDAO());
        this.usuarioService = new UsuarioServiceDecorator(new JDBCUsuarioDAO());
        this.socioService = new SocioService(new JDBCSocioDAO());
        this.prestamoService = new PrestamoService(new JDBCPrestamoDAO(), new JDBCLibroDAO(), new JDBCSocioDAO());
    }

    public void iniciar() {
        if (!login()) return;

        String[] menu = {"Catálogo", "Socios", "Usuarios", "Préstamos", "Exportar", "Salir"};
        while (true) {
            String opcion = (String) JOptionPane.showInputDialog(null,
                    "Usuario: " + usuarioLogueado.getEmail() + " (" + usuarioLogueado.getRol() + ")\nSeleccione opción:",
                    "LibroNova - Menú Principal",
                    JOptionPane.QUESTION_MESSAGE, null, menu, menu[0]);

            if (opcion == null || "Salir".equals(opcion)) break;

            try {
                switch (opcion) {
                    case "Catálogo" -> gestionarCatalogo();
                    case "Socios" -> gestionarSocios();
                    case "Usuarios" -> gestionarUsuarios();
                    case "Préstamos" -> gestionarPrestamos();
                    case "Exportar" -> exportarDatos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "❌ Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                logger.severe("Excepción en menú: " + e.getMessage());
            }
        }
    }

    private boolean login() {
        String email = JOptionPane.showInputDialog("📧 Email:");
        String pass = JOptionPane.showInputDialog("🔒 Contraseña:");
        if (email == null || pass == null) return false;

        usuarioLogueado = usuarioService.login(email, pass);
        if (usuarioLogueado == null) {
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas o usuario inactivo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // =============== CATÁLOGO ===============
    private void gestionarCatalogo() {
        String[] ops = {"Registrar", "Listar", "Filtrar por Autor", "Filtrar por Categoría"};
        String op = (String) JOptionPane.showInputDialog(null, "Opción:", "Catálogo", JOptionPane.QUESTION_MESSAGE, null, ops, ops[0]);
        if (op == null) return;

        try {
            switch (op) {
                case "Registrar" -> {
                    String isbn = JOptionPane.showInputDialog("ISBN (único):");
                    String titulo = JOptionPane.showInputDialog("Título:");
                    String autor = JOptionPane.showInputDialog("Autor:");
                    String cat = JOptionPane.showInputDialog("Categoría:");
                    int ej = Integer.parseInt(JOptionPane.showInputDialog("Ejemplares totales:"));
                    double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio referencia:"));
                    libroService.registrarLibro(new Libro(isbn, titulo, autor, cat, ej, precio));
                    JOptionPane.showMessageDialog(null, "✅ Libro registrado.");
                }
                case "Listar" -> {
                    List<Libro> libros = libroService.listarLibros();
                    if (libros.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay libros registrados.");
                    } else {
                        String tabla = TablePrinter.printLibros(libros);
                        JTextArea area = new JTextArea(tabla);
                        area.setEditable(false);
                        JScrollPane scroll = new JScrollPane(area);
                        JOptionPane.showMessageDialog(null, scroll, "Catálogo completo", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                case "Filtrar por Autor" -> {
                    String autor = JOptionPane.showInputDialog("Autor:");
                    List<Libro> libros = libroService.filtrarPorAutor(autor);
                    if (libros.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No se encontraron libros del autor: " + autor);
                    } else {
                        String tabla = TablePrinter.printLibros(libros);
                        JTextArea area = new JTextArea(tabla);
                        area.setEditable(false);
                        JScrollPane scroll = new JScrollPane(area);
                        JOptionPane.showMessageDialog(null, scroll, "Filtrado por Autor", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                case "Filtrar por Categoría" -> {
                    String cat = JOptionPane.showInputDialog("Categoría:");
                    List<Libro> libros = libroService.filtrarPorCategoria(cat);
                    if (libros.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay libros en la categoría: " + cat);
                    } else {
                        String tabla = TablePrinter.printLibros(libros);
                        JTextArea area = new JTextArea(tabla);
                        area.setEditable(false);
                        JScrollPane scroll = new JScrollPane(area);
                        JOptionPane.showMessageDialog(null, scroll, "Filtrado por Categoría", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "❌ Formato numérico inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =============== SOCIOS ===============
    private void gestionarSocios() {
        if (!usuarioLogueado.isAdmin()) {
            JOptionPane.showMessageDialog(null, "⚠️ Solo ADMIN puede gestionar socios.");
            return;
        }

        String[] ops = {"Registrar", "Listar", "Actualizar estado"};
        String op = (String) JOptionPane.showInputDialog(null, "Opción:", "Socios", JOptionPane.QUESTION_MESSAGE, null, ops, ops[0]);
        if (op == null) return;

        try {
            switch (op) {
                case "Registrar" -> {
                    String nombre = JOptionPane.showInputDialog("Nombre completo:");
                    String email = JOptionPane.showInputDialog("Email (opcional):");
                    String tel = JOptionPane.showInputDialog("Teléfono (opcional):");
                    String dir = JOptionPane.showInputDialog("Dirección (opcional):");

                    Socio s = new Socio();
                    s.setNombreCompleto(nombre);
                    s.setEmail(email.isEmpty() ? null : email);
                    s.setTelefono(tel.isEmpty() ? null : tel);
                    s.setDireccion(dir.isEmpty() ? null : dir);
                    socioService.crearSocio(s);
                    JOptionPane.showMessageDialog(null, "✅ Socio registrado con ID: " + s.getId());
                }
                case "Listar" -> {
                    List<Socio> socios = socioService.listarSocios();
                    if (socios.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay socios registrados.");
                    } else {
                        String tabla = TablePrinter.printSocios(socios);
                        JTextArea area = new JTextArea(tabla);
                        area.setEditable(false);
                        JScrollPane scroll = new JScrollPane(area);
                        JOptionPane.showMessageDialog(null, scroll, "Listado de Socios", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                case "Actualizar estado" -> {
                    Long id = Long.parseLong(JOptionPane.showInputDialog("ID del socio:"));
                    Socio s = socioService.obtenerSocio(id);
                    String nuevoEstado = (String) JOptionPane.showInputDialog(null,
                            "Estado actual: " + s.getEstado() + "\nNuevo estado:",
                            "Actualizar estado",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new String[]{"ACTIVO", "INACTIVO"},
                            s.getEstado());
                    if (nuevoEstado != null) {
                        s.setEstado(nuevoEstado);
                        socioService.actualizarSocio(s);
                        JOptionPane.showMessageDialog(null, "✅ Estado actualizado.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "❌ ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =============== USUARIOS ===============
    private void gestionarUsuarios() {
        if (!usuarioLogueado.isAdmin()) {
            JOptionPane.showMessageDialog(null, "⚠️ Solo ADMIN puede gestionar usuarios.");
            return;
        }

        String[] ops = {"Registrar"};
        String op = (String) JOptionPane.showInputDialog(null, "Opción:", "Usuarios", JOptionPane.QUESTION_MESSAGE, null, ops, ops[0]);
        if (op == null || !"Registrar".equals(op)) return;

        try {
            String nombre = JOptionPane.showInputDialog("Nombre:");
            String email = JOptionPane.showInputDialog("Email:");
            String pass = JOptionPane.showInputDialog("Contraseña:");
            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setEmail(email);
            u.setPassword(pass);
            // Rol y estado se asignan por decorador
            usuarioService.crearUsuario(u);
            JOptionPane.showMessageDialog(null, "✅ Usuario creado como ASISTENTE.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =============== PRÉSTAMOS ===============
    private void gestionarPrestamos() {
        String[] ops = {"Registrar préstamo", "Registrar devolución", "Ver préstamos vencidos"};
        String op = (String) JOptionPane.showInputDialog(null, "Opción:", "Préstamos", JOptionPane.QUESTION_MESSAGE, null, ops, ops[0]);
        if (op == null) return;

        try {
            switch (op) {
                case "Registrar préstamo" -> {
                    String isbn = JOptionPane.showInputDialog("ISBN del libro:");
                    Long socioId = Long.parseLong(JOptionPane.showInputDialog("ID del socio:"));
                    prestamoService.realizarPrestamo(isbn, socioId);
                    JOptionPane.showMessageDialog(null, "✅ Préstamo registrado.");
                }
                case "Registrar devolución" -> {
                    Long prestamoId = Long.parseLong(JOptionPane.showInputDialog("ID del préstamo:"));
                    String fechaStr = JOptionPane.showInputDialog("Fecha de devolución (yyyy-MM-dd):");
                    LocalDate fechaDev = LocalDate.parse(fechaStr);
                    prestamoService.realizarDevolucion(prestamoId, fechaDev);
                    JOptionPane.showMessageDialog(null, "✅ Devolución registrada.");
                }
                case "Ver préstamos vencidos" -> {
                    List<Prestamo> vencidos = prestamoService.obtenerPrestamosVencidos();
                    if (vencidos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay préstamos vencidos.");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(String.format("%-5s %-15s %-25s %-20s %-12s %s\n", "ID", "ISBN", "Título", "Socio", "Multa", "Vencido"));
                        sb.append("=".repeat(90)).append("\n");
                        for (Prestamo p : vencidos) {
                            long dias = java.time.temporal.ChronoUnit.DAYS.between(
                                    p.getFechaPrestamo().plusDays(com.libronova.util.DBConnection.getDiasPrestamo()),
                                    LocalDate.now()
                            );
                            sb.append(String.format("%-5d %-15s %-25s %-20s $%-11.0f +%d días\n",
                                    p.getId(),
                                    p.getIsbn(),
                                    p.getTituloLibro().length() > 25 ? p.getTituloLibro().substring(0, 22) + "..." : p.getTituloLibro(),
                                    p.getNombreSocio().length() > 20 ? p.getNombreSocio().substring(0, 17) + "..." : p.getNombreSocio(),
                                    p.getMulta(),
                                    dias));
                        }
                        JTextArea area = new JTextArea(sb.toString());
                        area.setEditable(false);
                        JScrollPane scroll = new JScrollPane(area);
                        JOptionPane.showMessageDialog(null, scroll, "Préstamos Vencidos", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "❌ ID o formato numérico inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "❌ Formato de fecha inválido. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InsufficientStockException | SocioInactivoException e) {
            JOptionPane.showMessageDialog(null, "⚠️ " + e.getMessage(), "Regla de negocio", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =============== EXPORTAR ===============
    private void exportarDatos() {
        try {
            List<Libro> libros = libroService.listarLibros();
            CSVExporter.exportarLibros(libros);

            List<Prestamo> vencidos = prestamoService.obtenerPrestamosVencidos();
            CSVExporter.exportarPrestamosVencidos(vencidos);

            JOptionPane.showMessageDialog(null,
                    "✅ Archivos exportados:\n" +
                    "- libros_export.csv\n" +
                    "- prestamos_vencidos.csv\n\n" +
                    "Ubicación: carpeta del proyecto",
                    "Exportación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al exportar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}