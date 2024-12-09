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
    private MenuPrincipal menuPrincipal;

    public PantallaAdmin(File rootDirectory,MenuPrincipal menuPrincipal, File archivoBinario) {
        this.rutaDirectorio = rootDirectory;
        this.archivoUsuario=archivoBinario;
        this.menuPrincipal=menuPrincipal;

        setTitle("Panel Admin ");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Izquierdo: Árbol de directorios
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 0));
        DefaultMutableTreeNode nodoRaiz = new DefaultMutableTreeNode(rootDirectory.getName());
        arbolmodelo = new DefaultTreeModel(nodoRaiz);
        arbol = new JTree(arbolmodelo);
        Cargar_Directorio(rootDirectory, nodoRaiz);

        JScrollPane treeScrollPane = new JScrollPane(arbol);
        leftPanel.add(treeScrollPane, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // Panel Central: Archivos
        Panel = new JPanel(new GridLayout(0, 4, 10, 10));
        JScrollPane fileScrollPane = new JScrollPane(Panel);
        add(fileScrollPane, BorderLayout.CENTER);

        // Panel Superior: Solo botón Volver
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = createButton("Volver", "back", e -> backToMenu());
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
    }

    private JButton createButton(String text, String icon, ActionListener action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    private void Cargar_Directorio(File directory, DefaultMutableTreeNode NodoPadre) {
        if (directory == null || !directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            Arrays.sort(files);
            for (File file : files) {
                DefaultMutableTreeNode NodoSecundario = new DefaultMutableTreeNode(file.getName());
                NodoPadre.add(NodoSecundario);
                if (file.isDirectory()) {
                    Cargar_Directorio(file, NodoSecundario);
                }
            }
        }
    }

    private void backToMenu() {
        menuPrincipal.setVisible(true);
        dispose();
    }
}