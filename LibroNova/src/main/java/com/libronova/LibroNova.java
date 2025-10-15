/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.libronova;

import com.libronova.controller.AppController;
import com.sun.tools.javac.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
/**
 *
 * @author Coder
 */
public class LibroNova {

    public static void main(String[] args) {
        try {
            InputStream logging = Main.class.getClassLoader().getResourceAsStream("logging.properties");
            if (logging != null) {
                LogManager.getLogManager().readConfiguration(logging);
            }
        } catch (IOException e) {
            System.err.println("⚠️ No se pudo cargar logging.properties");
        }

        new AppController().iniciar();
    }
}
