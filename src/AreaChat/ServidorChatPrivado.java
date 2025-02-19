/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AreaChat;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author royum
 */
public class ServidorChatPrivado {
    
    private static final int PUERTO = 54321;
    private static Map<String, ObjectOutputStream> clientesPrivados = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Servidor de chats privados iniciado...");
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            while (true) {
                Socket cliente = servidor.accept();
                new Thread(new ManejadorClientePrivado(cliente)).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor privado: " + e.getMessage());
        }
    }

    private static class ManejadorClientePrivado implements Runnable {
        private Socket socket;
        private String usuario;
        private ObjectOutputStream salida;

        public ManejadorClientePrivado(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {
                salida = new ObjectOutputStream(socket.getOutputStream());
                usuario = (String) entrada.readObject(); // Leer el nombre del usuario
                synchronized (clientesPrivados) {
                    clientesPrivados.put(usuario, salida);
                }

                Object mensaje;
                while ((mensaje = entrada.readObject()) != null) {
                    procesarMensaje(mensaje);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error con el cliente privado: " + e.getMessage());
            } finally {
                synchronized (clientesPrivados) {
                    clientesPrivados.remove(usuario);
                }
            }
        }

        private void procesarMensaje(Object mensajeRecibido) {
            try {
                // Validar que el objeto recibido es un arreglo
                if (mensajeRecibido instanceof Object[]) {
                    Object[] datos = (Object[]) mensajeRecibido;
                    MensajeChat mensaje = (MensajeChat) datos[0];
                    String destinatario = (String) datos[1];

                    // Enviar mensaje al destinatario si está conectado
                    synchronized (clientesPrivados) {
                        ObjectOutputStream salidaDestinatario = clientesPrivados.get(destinatario);
                        if (salidaDestinatario != null) {
                            salidaDestinatario.writeObject(mensaje);
                            salidaDestinatario.flush();
                        } else {
                            System.out.println("El destinatario no está conectado: " + destinatario);
                        }
                    }
                } else {
                    System.err.println("Formato de mensaje inválido: " + mensajeRecibido);
                }
            } catch (Exception e) {
                System.err.println("Error procesando el mensaje: " + e.getMessage());
            }
        }
    }
}
