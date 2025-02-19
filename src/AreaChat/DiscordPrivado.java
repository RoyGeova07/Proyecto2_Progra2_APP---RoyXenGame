/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AreaChat;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.TitledBorder;

/**
 *
 * @author royum
 */
public class DiscordPrivado extends JFrame {

    private String usuarioEnSesion;
    private String otroUsuario;
    private ChatsPrivados gestorChats;
    private JPanel panelMensajes;
    private JTextField campoMensaje;
    private JScrollPane scrollPane;
    private ClienteChatPrivado clientePrivado;

    public DiscordPrivado(String usuarioEnSesion) throws IOException {
        this.usuarioEnSesion = usuarioEnSesion;
        this.gestorChats = new ChatsPrivados();

        ConfigurarPanel();
        
        clientePrivado = new ClienteChatPrivado("localhost", 54321, usuarioEnSesion);
        clientePrivado.escucharMensajes(this::nuevoMensaje);

    }
    
    private void ConfigurarPanel(){
        setLayout(new BorderLayout());
        
        
        // Panel de mensajes
        panelMensajes = new JPanel();
        panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));
        scrollPane   = new JScrollPane(panelMensajes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chat con " + otroUsuario));
        add(scrollPane, BorderLayout.CENTER);

        // Campo para enviar mensajes
        JPanel panelInferior = new JPanel(new BorderLayout());
        campoMensaje = new JTextField();
        JButton botonEnviar = new JButton("Enviar");
        panelInferior.add(campoMensaje, BorderLayout.CENTER);
        panelInferior.add(botonEnviar, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);

       
        botonEnviar.addActionListener(e -> {
            try {
                enviarMensaje();
            } catch (IOException ex) {
                Logger.getLogger(DiscordPrivado.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void actualizarConversacion(String nuevoUsuario) throws IOException {
        this.otroUsuario = nuevoUsuario;

        // Limpiar el panel de mensajes
        panelMensajes.removeAll();

        // Recargar los mensajes de la nueva conversacion
            CargarMensajes();

    
        TitledBorder border = (TitledBorder) scrollPane.getBorder();
        border.setTitle("Chat con " + otroUsuario);
        repaint();
        revalidate();
    }

    private void CargarMensajes() throws IOException {
        ArrayList<String> mensajes = gestorChats.CargarMensajes(usuarioEnSesion, otroUsuario);
        for (String mensaje : mensajes) {
            JLabel etiquetaMensaje = new JLabel(mensaje);
            etiquetaMensaje.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            panelMensajes.add(etiquetaMensaje);
        }
        panelMensajes.revalidate();
        panelMensajes.repaint();
    }
    
      private void nuevoMensaje(MensajeChat mensaje) {
        if (mensaje.getRemitente().equals(otroUsuario)) {
            SwingUtilities.invokeLater(() -> {
                JLabel etiquetaMensaje = new JLabel(mensaje.toString());
                etiquetaMensaje.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                panelMensajes.add(etiquetaMensaje);
                panelMensajes.revalidate();
                panelMensajes.repaint();
            });
        }
    }

    private void enviarMensaje() throws IOException {
        String texto = campoMensaje.getText().trim();
        if (!texto.isEmpty()) {
            // Crear el mensaje solo con el remitente y el texto
            MensajeChat mensaje = new MensajeChat(texto, usuarioEnSesion);

            // Enviar el mensaje al servidor junto con el destinatario
            clientePrivado.enviarMensaje(mensaje, otroUsuario);

            // Mostrar el mensaje en la interfaz localmente
            JLabel etiquetaMensaje = new JLabel(mensaje.toString());
            panelMensajes.add(etiquetaMensaje);
            campoMensaje.setText("");
            panelMensajes.revalidate();
            panelMensajes.repaint();
        }
    }
}
