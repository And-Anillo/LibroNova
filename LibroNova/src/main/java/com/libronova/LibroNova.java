/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.libronova;

import com.libronova.Config.ConexionDB;
import java.sql.Connection;

/**
 *
 * @author Coder
 */
public class LibroNova {

    public static void main(String[] args) {
        try (Connection conn = ConexionDB.getConnection()) {
            if (conn != null) {
                System.out.println("Prueba completada correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
