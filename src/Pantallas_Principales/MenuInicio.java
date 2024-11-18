/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas_Principales;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 *
 * @author royum
 */
public class MenuInicio extends JFrame {
    
    private JPanel panelPrincipal;
    private JLabel fondo;
    private JButton botonCrearUsuario;
    private JButton botonIniciarSesion;
    private JButton botonCerrarAplicacion;

    public MenuInicio() {
        configurarVentana();
        configurarComponentes();
    }

    private void configurarVentana() {
        setTitle("APP RoyXen -> Menu Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Tamaño predeterminado de la ventana
        setLocationRelativeTo(null); // Centrado en pantalla
        setResizable(false); // Evita que se redimensione
    }

    private void configurarComponentes() {
        // Panel principal
        panelPrincipal = new JPanel(new BorderLayout());
        setContentPane(panelPrincipal);

        // Fondo de pantalla
        fondo = new JLabel();
        fondo.setLayout(new GridBagLayout()); // Permite centrar los botones
        cargarFondo("/img_menuprin/im.jpg"); 
        panelPrincipal.add(fondo, BorderLayout.CENTER);

        // Crear los botones
        botonCrearUsuario = crearBoton("Crear Usuario");
        botonIniciarSesion = crearBoton("Iniciar Sesion");
        botonCerrarAplicacion = crearBoton("Cerrar Aplicación");

        // Añadir los botones al fondo
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false); // Transparente para que se vea el fondo
        panelBotones.setLayout(new GridLayout(3, 1, 10, 10)); // Botones en columna con separacion

        panelBotones.add(botonCrearUsuario);
        panelBotones.add(botonIniciarSesion);
        panelBotones.add(botonCerrarAplicacion);

        fondo.add(panelBotones); // Añadimos al centro del fondo
        
        botonCrearUsuario.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e) {
                 
                 CrearUsuario crear=new CrearUsuario();
                 crear.setVisible(true);
                 dispose();
                 
             }
            
        });
        
        botonIniciarSesion.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e) {
                 
                 LogIn entrar=new LogIn();
                 entrar.setVisible(true);
                 dispose();
             }
            
        });
        
        botonCerrarAplicacion.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e) {
                 
                 System.exit(0);
                 
             }
            
        });
        
    }

    private void cargarFondo(String ruta) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(ruta));
            Image img = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            fondo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el fondo: " + ruta);
        }
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Consolas", Font.BOLD, 16));
        boton.setPreferredSize(new Dimension(200, 50));
        boton.setBackground(new Color(45, 137, 239));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2, true));

        // Efecto de hover (cambiar color al pasar el mouse)
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0, 122, 204));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(45, 137, 239));
            }
        });
        return boton;
    }

   
}