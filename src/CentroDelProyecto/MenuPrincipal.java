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
public class MenuPrincipal extends JFrame {

    private JPanel panelPrincipal;
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

    public MenuPrincipal(String nombre) {
        this.nombreUsuario=nombre;
        GUI();
        manejoUsuarios=new ManejoUsuarios();
    }

    private void GUI() {
        setTitle("APP RoyXen -> Cuenta de "+nombreUsuario);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ventana maximizada
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Crear un JLayeredPane para manejar fondo y botones
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // Fondo personalizado
        fondo = new JLabel();
        fondo.setBounds(0, 0, getWidth(), getHeight()); // Tamaño inicial del fondo
        cargarFondo("/img_menuprin/fondo.jpg");
        layeredPane.add(fondo, Integer.valueOf(0)); // Agregar el fondo en la capa mas baja

        // Crear panel de botones
        panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false); // Hacer el panel transparente
        panelBotones.setBounds(getWidth() / 4, getHeight() / 3, getWidth() / 2, getHeight() / 3);
        layeredPane.add(panelBotones, Integer.valueOf(1)); // Agregar los botones en una capa superior

        // Crear botones con imágenes
        Juegos = crearBoton("Juegos", "/img_menuprin/juego.png");
        Musicas = crearBoton("Reproductor", "/img_menuprin/musica1.png");
        Discord = crearBoton("Discord", "/img_menuprin/discord1.png");
        Perfil = crearBoton("Mi Perfil", "/img_menuprin/perfil.jpg");
        Cerrar_Sesion = crearBoton("Cerrar Sesion", "/img_menuprin/cerrar_sesion.png");
        Administracion= crearBoton("Admin","/img_menuprin/administracion1.png");

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
        gbc.gridx=0;
        gbc.gridy=1;
        panelBotones.add(Perfil, gbc);
        
        gbc.gridx=1;
        panelBotones.add(Cerrar_Sesion,gbc);
        
        gbc.gridx=2;
        panelBotones.add(Administracion,gbc);

        // Redimensionar el fondo y los componentes al cambiar tamaño de la ventana
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                fondo.setBounds(0, 0, getWidth(), getHeight());
                cargarFondo("/img_menuprin/fondo.jpg");
                panelBotones.setBounds(getWidth() / 4, getHeight() / 3, getWidth() / 2, getHeight() / 3);
            }
        });
        
        Juegos.addActionListener(e-> {
            
            //AGREGA INSTANCIA JUEGO
            
        });
        
        Musicas.addActionListener(e-> {
            
            //AGREGAR INSTANCIA REPRODUCTOR
            
        });
        
        Discord.addActionListener(e-> {
            
            //AGREGAR INSTANCIA DISCORD
            
        });
        
        Perfil.addActionListener(e-> {
            
            //AGREGAR INSTANCIA PERFIL
            
        });
        
        Administracion.addActionListener(e-> {
            
            //AGREGAR INSTANCIA DE ADMINISTRACION 
            
        });
        
        Cerrar_Sesion.addActionListener(e-> {
            
            MenuInicio inicio=new MenuInicio();
            inicio.setVisible(true);
            dispose();
            
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal ventana = new MenuPrincipal(nombreUsuario);
            ventana.setVisible(true);
        });
    }
}