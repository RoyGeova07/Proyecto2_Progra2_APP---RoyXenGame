/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Steam;

import Base_De_Datos.ManejoUsuarios;
import Base_De_Datos.Usuario;
import Pantallas_Principales.MenuPrincipal;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author royum
 */
public class MenuSteam extends JFrame {

    private String usuarioActual; // Usuario actual como String
    private boolean esAdmin; // Determina si el usuario es administrador
    private JLabel fondo;
    private JPanel panelBotones;
    private File archivoUsuario; // Para el archivo binario
    private ManejoUsuarios manejoUsuarios;
    private Usuario user;

    public MenuSteam(String usuarioActual, File archivoUsuario) {
        this.usuarioActual = usuarioActual;
        this.archivoUsuario = archivoUsuario;

        // Inicializa el manejo de usuarios
        this.manejoUsuarios = new ManejoUsuarios();

        // Verifica si el usuario es administrador
        this.esAdmin = manejoUsuarios.esAdmin(usuarioActual);

        setTitle("APP RoyXen -> Menu Steam del Usuario: " + usuarioActual);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ventana maximizada
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // Fondo personalizado
        fondo = new JLabel();
        fondo.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(fondo, Integer.valueOf(0));

        // Crear panel de botones
        panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false); // Hacer el panel transparente
        panelBotones.setBounds(getWidth() / 4, getHeight() / 3, getWidth() / 2, getHeight() / 3);
        layeredPane.add(panelBotones, Integer.valueOf(1)); // Agregar los botones en una capa superior

        JButton btnVerJuegos = crearBoton("Ver Juegos", "/img_steam/ver_juegos.png");
        JButton btnAgregarJuegos = crearBoton("Agregar Juegos", "/img_steam/agregar_juegos.png");
        JButton btnVolver = crearBoton("Volver", "/img_steam/volver.png");

        if (!manejoUsuarios.esAdmin(usuarioActual)) {
            btnAgregarJuegos.setEnabled(false);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBotones.add(btnVerJuegos, gbc);

        gbc.gridx = 1;
        panelBotones.add(btnAgregarJuegos, gbc);

        gbc.gridx = 2;
        panelBotones.add(btnVolver, gbc);

        // Redimensionar el fondo y los componentes al cambiar tamaño de la ventana
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                fondo.setBounds(0, 0, getWidth(), getHeight());
                cargarFondo("/img_steam/fondo_steam.png");
                panelBotones.setBounds(getWidth() / 4, getHeight() / 3, getWidth() / 2, getHeight() / 3);
            }
        });

        // Listeners de botones
        btnVerJuegos.addActionListener(e -> verJuegos());
        btnAgregarJuegos.addActionListener(e -> {
            System.out.println("hola salido.");
            agregarJuegos();
        });
        btnVolver.addActionListener(e -> {
            try {
                volver();
            } catch (IOException ex) {
                Logger.getLogger(MenuSteam.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        setVisible(true);
    }

    private void cargarFondo(String ruta) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(ruta));
            Image img = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT); // Escalar el fondo
            fondo.setIcon(new ImageIcon(img));
            fondo.setHorizontalAlignment(SwingConstants.CENTER);
            fondo.setVerticalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el fondo: " + ruta);
        }
    }

    private JButton crearBoton(String texto, String rutaIcono) {
        JButton boton = new JButton(texto);

        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
            Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Tamaño del icono
            boton.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + rutaIcono);
        }   

        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setFont(new Font("Consolas", Font.PLAIN, 14));
        boton.setPreferredSize(new Dimension(120, 120));

        // Transparencia en reposo
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50))); // Borde transparente claro

        // Cambiar el color al hacer clic
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(true);
                boton.setBackground(new Color(200, 200, 200, 100));
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(false);
            }
        });

        boton.setForeground(Color.WHITE);
        return boton;
    }

    private void verJuegos() {

        Juegos_Steam juegos = new Juegos_Steam(usuarioActual);
        juegos.setVisible(true);
        dispose();

    }

    private void agregarJuegos() {

        
        if(!manejoUsuarios.esAdmin(usuarioActual)) {
            System.out.println("Usuario no es administrador.");
            JOptionPane.showMessageDialog(this,
                    "No tienes permisos de administrador. Acceso denegado.",
                    "Acceso Denegado",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Usuario usuario = manejoUsuarios.ObtenerUsuario(usuarioActual);

        if(usuario != null){

            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/img_Steam/confirmacion.png"));
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

            System.out.println("hola");
            JOptionPane.showMessageDialog(this, "Bienvenido, administrador: " + usuarioActual, "Acceso Permitido", JOptionPane.INFORMATION_MESSAGE,iconoEscalado);

            // Mostrar la pantalla para agregar juegos
            Agregar_Juegos agregarJuegosPanel = new Agregar_Juegos(usuarioActual);
            agregarJuegosPanel.setVisible(true);
            dispose();
        }else{
            JOptionPane.showMessageDialog(this,"El usuario no existe en el sistema. Verifique la configuracion.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volver() throws IOException {
        MenuPrincipal m = new MenuPrincipal(usuarioActual, archivoUsuario);
        m.setVisible(true);
        dispose(); // Cierra la ventana
    }
}
