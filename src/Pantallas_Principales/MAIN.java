/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pantallas_Principales;

import AreaChat.ServidorChat;
import AreaChat.ServidorChatPrivado;

/**
 *^
 * @author royum
 */
public class MAIN { 

    /**
     * @param args the command line arguments
     */ 
    
    public static void main(String[] args) {
        
     // Iniciar el servidor de chat general en un hilo separado
        Thread servidorGeneralThread = new Thread(() -> {
            try {
                ServidorChat.main(null); 
            } catch (Exception e) {
                System.err.println("Error iniciando el servidor general: " + e.getMessage());
            }
        });
        servidorGeneralThread.setDaemon(true);
        servidorGeneralThread.start();

        // Iniciar el servidor de chat privado en un hilo separado
        Thread servidorPrivadoThread = new Thread(() -> {
            try {
                ServidorChatPrivado.main(null);
            } catch (Exception e) {
                System.err.println("Error iniciando el servidor privado: " + e.getMessage());
            }
        });
        servidorPrivadoThread.setDaemon(true);
        servidorPrivadoThread.start();

        // Iniciar la interfaz principal del cliente
        MenuInicio inicio = new MenuInicio();
        inicio.setVisible(true);
        inicio.setLocationRelativeTo(null);
    }
}