/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Steam;

import Pantallas_Principales.MenuPrincipal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 *
 * @author royum
 */
public class Juegos_Steam extends JFrame {

    private String nombreUsuario;
    private File carpetaUsuariosGestion;
    private File carpetaUsuario;
    private ArrayList<Juego> juegos;

    public Juegos_Steam(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;

        // Verificar carpeta raíz
        carpetaUsuariosGestion = new File("UsuariosGestion");
        if (!carpetaUsuariosGestion.exists() || !carpetaUsuariosGestion.isDirectory()) {
            JOptionPane.showMessageDialog(null,
                    "La carpeta raiz 'UsuariosGestion' no existe. Por favor, verifica la configuracion.");
            dispose();
            return;
        }

        
        carpetaUsuario = new File(carpetaUsuariosGestion, nombreUsuario);
        if (!carpetaUsuario.exists() || !carpetaUsuario.isDirectory()) {
            JOptionPane.showMessageDialog(null,
                    "El usuario \"" + nombreUsuario + "\" no tiene una carpeta asignada. Por favor, verifica el sistema.");
            dispose();
            return;
        }

        // Cargar juegos
        juegos = cargarJuegos();

        setTitle("Juegos Steam");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel principal para mostrar los juegos
        JPanel panelJuegos = new JPanel();
        panelJuegos.setLayout(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(panelJuegos);

        // Añadir cada juego al panel
        for (Juego juego : juegos) {
            panelJuegos.add(crearPanelJuego(juego));
        }

        // Botón Volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            
            MenuSteam m=new MenuSteam(nombreUsuario,carpetaUsuario);
            m.setVisible(true);
            dispose();
            
        });

        // Añadir componentes al frame
        add(scrollPane, BorderLayout.CENTER);
        add(btnVolver, BorderLayout.SOUTH);

        setVisible(true);
    }

    private ArrayList<Juego> cargarJuegos() {
        File archivoJuegos = new File("juegos.dat");
        ArrayList<Juego> juegos = new ArrayList<>();

        if (archivoJuegos.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoJuegos))) {
                juegos = (ArrayList<Juego>) ois.readObject();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar los juegos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se encontro el archivo de juegos. Se cargará una lista vacía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        return juegos;
    }

    private JPanel crearPanelJuego(Juego juego) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Mostrar imagen del juego
        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            // Convertir byte[] a ImageIcon
            byte[] caratulaBytes = juego.getCaratula();
            if (caratulaBytes != null && caratulaBytes.length > 0) {
                Image img = Toolkit.getDefaultToolkit().createImage(caratulaBytes);
                ImageIcon icon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                lblImagen.setIcon(icon);
            } else {
                lblImagen.setText("Sin Imagen");
            }
        } catch (Exception ex) {
            lblImagen.setText("Error al cargar imagen");
        }

        panel.add(lblImagen, BorderLayout.NORTH);

        // Botón Ver Información
        JButton btnInfo = new JButton("Ver Informacion");
        btnInfo.addActionListener(e -> mostrarInformacion(juego));
        panel.add(btnInfo, BorderLayout.CENTER);

        // Botón Descargar
        JButton btnDescargar = new JButton("Descargar");
        btnDescargar.addActionListener(e -> descargarJuego(juego));
        panel.add(btnDescargar, BorderLayout.SOUTH);

        return panel;
    }

    private void mostrarInformacion(Juego juego) {
        String info = String.format(
                "Título: %s\nGénero: %s\nDesarrollador: %s\nFecha de Lanzamiento: %s\nRuta de Instalación: %s",
                juego.getNombre(), juego.getGenero(), juego.getDesarrollador(), juego.getFechaLanzamiento(), juego.getRutaInstalacion());
        JOptionPane.showMessageDialog(this, info, "Información del Juego", JOptionPane.INFORMATION_MESSAGE);
    }

    private void descargarJuego(Juego juego) {
        if (juego.getRutaInstalacion() == null || juego.getRutaInstalacion().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ruta de instalación no valida para este juego.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File archivoOriginal = new File(juego.getRutaInstalacion());
            if (!archivoOriginal.exists()) {
                JOptionPane.showMessageDialog(this,
                        "El archivo original del juego no existe: " + archivoOriginal.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File carpetaJuegosUsuario = new File(carpetaUsuario, "Juegos");
            if (!carpetaJuegosUsuario.exists()) {
                carpetaJuegosUsuario.mkdirs();
            }

            File archivoDestino = new File(carpetaJuegosUsuario, archivoOriginal.getName());
            Files.copy(archivoOriginal.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            JOptionPane.showMessageDialog(this,
                    "Juego descargado exitosamente en: " + archivoDestino.getAbsolutePath(), "Descarga Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al descargar el juego: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
