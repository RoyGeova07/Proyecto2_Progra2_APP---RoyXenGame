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
import java.io.File;

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
    private File archivoUsuario; // Para el archivo binario
    private JLabel fondo;

    public CrearUsuario() {
        manejoUsuarios = new ManejoUsuarios();
        manejoUsuarios.CargarUsuarios();

        setTitle("APP RoyXen -> Crear Usuario");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configurar fondo
        fondo = new JLabel();
        fondo.setLayout(new GridBagLayout()); // Para centrar elementos sobre el fondo
        cargarFondo("/img_menuprin/fondo4.gif");
        setContentPane(fondo); // Establecer el fondo como panel principal

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false); // Transparente para que el fondo sea visible
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel tituloLabel = new JLabel("Crear Usuario");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 25));
        tituloLabel.setForeground(Color.LIGHT_GRAY);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(tituloLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Campo de Nombre
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nombrePanel.setOpaque(false); // Transparente
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 17));
        nombreLabel.setForeground(Color.LIGHT_GRAY);
        nombreField = new JTextField(15);
        nombreField.setFont(new Font("Arial", Font.PLAIN, 17));
        nombrePanel.add(nombreLabel);
        nombrePanel.add(nombreField);
        mainPanel.add(nombrePanel);

        // Campo de Contraseña
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setOpaque(false); // Transparente
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 17));
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 17));
        passwordField.setForeground(Color.BLACK);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        // CheckBox de Administrador
        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adminPanel.setOpaque(false); // Transparente
        adminCheckBox = new JCheckBox("¿Es administrador?");
        adminCheckBox.setFont(new Font("Arial", Font.BOLD, 17));
        adminCheckBox.setForeground(Color.LIGHT_GRAY);
        adminCheckBox.setOpaque(false); // Transparente
        adminPanel.add(adminCheckBox);
        mainPanel.add(adminPanel);

        // Botones
        mainPanel.add(Box.createVerticalStrut(20)); // Espaciado
        mainPanel.add(crearPanelBotones()); // Agregar panel de botones

        fondo.add(mainPanel); // Agregar el panel principal al fondo

        // Eventos de botones
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
                    new MenuPrincipal(nombre, archivoUsuario).setVisible(true);
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

    private JPanel crearPanelBotones() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 1 fila, 3 columnas
        buttonPanel.setOpaque(false); // Transparente

        crearUsuario = crearBoton("Crear");
        cancelar = crearBoton("Cancelar");
        volver = crearBoton("Volver");

        // Agregar botones al panel
        buttonPanel.add(crearUsuario);
        buttonPanel.add(cancelar);
        buttonPanel.add(volver);

        return buttonPanel;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Consolas", Font.BOLD, 14)); // Tamaño reducido
        boton.setPreferredSize(new Dimension(130, 40)); // Tamaño ajustado
        boton.setBackground(Color.BLUE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2, true));

        // Efecto de hover (cambiar color al pasar el mouse)
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(true);
                boton.setBackground(new Color(0, 0, 0, 50)); // Negro con transparencia
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.BLUE);
            }
        });
        return boton;
    }

    private void cargarFondo(String ruta) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(ruta));
            Image img = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT); // Escala el GIF
            fondo.setIcon(new ImageIcon(img));
            fondo.setHorizontalAlignment(SwingConstants.CENTER); // Centra el GIF
            fondo.setVerticalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el fondo: " + ruta);
        }
    }

    private void limpiarCampos() {
        nombreField.setText("");
        passwordField.setText("");
        adminCheckBox.setSelected(false);
    }
}