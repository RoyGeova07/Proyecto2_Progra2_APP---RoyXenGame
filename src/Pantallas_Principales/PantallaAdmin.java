/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas_Principales;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.tree.TreeNode;

/**
 *
 * @author royum
 */
public class PantallaAdmin extends JFrame {

    private JTree arbol;
    private DefaultTreeModel arbolmodelo;
    private File rutaDirectorio;
    private File directorioActual;
    private JPanel Panel;
    private JTextArea logArea;
    private String nombreUsuario;
    private File archivoUsuario;//para el archivo binario
    private File archivoNuevo;

    public PantallaAdmin(File rootDirectory, String nombreUsuario, File archivoBinario) {
        this.rutaDirectorio = rootDirectory;
        this.nombreUsuario = nombreUsuario;
        this.archivoUsuario=archivoBinario;

        setTitle("Panel Admin de " + nombreUsuario);
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Izquierdo: Arbol de directorios
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 0));
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootDirectory.getName());
        arbolmodelo = new DefaultTreeModel(rootNode);
        arbol = new JTree(arbolmodelo);
        Cargar_Directorio(rootDirectory, rootNode);

        arbol.addTreeSelectionListener(this::en_Selección_De_Arbol);

        JScrollPane treeScrollPane = new JScrollPane(arbol);
        leftPanel.add(treeScrollPane, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // Panel Central: Archivos
        Panel = new JPanel(new GridLayout(0, 4, 10, 10));
        JScrollPane fileScrollPane = new JScrollPane(Panel);
        add(fileScrollPane, BorderLayout.CENTER);

        // Panel Inferior: Log de operaciones
        logArea = new JTextArea(5, 20);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        add(logScrollPane, BorderLayout.SOUTH);

        // Panel Superior: Botones
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = createButton("Agregar Música", "add", e -> Agregarmusica());
        JButton deleteButton = createButton("Eliminar", "delete", e -> borrar());
        JButton renameButton = createButton("Renombrar", "rename", e -> renombrar_Archivo(archivoNuevo,nombreUsuario));
        JButton backButton = createButton("Volver", "back", e -> backToMenu());

        JButton createButton = createButton("Crear", "create", e -> crearFolder_Archivo());


        topPanel.add(addButton);
        topPanel.add(deleteButton);
        topPanel.add(renameButton);
        topPanel.add(backButton);
        topPanel.add(createButton);
        add(topPanel, BorderLayout.NORTH);
    }

    private JButton createButton(String text, String icon, ActionListener action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    private void Cargar_Directorio(File directory, DefaultMutableTreeNode parentNode) {
        if (directory == null || !directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            Arrays.sort(files);
            for (File file : files) {
                DefaultMutableTreeNode NodoSecudario = new DefaultMutableTreeNode(file.getName());
                parentNode.add(NodoSecudario);
                if (file.isDirectory()) {
                    Cargar_Directorio(file, NodoSecudario);
                }
            }
        }
    }

    private void en_Selección_De_Arbol(TreeSelectionEvent e) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
        if (selectedNode == null) {
            return;
        }

        String path = obtenerRutaDesdeNodo(selectedNode);
        directorioActual = new File(rutaDirectorio, path);
        if (directorioActual.exists()&&directorioActual.isDirectory()) {
            mostrar_ArchivosEn_Directorio(directorioActual);
        }
    }

    private String obtenerRutaDesdeNodo(DefaultMutableTreeNode node) {
        StringBuilder path = new StringBuilder();
        TreeNode[]nodes=node.getPath();
        for (int i=1;i<nodes.length; i++) {
            path.append(nodes[i].toString());
            if (i<nodes.length-1) {
                path.append(File.separator);
            }
        }
        return path.toString();
    }

    private void mostrar_ArchivosEn_Directorio(File directory) {
        Panel.removeAll();
        File[]files=directory.listFiles();
        if (files!=null) {
            Arrays.sort(files);
            for (File file : files) {
                JLabel label = new JLabel(file.getName(), JLabel.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                Panel.add(label);
            }
        }
        Panel.revalidate();
        Panel.repaint();
    }

    private void Agregarmusica() {
        if (directorioActual == null || !directorioActual.exists()) {
            log("Directorio no válido seleccionado.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de música", "mp3", "wav", "aac"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            for (File file : fileChooser.getSelectedFiles()) {
                try {
                    File destFile = new File(directorioActual, file.getName());
                    copiar_archivo(file, destFile);
                    log("Archivo agregado: " + file.getName());
                } catch (IOException ex) {
                    log("Error al agregar archivo: " + file.getName());
                }
            }
            mostrar_ArchivosEn_Directorio(directorioActual);
        }
    }

    private void copiar_archivo(File source, File destination) throws IOException {
        try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(destination)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void borrar() {
        if (directorioActual == null || !directorioActual.exists()) {
            log("Directorio no válido seleccionado.");
            return;
        }

        File[] files = directorioActual.listFiles();
        if (files == null || files.length == 0) {
            log("El directorio está vacío. No hay nada que eliminar.");
            return;
        }

        String fileName=JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo o carpeta a eliminar:");
        if (fileName!=null && !fileName.trim().isEmpty()) {
            File file = new File(directorioActual, fileName);

            if (file.exists()) {
                if (borrar_recursivo(file)) {
                    log("Eliminado exitosamente: " + fileName);
                    mostrar_ArchivosEn_Directorio(directorioActual);
                } else {
                    log("No se pudo eliminar: " + fileName);
                }
            } else {
                log("El archivo o carpeta no existe: " + fileName);
            }
        }
    }

    private boolean borrar_recursivo(File file){
        
        if(file.isDirectory()){
            
            File[] files= file.listFiles();
            if(files!=null){
                for (File borra : files) {
                    if(!borrar_recursivo(borra)){
                        return false;
                    }
                }
            }
            
        }
        return file.delete();
        
    }
    
   
    
    private boolean deleteDirectory(File directory){
        
        if(directory.isDirectory()){
            
            File[] archivos=directory.listFiles();
            if(archivos!=null){
                
                for (File archivo : archivos) {
                    deleteDirectory(archivo);
                }
                
            }
            
        }
        return directory.delete();
        
    }
    
  
    
    private void crearFolder_Archivo() {
        if (directorioActual == null || !directorioActual.exists()) {
            log("Directorio no válido seleccionado.");
            return;
        }

        String[] options = {"Archivo", "Carpeta"};
        int choice = JOptionPane.showOptionDialog(this, "¿Qué desea crear?", "Crear",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) { // Crear archivo
            String fileName = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo:");
            if (fileName != null && !fileName.trim().isEmpty()) {
                File newFile = new File(directorioActual, fileName);
                try {
                    if (newFile.createNewFile()) {
                        log("Archivo creado: " + fileName);
                        mostrar_ArchivosEn_Directorio(directorioActual);
                    } else {
                        log("No se pudo crear el archivo. Verifique si ya existe.");
                    }
                } catch (IOException e) {
                    log("Error al crear archivo: " + e.getMessage());
                }
            }
        } else if (choice == 1) { // Crear carpeta
            String folderName = JOptionPane.showInputDialog(this, "Ingrese el nombre de la carpeta:");
            if (folderName != null && !folderName.trim().isEmpty()) {
                File newFolder = new File(directorioActual, folderName);
                if (newFolder.mkdir()) {
                    log("Carpeta creada: " + folderName);
                    mostrar_ArchivosEn_Directorio(directorioActual);
                } else {
                    log("No se pudo crear la carpeta. Verifique si ya existe.");
                }
            }
        }
    }

    private void renombrar_Archivo(File archivoOriginal, String nuevoNombre) {
        
        if(!archivoOriginal.exists()) {
            log("el archivo orginial no existe " + archivoOriginal.getAbsolutePath());
            return;
        }

        File directorioPadre = archivoOriginal.getParentFile();

        File archivoRenombrado = new File(directorioPadre, nuevoNombre);

        
        if (archivoRenombrado.exists()) {
            log("Ya existe un archivo con el nombre: " + nuevoNombre);
            return;
        }

     
        boolean exito = archivoOriginal.renameTo(archivoRenombrado);

        if (exito) {
            log("Archivo renombrado exitosamente: " + archivoRenombrado.getAbsolutePath());
        } else {
            log("No se pudo renombrar el archivo: " + archivoOriginal.getAbsolutePath());
        }
    }

    private void log(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void backToMenu() {
        new MenuPrincipal(nombreUsuario,archivoUsuario).setVisible(true);
        dispose();
    }

}
