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
public class ServidorChat {
    
    private static final int PUERTO = 12345;
    private static Set<ObjectOutputStream> clientes = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Servidor de chat iniciado...");
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());
                new Thread(new ManejadorCliente(cliente)).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static class ManejadorCliente implements Runnable {
        private Socket socket;
        private ObjectOutputStream salida;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {
                salida = new ObjectOutputStream(socket.getOutputStream());
                synchronized (clientes) {
                    clientes.add(salida);
                }

                Object mensaje;
                while ((mensaje = entrada.readObject()) != null) {
                    retransmitirMensaje(mensaje);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error con el cliente: " + e.getMessage());
            } finally {
                synchronized (clientes) {
                    clientes.remove(salida);
                }
            }
        }

        private void retransmitirMensaje(Object mensaje) {
            synchronized (clientes) {
                System.out.println("Retransmitiendo mensaje: " + mensaje.toString());
                for (ObjectOutputStream cliente : clientes) {
                    if (cliente != this.salida) { // Log antes de enviar
                        System.out.println("Enviando a cliente: " + cliente);
                        try {
                            cliente.writeObject(mensaje);
                            cliente.flush();
                        } catch (IOException e) {
                            System.err.println("Error enviando mensaje: " + e.getMessage());
                        }
                    }
                }

            }
        }
    }
}
