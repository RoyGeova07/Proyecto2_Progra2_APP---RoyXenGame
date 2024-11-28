/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Steam;

import Base_De_Datos.ManejoUsuarios;
import Pantallas_Principales.MenuPrincipal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author royum
 */
public class Juegos_Steam extends JFrame {

    private String nombreUsuario;
    private File carpetaUsuariosGestion;
    private File carpetaUsuario;
    private boolean esAdmin;
    private ArrayList<Juego> juegos;

    public Juegos_Steam(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;

        
        this.esAdmin = new ManejoUsuarios().esAdmin(nombreUsuario);

     
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

        
        juegos = cargarJuegos();

        setTitle("Juegos Steam");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        JPanel panelJuegos = new JPanel();
        panelJuegos.setLayout(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(panelJuegos);

        // Añadir cada juego al panel
        for (Juego juego : juegos) {
            panelJuegos.add(crearPanelJuego(juego));
        }

        
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            MenuSteam m = new MenuSteam(nombreUsuario, carpetaUsuario);
            m.setVisible(true);
            dispose();
        });

        
        if (esAdmin) {
            JButton btnEliminar = new JButton("Eliminar Juego");
            btnEliminar.addActionListener(e -> {
                try {
                    eliminarJuego();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar el juego: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            add(btnEliminar, BorderLayout.NORTH);
        }

        
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
                    "No se encontro el archivo de juegos. Se cargara una lista vacia.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        return juegos;
    }

    private JPanel crearPanelJuego(Juego juego) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        
        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);

        try {
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

        JButton btnInfo = new JButton("Ver Informacion");
        btnInfo.addActionListener(e -> mostrarInformacion(juego));
        panel.add(btnInfo, BorderLayout.CENTER);

        JButton btnDescargar = new JButton("Descargar");
        btnDescargar.addActionListener(e -> descargarJuego(juego));
        panel.add(btnDescargar, BorderLayout.SOUTH);

        return panel;
    }

    private void mostrarInformacion(Juego juego) {
        String info = String.format(
                "Titulo: %s\nGenero: %s\nDesarrollador: %s\nFecha de Lanzamiento: %s\nRuta de Instalacion: %s",
                juego.getNombre(), juego.getGenero(), juego.getDesarrollador(), juego.getFechaLanzamiento(), juego.getRutaInstalacion());
        JOptionPane.showMessageDialog(this, info, "Informacion del Juego", JOptionPane.INFORMATION_MESSAGE);
    }

   private void descargarJuego(Juego juego) {
    if (juego.getRutaInstalacion() == null || juego.getRutaInstalacion().isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Ruta de instalación no valida para este juego.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Obtener ruta base del proyecto
        String Rutabase = System.getProperty("user.dir");
        System.out.println("Ruta base: " + Rutabase);

        // Archivo original
        File archivoOriginal = new File(Rutabase + File.separator + "juegos.dat");
        System.out.println("Ruta archivo original: " + archivoOriginal.getAbsolutePath());
        if (!archivoOriginal.exists()) {
            JOptionPane.showMessageDialog(this,
                    "El archivo del juego no existe en: " + archivoOriginal.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Carpeta destino
        File carpetaDestino = new File(Rutabase + File.separator + "UsuariosGestion" + File.separator +
                nombreUsuario + File.separator + juego.getRutaInstalacion());
        System.out.println("Ruta carpeta destino: " + carpetaDestino.getAbsolutePath());
        if (!carpetaDestino.exists()) {
            carpetaDestino.mkdirs(); // Crear la carpeta si no existe
        }

        // Archivo destino 
        File archivoDestino = new File(carpetaDestino, juego.getNombre() + ".dat");
        System.out.println("Archivo destino: " + archivoDestino.getAbsolutePath());

        // Copiar archivo
        Files.copy(archivoOriginal.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
        JOptionPane.showMessageDialog(this,
                "Juego descargado exitosamente en: " + archivoDestino.getAbsolutePath(), "Descarga Exitosa", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        ex.printStackTrace(); 
        JOptionPane.showMessageDialog(this,
                "Error al descargar el juego: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void eliminarJuego() throws IOException {
        String nombreJuego = JOptionPane.showInputDialog(this, "Ingrese el nombre del juego a eliminar:");
        if (nombreJuego == null || nombreJuego.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre valido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean eliminado = juegos.removeIf(juego -> juego.getNombre().equalsIgnoreCase(nombreJuego));
        if (eliminado) {
            guardarJuegos();
            JOptionPane.showMessageDialog(this, "El juego ha sido eliminado exitosamente.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new Juegos_Steam(nombreUsuario).setVisible(true); // Recargar la ventana
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro ningun juego con el nombre " + nombreJuego + ".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarJuegos() throws IOException {
        File archivoJuegos = new File("juegos.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoJuegos))) {
            oos.writeObject(juegos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar los juegos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}