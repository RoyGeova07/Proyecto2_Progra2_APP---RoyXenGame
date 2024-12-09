/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas_Principales;

import Base_De_Datos.ManejoUsuarios;
import Base_De_Datos.Usuario;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author royum
 */
public class LogIn extends JFrame {

    private JTextField usuarioField;
    private JPasswordField passwordField;
    private JButton iniciarSesionButton;
    private JButton cancelarButton;
    private JButton volverButton;
    private ManejoUsuarios manejoUsuarios;
    private JLabel fondo;
    private File archivoUsuario; // para el archivo binario
    private Usuario user;

    public LogIn() {
        manejoUsuarios = new ManejoUsuarios();
        manejoUsuarios.CargarUsuarios();
        
        setTitle("APP RoyXen -> Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        fondo = new JLabel();
        fondo.setLayout(new GridBagLayout()); // Permite centrar los botones
        cargarFondo("/img_menuprin/fondo4.gif");
        setContentPane(fondo); // Establece el fondo como panel principal

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false); // Para que sea transparente y se vea el fondo
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel tituloLabel = new JLabel("Iniciar Sesion");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 25));
        tituloLabel.setForeground(new Color(102, 102, 255));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(tituloLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Campo de Usuario
        JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usuarioPanel.setOpaque(false); // Transparente
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 17));
        usuarioLabel.setForeground(Color.LIGHT_GRAY);
        usuarioField = new JTextField(15);
        usuarioField.setFont(new Font("Arial", Font.PLAIN, 17));
        usuarioPanel.add(usuarioLabel);
        usuarioPanel.add(usuarioField);
        mainPanel.add(usuarioPanel);

        // Campo de Contraseña
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setOpaque(false); // Transparente
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 17));
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 17));
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        // Botones
        mainPanel.add(Box.createVerticalStrut(20)); // Espaciado
        mainPanel.add(crearPanelBotones()); // Agrega el panel de botones

        fondo.add(mainPanel); // Agrega el panel principal sobre el fondo

        // Eventos de Botones
        iniciarSesionButton.addActionListener(e -> {
            String usuario = usuarioField.getText();
            String password = new String(passwordField.getPassword());

            if (usuario.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (manejoUsuarios.ValidarCredenciales(usuario, password)) {
                    
                    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/img_menuprin/confirmacion1.png"));
                    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                    ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
                    
                    JOptionPane.showMessageDialog(null, "Bienvenido, usuario " + usuario + "!", "Exito", JOptionPane.INFORMATION_MESSAGE, iconoEscalado);

                    try {
                        new MenuPrincipal(usuario, archivoUsuario).setVisible(true);
                    } catch (IOException ex) {
                        Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelarButton.addActionListener(e -> limpiarCampos());

        volverButton.addActionListener(e -> {
            new MenuInicio().setVisible(true);
            dispose();
        });
    }

    private JPanel crearPanelBotones() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 1 fila, 3 columnas
        buttonPanel.setOpaque(false); // Transparente

        iniciarSesionButton = crearBoton("Iniciar Sesion");
        cancelarButton = crearBoton("Vaciar");
        volverButton = crearBoton("Volver");

        iniciarSesionButton.setBackground(Color.BLUE);
        cancelarButton.setBackground(Color.BLUE);
        volverButton.setBackground(Color.BLUE);
        // Agregamos los botones al panel
        buttonPanel.add(iniciarSesionButton);
        buttonPanel.add(cancelarButton);
        buttonPanel.add(volverButton);

        return buttonPanel;
    }

    private JButton crearBoton(String texto){
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Consolas", Font.BOLD, 17)); // Tamaño de fuente más pequeño
        boton.setPreferredSize(new Dimension(130, 40)); // Tamaño reducido
        boton.setBackground(Color.BLUE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2, true));

        // Efecto de hover (cambiar color al pasar el mouse)
        boton.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){
                boton.setContentAreaFilled(true);
                boton.setBackground(new Color(0, 0, 0, 50)); // Negro con 50/255 de transparencia.
            }

            public void mouseExited(java.awt.event.MouseEvent evt){
                boton.setBackground(Color.BLUE);
            }
        });
        return boton;
    }

    private void cargarFondo(String ruta) {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource(ruta));
            Image img = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT); // Escala el GIF
            fondo.setIcon(new ImageIcon(img));
            fondo.setHorizontalAlignment(SwingConstants.CENTER); // Centra el GIF
            fondo.setVerticalAlignment(SwingConstants.CENTER);
        }catch(Exception e){
            System.out.println("No se pudo cargar el fondo: " + ruta);
        }
    }

    private void limpiarCampos() {
        usuarioField.setText("");
        passwordField.setText("");
    }
}
