/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas_Principales;

import AreaChat.Discord;
import Base_De_Datos.ManejoUsuarios;
import Base_De_Datos.Usuario;
import Perfil_De_Usuario.Gestion_Perfil;
import Reproductor.Musica;
import Reproductor.VentanaPrincipal;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author royum
 */
public class MenuPrincipal extends JFrame {

    private JPanel panelPrincipal;
    private JFrame reproductorFrame; // Mantener referencia al reproductor
    private JLabel fondo;
    private JPanel panelBotones;
    private JButton Juegos;
    private JButton Musicas;
    private JButton Discord;
    private JButton Perfil;
    private JButton Cerrar_Sesion;
    private JButton Administracion;
    private ManejoUsuarios manejoUsuarios;
    private static String nombreUsuario;
    private File archivoUsuario;//para el archivo binario
    private Usuario usu;

    public MenuPrincipal(String nombre,File archivoUsuarios) {
        this.nombreUsuario = nombre;
        this.archivoUsuario=archivoUsuarios;
        GUI();
        manejoUsuarios = new ManejoUsuarios();
    }

    private void GUI() {
        setTitle("APP RoyXen -> Cuenta de " + nombreUsuario);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ventana maximizada
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Crear un JLayeredPane para manejar fondo y botones
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // Fondo personalizado
        fondo = new JLabel();
        fondo.setBounds(0, 0, getWidth(), getHeight()); // Tamaño inicial del fondo
        layeredPane.add(fondo, Integer.valueOf(0)); // Agregar el fondo en la capa más baja

        // Crear panel de botones
        panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false); // Hacer el panel transparente
        panelBotones.setBounds(getWidth() / 4, getHeight() / 3, getWidth() / 2, getHeight() / 3);
        layeredPane.add(panelBotones, Integer.valueOf(1)); // Agregar los botones en una capa superior

        // Crear botones con imagenes
        Juegos = crearBoton("Juegos", "/img_menuprin/juego.png");
        Musicas = crearBoton("Reproductor", "/img_menuprin/musica1.png");
        Discord = crearBoton("Discord", "/img_menuprin/discord1.png");
        Perfil = crearBoton("Mi Perfil", "/img_menuprin/perfil.jpg");
        Cerrar_Sesion = crearBoton("Cerrar Sesion", "/img_menuprin/cerrar_sesion.png");
        Administracion = crearBoton("Admin", "/img_menuprin/administracion1.png");

        // Añadir botones al panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre botones
        //aqui primera fila
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBotones.add(Juegos, gbc);

        gbc.gridx = 1;
        panelBotones.add(Musicas, gbc);

        gbc.gridx = 2;
        panelBotones.add(Discord, gbc);

        //aqui segunda fila
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelBotones.add(Perfil, gbc);

        gbc.gridx = 1;
        panelBotones.add(Cerrar_Sesion, gbc);

        gbc.gridx = 2;
        panelBotones.add(Administracion, gbc);

        // Redimensionar el fondo y los componentes al cambiar tamaño de la ventana
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                fondo.setBounds(0, 0, getWidth(), getHeight());
                cargarFondo("/img_menuprin/h.gif");
                panelBotones.setBounds(getWidth() / 4, getHeight() / 3, getWidth() / 2, getHeight() / 3);
            }
        });

        Juegos.addActionListener(e -> {

            //AGREGAR INSTANCIA JUEGO
            
        });

        Musicas.addActionListener(e -> cargarReproductorMusica(this));

        Discord.addActionListener(e -> {

            try {
                Discord dis=new Discord(nombreUsuario);
                dis.setVisible(true);
                dispose();
            } catch (IOException ex) {
                Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Perfil.addActionListener(e -> {

            if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
                // Construimos la ruta en base al directorio actual y al nombre de usuario
                File carpetaUsuario = new File(System.getProperty("user.dir") + File.separator + "UsuariosGestion"+ File.separator+ nombreUsuario);
                if (carpetaUsuario.exists()) {
                    // Si la carpeta existe, abrimos el perfil
                    Gestion_Perfil navegador = new Gestion_Perfil(nombreUsuario);
                    navegador.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "El directorio del usuario no existe: " + carpetaUsuario.getAbsolutePath());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error: El nombre de usuario es nulo o vacio.");
            }

        });

        Administracion.addActionListener(e -> {
            
            String usuarioEnSesion= nombreUsuario;
            
            if(!manejoUsuarios.esAdmin(usuarioEnSesion)){
                JOptionPane.showMessageDialog(null, "No eres admin Acceso denegado!","Informacion",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String UsuarioIngresado = JOptionPane.showInputDialog(null,
                    "Ingrese el nombre del usuario para ingresar a sus carpetas",
                    "Verificación Usuario",
                    JOptionPane.PLAIN_MESSAGE);

            if (UsuarioIngresado != null && !UsuarioIngresado.trim().isEmpty()) {

                Usuario usuario = manejoUsuarios.ObtenerUsuario(UsuarioIngresado);
                if (usuario != null) {

                    JOptionPane.showMessageDialog(null,
                            "Bienvenido a las carpetas de " + UsuarioIngresado + "!");

                    File DirectorioAdmin = new File(System.getProperty("user.dir")
                            + File.separator + "UsuariosGestion"
                            + File.separator + UsuarioIngresado);

                    PantallaAdmin adminPanel = new PantallaAdmin(DirectorioAdmin, this, archivoUsuario);
                    adminPanel.setVisible(true);
                    this.setVisible(false);

                } else {
                    // Mensaje si el usuario no existe
                    JOptionPane.showMessageDialog(null,
                            "El usuario ingresado no existe.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "No ingresaste ningun nombre de usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        Cerrar_Sesion.addActionListener(e -> {

            MenuInicio inicio = new MenuInicio();
            inicio.setVisible(true);
            dispose();

        });
        
        

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


    private JButton crearBoton(String texto, String rutaIcono) {
        JButton boton = new JButton(texto);

        // Cargar el icono
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
            Image img = icono.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH); // Tamaño del icono
            boton.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + rutaIcono);
        }

        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setFont(new Font("Consolas", Font.PLAIN, 12));
        boton.setPreferredSize(new Dimension(100, 100));

        // Transparencia en reposo
        boton.setContentAreaFilled(false); // Quitar el fondo en reposo
        boton.setOpaque(false);           // Garantizar la transparencia
        boton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50))); // Borde transparente claro

        // Cambiar el color al hacer clic
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(true); // Fondo visible al hacer clic
                boton.setBackground(new Color(200, 200, 200, 100)); // Color suave al presionar
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(false); // Regresar a transparente
            }
        });

        boton.setForeground(Color.WHITE); // Mantener el texto blanco para visibilidad

        return boton;
    }

    //aqui funcion para acceder al reproductor de musica desde un panel especial
    public void cargarReproductorMusica(MenuPrincipal menuPrincipal) {
        if (reproductorFrame != null) {
            reproductorFrame.setVisible(true); // Volver a mostrar la ventana
        } else {
            // Crear el reproductor por primera vez si no existe
            JFXPanel jfxPanel = new JFXPanel();
            jfxPanel.setPreferredSize(new Dimension(850, 800));

            Platform.runLater(() -> {
                try {
                    Scene escena = new Scene(new VentanaPrincipal(nombreUsuario, menuPrincipal), 850, 750);
                    jfxPanel.setScene(escena);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            reproductorFrame = new JFrame("APP RoyXen -> Reproductor de Musica de " + nombreUsuario);
            reproductorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // No cerrar automáticamente

            // Añadir listener para manejar el cierre manualmente
            reproductorFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(
                            reproductorFrame,
                            "¿Estas seguro de que deseas cerrar el reproductor? si tienes musica en reproduccion esto detendra la musica.",
                            "Cerrar Reproductor de " + nombreUsuario,
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        detenerMusica();
                        reproductorFrame.setVisible(false); // Ocultar la ventana
                    }
                }
            });

            reproductorFrame.getContentPane().add(jfxPanel);
            reproductorFrame.pack();
            reproductorFrame.setLocationRelativeTo(null);
            reproductorFrame.setVisible(true);
        }
    }

    private void detenerMusica() {
        
        //Musica.ResetearReproductor();
        Musica.stop(); 
        
        
    }

}
