/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CentroDelProyecto;

import Base_De_Datos.ManejoUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author royum
 */
public class CrearUsuario extends JFrame {
    

    private JTextField nombreField;
    private JPasswordField passwordField;
    private JCheckBox adminCheckBox;
    private JButton crearUsuario;
    private JButton cancelar;
    private JButton volver;
    private ManejoUsuarios manejoUsuarios;

    public CrearUsuario() {
        manejoUsuarios = new ManejoUsuarios();
        manejoUsuarios.CargarUsuarios();

        setTitle("APP RoyXen -> Crear Usuario");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // titulo
        JLabel tituloLabel = new JLabel("Crear Usuario");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 22));
        tituloLabel.setForeground(new Color(0, 102, 204));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(tituloLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Campo de Nombre
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nombrePanel.setBackground(mainPanel.getBackground());
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(15);
        nombrePanel.add(nombreLabel);
        nombrePanel.add(nombreField);
        mainPanel.add(nombrePanel);

        // Campo de Contraseña
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setBackground(mainPanel.getBackground());
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        // CheckBox de Administrador
        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adminPanel.setBackground(mainPanel.getBackground());
        adminCheckBox = new JCheckBox("¿Es administrador?");
        adminCheckBox.setBackground(adminPanel.getBackground());
        adminPanel.add(adminCheckBox);
        mainPanel.add(adminPanel);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(mainPanel.getBackground());
        crearUsuario = crearBoton("Crear");
        cancelar = crearBoton("Cancelar");
        volver = crearBoton("Volver");
        buttonPanel.add(crearUsuario);
        buttonPanel.add(cancelar);
        buttonPanel.add(volver);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        // Agregar el panel principal
        add(mainPanel);

        // Eventos de Botones
        crearUsuario.addActionListener(e -> {
            String nombre = nombreField.getText();
            String password = new String(passwordField.getPassword());
            boolean esAdmin = adminCheckBox.isSelected();

            if (nombre.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (manejoUsuarios.RegistroUsuario(nombre, password, esAdmin)) {
                    JOptionPane.showMessageDialog(null, "Usuario creado exitosamente:\n"
                            + "Nombre: " + nombre + "\nAdministrador: " + (esAdmin ? "Sí" : "No"));
                    limpiarCampos();
                    new MenuPrincipal(nombre).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "El usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelar.addActionListener(e -> limpiarCampos());

        volver.addActionListener(e -> {
            new MenuInicio().setVisible(true);
            dispose();
        });
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(0, 153, 204));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(120, 40));
        return boton;
    }

    private void limpiarCampos() {
        nombreField.setText("");
        passwordField.setText("");
        adminCheckBox.setSelected(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CrearUsuario().setVisible(true));
    }
}