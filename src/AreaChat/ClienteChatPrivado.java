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
public class ClienteChatPrivado {
    
    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;

    public ClienteChatPrivado(String host, int puerto, String usuario) throws IOException {
        socket = new Socket(host, puerto);
        salida = new ObjectOutputStream(socket.getOutputStream());
        entrada = new ObjectInputStream(socket.getInputStream());
        salida.writeObject(usuario); // Enviar nombre del usuario al servidor
        salida.flush();
    }

    public void enviarMensaje(MensajeChat mensaje, String destinatario) throws IOException {
        // Enviar el mensaje y el destinatario al servidor
        salida.writeObject(new Object[]{mensaje, destinatario});
        salida.flush();
    }

    public void escucharMensajes(ChatListener listener) {
        new Thread(() -> {
            try {
                while (true) {
                    MensajeChat mensaje = (MensajeChat) entrada.readObject();
                    listener.nuevoMensaje(mensaje);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Conexión cerrada: " + e.getMessage());
            }
        }).start();
    }

    public interface ChatListener {
        void nuevoMensaje(MensajeChat mensaje);
    }

    public void cerrar() throws IOException {
        socket.close();
    }
}