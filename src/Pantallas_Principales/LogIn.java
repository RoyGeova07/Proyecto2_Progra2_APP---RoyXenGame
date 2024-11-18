/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas_Principales;

import Base_De_Datos.ManejoUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public LogIn() {
        manejoUsuarios = new ManejoUsuarios();
        manejoUsuarios.CargarUsuarios();

        setTitle("APP RoyXen -> Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 250));

        // titulo
        JLabel tituloLabel = new JLabel("Iniciar Sesión");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 22));
        tituloLabel.setForeground(new Color(102, 102, 255));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(tituloLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Campo de Usuario
        JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usuarioPanel.setBackground(mainPanel.getBackground());
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioField = new JTextField(15);
        usuarioPanel.add(usuarioLabel);
        usuarioPanel.add(usuarioField);
        mainPanel.add(usuarioPanel);

        // Campo de Contraseña
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setBackground(mainPanel.getBackground());
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(mainPanel.getBackground());
        iniciarSesionButton = crearBoton("Iniciar Sesión");
        cancelarButton = crearBoton("Cancelar");
        volverButton = crearBoton("Volver");
        buttonPanel.add(iniciarSesionButton);
        buttonPanel.add(cancelarButton);
        buttonPanel.add(volverButton);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        add(mainPanel);

        // Eventos de Botones
        iniciarSesionButton.addActionListener(e -> {
            String usuario = usuarioField.getText();
            String password = new String(passwordField.getPassword());

            if (usuario.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (manejoUsuarios.ValidarCredenciales(usuario, password)) {
                    JOptionPane.showMessageDialog(null, "Bienvenido, " + usuario + "!");
                    new MenuPrincipal(usuario).setVisible(true);
                    dispose();
                } else {
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

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(153, 102, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(120, 40));
        return boton;
    }

    private void limpiarCampos() {
        usuarioField.setText("");
        passwordField.setText("");
    }

}