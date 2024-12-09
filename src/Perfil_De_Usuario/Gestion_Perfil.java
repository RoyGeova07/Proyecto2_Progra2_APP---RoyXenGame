/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Perfil_De_Usuario;

import Base_De_Datos.Usuario;
import Pantallas_Principales.MenuPrincipal;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private File archivoUsuario;//para el archivo binario
    MenuPrincipal menu;
    private Usuario user;

    public Gestion_Perfil(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;

        // Carpeta raiz de gestion
        File carpetaUsuariosGestion = new File("UsuariosGestion");
        if (!carpetaUsuariosGestion.exists() || !carpetaUsuariosGestion.isDirectory()) {
            JOptionPane.showMessageDialog(null,
                    "La carpeta raiz 'UsuariosGestion' no existe. Por favor, verifica la configuracion.");
            dispose();
            return;
        }

        // Carpeta del usuario actual
        carpeta = new File(carpetaUsuariosGestion, nombreUsuario);
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            JOptionPane.showMessageDialog(null,
                    "El usuario \"" + nombreUsuario + "\" no tiene una carpeta asignada. Por favor, verifica el sistema.");
            dispose();
            return;
        }

        // Configuracion del JFrame
        setTitle("APP RoyXen -> Archivos de " + nombreUsuario);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear el arbol de directorios
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(250, 0));
        panelIzquierdo.setBackground(Color.BLACK);

        DefaultMutableTreeNode RaizDeNodo = new DefaultMutableTreeNode(carpeta.getName());
        modeloArbol = new DefaultTreeModel(RaizDeNodo);
        arbol = new JTree(modeloArbol);
        cargarDirectorio(carpeta, RaizDeNodo);

        arbol.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode SeleccionarNodo = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
            if (SeleccionarNodo == null) {
                DirectorioActual = null;
                return;
            }

            String rutaRelativa = getRutaDesdeNodo(SeleccionarNodo).trim();
            File directorioSeleccionado = new File(carpeta, rutaRelativa);

            if (directorioSeleccionado.exists() && directorioSeleccionado.isDirectory()) {
                DirectorioActual = directorioSeleccionado;
                mostrarContenidoCarpeta(DirectorioActual);
            } else {
                DirectorioActual = null;
            }
        });

        JScrollPane Scroll_Arbol = new JScrollPane(arbol);
        Scroll_Arbol.setBackground(Color.BLACK);
        panelIzquierdo.add(Scroll_Arbol, BorderLayout.CENTER);
        add(panelIzquierdo, BorderLayout.WEST);

        // Panel de archivos
        panelArchivos = new JPanel();
        panelArchivos.setLayout(new GridLayout(0, 4, 15, 15));
        panelArchivos.setBackground(Color.DARK_GRAY);
        JScrollPane scrollArchivos = new JScrollPane(panelArchivos);
        add(scrollArchivos, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.setBackground(new Color(45, 45, 45));

        JButton agregarButton = new JButton("Agregar Musicas");
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

        JButton crearButton = new JButton("Crear");
        configurarBoton(crearButton, new Color(75, 75, 255));
        crearButton.addActionListener(e -> crearArchivoOCarpeta());
        panelBotones.add(crearButton);

        JButton Volver = new JButton("Volver");
        configurarBoton(Volver, new Color(120, 20, 60));
        Volver.addActionListener(e -> {

            try {
                menu = new MenuPrincipal(nombreUsuario, archivoUsuario);
            } catch (IOException ex) {
                Logger.getLogger(Gestion_Perfil.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            Arrays.sort(archivos);//el sort me ayuda a ordenar los objetos en el arreglo por su nombre
            for (File archivo : archivos) {
                DefaultMutableTreeNode Nodo_Secundario = new DefaultMutableTreeNode(archivo.getName());
                node.add(Nodo_Secundario);
                if (archivo.isDirectory()) {
                    cargarDirectorio(archivo, Nodo_Secundario);
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
            JOptionPane.showMessageDialog(this, "Debe seleccionar un directorio o archivo valido.");
            return;
        }

        // Solicitar el nombre actual del archivo o carpeta
        String nombreActual = JOptionPane.showInputDialog(this,
                "Ingrese el nombre del archivo o carpeta a renombrar:",
                "Renombrar Archivo/Carpeta",
                JOptionPane.QUESTION_MESSAGE);

        if (nombreActual == null || nombreActual.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre no valido.");
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
            mostrarContenidoCarpeta(DirectorioActual);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo renombrar el archivo o carpeta.");
        }
    }

    private void agregarArchivosMusica() {
        if (DirectorioActual == null || !DirectorioActual.exists()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un directorio valido.");
            return;
        }
        JFileChooser SelectorArchivos = new JFileChooser();
        SelectorArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);
        SelectorArchivos.setMultiSelectionEnabled(true);

        if (SelectorArchivos.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File archivo : SelectorArchivos.getSelectedFiles()) {
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
            int bytes_leer;
            while ((bytes_leer = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytes_leer);
            }
        }
    }

    private void eliminarArchivo() {

        String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a eliminar:");
        if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
            File archivo = new File(DirectorioActual, nombreArchivo);
            if (archivo.exists() && archivo.delete()) {
                JOptionPane.showMessageDialog(this, "Archivo " + nombreArchivo + " eliminado.");

                File CarpetaUsuario = new File("UsuariosGestion/" + nombreUsuario + "/Juegos");
                File juegoenCarpeta = new File(CarpetaUsuario, nombreArchivo);

                if (juegoenCarpeta.exists()) {
                    if (juegoenCarpeta.delete()) {
                        JOptionPane.showMessageDialog(this, "El juego " + nombreArchivo + " también fue eliminado de la carpeta de Juegos.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo eliminar el juego " + nombreArchivo + " de la carpeta de Juegos.");
                    }
                }

                dispose();
                new Gestion_Perfil(nombreUsuario).setVisible(true);
                mostrarContenidoCarpeta(DirectorioActual);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el archivo.");
            }
        }
    }

    private void crearArchivoOCarpeta() {

        if (DirectorioActual == null || !DirectorioActual.exists()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un directorio valido.");
            return;
        }

        String[] opciones = {"Archivo", "Carpeta"};
        int seleccion = JOptionPane.showOptionDialog(this,
                "¿Que desea crear?",
                "Crear",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == 0) { // Crear archivo
            String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo:");
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                File nuevoArchivo = new File(DirectorioActual, nombreArchivo);
                try {
                    if (nuevoArchivo.createNewFile()) {
                        JOptionPane.showMessageDialog(this, "Archivo creado exitosamente.");
                        dispose();
                    new Gestion_Perfil(nombreUsuario).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo crear el archivo. Verifique si ya existe.");
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al crear archivo: " + e.getMessage());
                }
            }
        } else if (seleccion == 1) { // Crear carpeta
            String nombreCarpeta = JOptionPane.showInputDialog(this, "Ingrese el nombre de la carpeta:");
            if (nombreCarpeta != null && !nombreCarpeta.trim().isEmpty()) {
                File nuevaCarpeta = new File(DirectorioActual, nombreCarpeta);
                if (nuevaCarpeta.mkdir()) {
                    JOptionPane.showMessageDialog(this, "Carpeta creada exitosamente.");
                    dispose();
                    new Gestion_Perfil(nombreUsuario).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo crear la carpeta. Verifique si ya existe.");
                }
            }
        }

        // Actualizar la vista del contenido
        mostrarContenidoCarpeta(DirectorioActual);
    }
}
