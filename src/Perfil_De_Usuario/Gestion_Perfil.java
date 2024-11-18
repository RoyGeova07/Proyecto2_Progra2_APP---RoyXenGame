/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Perfil_De_Usuario;

import Pantallas_Principales.MenuPrincipal;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Arrays;

/**
 *
 * @author royum
 */
public class Gestion_Perfil extends JFrame {

    private JTree arbol;
    private DefaultTreeModel modeloArbol;
    private File carpeta;
    private File DirectorioActual;
    private JPanel panelArchivos;
    private String nombreUsuario;

    public Gestion_Perfil(File directorioUsuario, String nombre) {
        this.carpeta = directorioUsuario;
        this.nombreUsuario = nombre;

        // Configuracion general del frame
        setTitle("APP RoyXen -> Archivos de " + nombreUsuario);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear un panel izquierdo para el arbol de directorios
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(250, 0));
        panelIzquierdo.setBackground(Color.BLACK);

        // Crear el arbol de directorios
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(carpeta.getName());
        modeloArbol = new DefaultTreeModel(rootNode);
        arbol = new JTree(modeloArbol);
        cargarDirectorio(carpeta, rootNode);

        // Listener para cambios en la seleccion del arbol
        arbol.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
            if (selectedNode == null) {
                DirectorioActual = null;
                return;
            }

            String rutaRelativa = getRutaDesdeNodo(selectedNode).trim();
            File directorioSeleccionado = new File(carpeta, rutaRelativa);

            if (directorioSeleccionado.exists() && directorioSeleccionado.isDirectory()) {
                DirectorioActual = directorioSeleccionado;
                mostrarContenidoCarpeta(DirectorioActual);
            } else {
                DirectorioActual = null;
            }
        });

        JScrollPane scrollTree = new JScrollPane(arbol);
        scrollTree.setBackground(Color.BLACK);
        panelIzquierdo.add(scrollTree, BorderLayout.CENTER);
        add(panelIzquierdo, BorderLayout.WEST);

        //aqui se crea un area central para mostrar archivos
        panelArchivos = new JPanel();
        panelArchivos.setLayout(new GridLayout(0, 4, 15, 15));
        panelArchivos.setBackground(Color.DARK_GRAY);
        JScrollPane scrollArchivos = new JScrollPane(panelArchivos);
        add(scrollArchivos, BorderLayout.CENTER);

        // Crear un panel superior para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.setBackground(new Color(45, 45, 45));

        JButton agregarButton = new JButton("Agregar Musica");
        configurarBoton(agregarButton, new Color(120, 180, 180));
        agregarButton.addActionListener(e -> agregarArchivosMusica());
        panelBotones.add(agregarButton);

        JButton eliminarButton = new JButton("Eliminar");
        configurarBoton(eliminarButton, new Color(220, 20, 60));
        eliminarButton.addActionListener(e -> eliminarArchivo());
        panelBotones.add(eliminarButton);

        JButton renombrarButton = new JButton("Renombrar");
        configurarBoton(renombrarButton, new Color(50, 150, 50));
        renombrarButton.addActionListener(e -> renombrarArchivo());
        panelBotones.add(renombrarButton);

        JButton Volver = new JButton("Volver");
        configurarBoton(Volver, new Color(120, 20, 60));
        Volver.addActionListener(e -> {

            MenuPrincipal menu = new MenuPrincipal(nombreUsuario);
            menu.setVisible(true);
            dispose();

        });
        panelBotones.add(Volver);

        add(panelBotones, BorderLayout.NORTH);
    }

    private void configurarBoton(JButton boton, Color color) {
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void cargarDirectorio(File directorio, DefaultMutableTreeNode node) {
        if (directorio == null || !directorio.exists()) {
            return;
        }
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            Arrays.sort(archivos);
            for (File archivo : archivos) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(archivo.getName());
                node.add(childNode);
                if (archivo.isDirectory()) {
                    cargarDirectorio(archivo, childNode);
                }
            }
        }
    }

    private String getRutaDesdeNodo(DefaultMutableTreeNode node) {
        StringBuilder rutaRelativa = new StringBuilder();
        TreeNode[] ruta = node.getPath();
        for (int i = 1; i < ruta.length; i++) {
            rutaRelativa.append(ruta[i].toString());
            if (i < ruta.length - 1) {
                rutaRelativa.append(File.separator);
            }
        }
        return rutaRelativa.toString();
    }

    private void mostrarContenidoCarpeta(File carpeta) {
        DirectorioActual = carpeta;
        panelArchivos.removeAll();
        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            Arrays.sort(archivos);
            for (File archivo : archivos) {
                JLabel label = new JLabel(archivo.getName(), JLabel.CENTER);
                label.setForeground(Color.WHITE);
                panelArchivos.add(label);
            }
        }
        panelArchivos.revalidate();
        panelArchivos.repaint();
    }

    private void renombrarArchivo() {
        if (DirectorioActual == null || !DirectorioActual.exists()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un directorio o archivo válido.");
            return;
        }

        // Solicitar el nombre actual del archivo o carpeta
        String nombreActual = JOptionPane.showInputDialog(this,
                "Ingrese el nombre del archivo o carpeta a renombrar:",
                "Renombrar Archivo/Carpeta",
                JOptionPane.QUESTION_MESSAGE);

        if (nombreActual == null || nombreActual.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre no válido.");
            return;
        }

        File archivoActual = new File(DirectorioActual, nombreActual);

        if (!archivoActual.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo o carpeta no existe.");
            return;
        }

        // Solicitar el nuevo nombre
        String nuevoNombre = JOptionPane.showInputDialog(this,
                "Ingrese el nuevo nombre:",
                "Nuevo Nombre",
                JOptionPane.QUESTION_MESSAGE);

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nuevo nombre no puede estar vacío.");
            return;
        }

        File archivoRenombrado = new File(DirectorioActual, nuevoNombre);

        if (archivoRenombrado.exists()) {
            JOptionPane.showMessageDialog(this, "Ya existe un archivo o carpeta con ese nombre.");
            return;
        }

        // Renombrar el archivo o carpeta
        if (archivoActual.renameTo(archivoRenombrado)) {
            JOptionPane.showMessageDialog(this, "Renombrado exitoso.");
            mostrarContenidoCarpeta(DirectorioActual); // Actualizar la lista de archivos
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo renombrar el archivo o carpeta.");
        }
    }

    private void agregarArchivosMusica() {
        if (DirectorioActual == null || !DirectorioActual.exists()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un directorio valido.");
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File archivo : fileChooser.getSelectedFiles()) {
                if (archivo.getName().matches(".*\\.(mp3|wav|aac)$")) {
                    try {
                        File destino = new File(DirectorioActual, archivo.getName());
                        copiarArchivo(archivo, destino);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, "Error al copiar: " + archivo.getName());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, archivo.getName() + " no es un archivo de musica valido.");
                }
            }
            mostrarContenidoCarpeta(DirectorioActual);
        }
    }

    private void copiarArchivo(File fuente, File destino) throws IOException {
        try (InputStream in = new FileInputStream(fuente); OutputStream out = new FileOutputStream(destino)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void eliminarArchivo() {
        String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a eliminar:");
        if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
            File archivo = new File(DirectorioActual, nombreArchivo);
            if (archivo.exists() && archivo.delete()) {
                JOptionPane.showMessageDialog(this, "Archivo eliminado.");
                mostrarContenidoCarpeta(DirectorioActual);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el archivo.");
            }
        }
    }

}
