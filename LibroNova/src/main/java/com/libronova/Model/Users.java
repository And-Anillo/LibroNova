/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.Model;

import java.time.LocalDate;

/**
 *
 * @author Coder
 */
public class Users {
    private int id;
    private String nombre;
    private String email;
    private String password;
    private String role; // ADMIN o ASISTENTE
    private String estado; // ACTIVO o INACTIVO
    private LocalDate createdAt;
}
