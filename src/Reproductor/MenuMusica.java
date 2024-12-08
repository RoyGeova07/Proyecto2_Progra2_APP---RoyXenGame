/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import Base_De_Datos.ManejoUsuarios;
import Base_De_Datos.Usuario;
import Pantallas_Principales.MenuPrincipal;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author royum
 */
public class MenuMusica extends JFrame {
    
    private String Usuarioactual;
    private boolean Esadmin;
    private File Archivoentrar;
    private ManejoUsuarios manejousuarios;
    private Usuario user;

    public MenuMusica(String UsuarioActual,File ArchivoUsuario) {
        this.Usuarioactual=UsuarioActual;
        this.Archivoentrar= ArchivoUsuario;
        
        this.manejousuarios=new ManejoUsuarios();
        
        this.Esadmin=manejousuarios.esAdmin(UsuarioActual);
        
        setTitle("APP RoyXen -> Menu de musica del Usuario: "+UsuarioActual);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar la ventana

        // Crear el panel principal con fondo
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image backgroundImage = new ImageIcon(getClass().getResource("/img_repro/fon4.jpg")).getImage();
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    System.out.println("No se pudo cargar la imagen de fondo.");
                }
            }
        };
        panelPrincipal.setLayout(new GridBagLayout()); // Usar un layout para centrar los botones

        // Crear los botones
        JButton btnVolver = crearBoton("Volver", "/img_steam/volver.png");
        JButton btnAgregarMusica = crearBoton("Agregar Musica", "/img_repro/agregarmu.png");
        JButton btnVerMusicas = crearBoton("Ver Musicas", "/img_repro/spotify.png");
        
        if(!manejousuarios.esAdmin(UsuarioActual)){
            btnAgregarMusica.setVisible(false);
        }

        // Añadir acciones a los botones
        btnVolver.addActionListener(e -> {
            
            Volver();
            
        });
        btnAgregarMusica.addActionListener(e -> {

            AgregarMusica.SeleccionaryAgregarCancion();
            
        });
        
        btnVerMusicas.addActionListener(e -> {
           
            dispose();
            Musicas m=new Musicas(UsuarioActual);
            m.setVisible(true);
            
        });

        // Añadir los botones al panel principal
         GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Espaciado entre botones
        gbc.gridy = 0; // Todos los botones estarán en la misma fila (horizontal)
        
        gbc.gridx = 0; // Primera columna
        panelPrincipal.add(btnVerMusicas, gbc);

        gbc.gridx = 1; // Segunda columna
        panelPrincipal.add(btnAgregarMusica, gbc);

        gbc.gridx = 2; // Tercera columna
        panelPrincipal.add(btnVolver, gbc);

        // Añadir el panel al JFrame
        setContentPane(panelPrincipal);
        setVisible(true);
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
        boton.setFont(new Font("Consolas", Font.PLAIN, 13));
        boton.setPreferredSize(new Dimension(100, 100));

        // Transparencia en reposo
        boton.setContentAreaFilled(false); // Quitar el fondo en reposo
        boton.setOpaque(false);           // Garantizar la transparencia
        boton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50))); // Borde transparente claro

        
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

  
    

    private void Volver() {
        
        try {
            dispose();
            MenuPrincipal m=new MenuPrincipal(Usuarioactual, Archivoentrar);
            m.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(MenuMusica.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
}
