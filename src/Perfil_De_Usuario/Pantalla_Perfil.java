/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Perfil_De_Usuario;

import Base_De_Datos.ManejoUsuarios;
import Base_De_Datos.Usuario;
import Pantallas_Principales.MenuPrincipal;
import Reproductor.Musicas;
import Steam.Juego;
import Steam.Juegos_Steam;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author royum
 */
public class Pantalla_Perfil extends JFrame {
//====DE ESTA CLASE===============================================================
    private String usuario;
    private JLabel lblFotoPerfil;
    private JPasswordField txtNuevaContrasena;
    private File entrarmenu;
//==DE JUEGOS==============================================================================
    private Juegos_Steam juegosSteam;
    private JPanel panelJuegos;
    private JScrollPane scrollJuegos;
    
//==DE MUSICAS==============================================================================    
    private JPanel panelMusicas;
    private Musicas musicasPerfil;
    

    public Pantalla_Perfil(String usuario) throws IOException {
        this.usuario = usuario;
        this.juegosSteam = new Juegos_Steam(usuario);
        juegosSteam.setVisible(false);
        

        setTitle("APP RoyXen -> Perfil de " + usuario);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        // Crear pestañas
        JTabbedPane pestañas = new JTabbedPane();

        panelJuegos = new JPanel();
        panelJuegos.setLayout(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(panelJuegos);
        
        try {
            actualizarPanelJuegos();
        } catch (IOException ex) {
            Logger.getLogger(Pantalla_Perfil.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Crear y agregar los paneles a las pestañas
        pestañas.addTab("Mis datos de Usuario -> " + usuario, crearPanelUsuario());
        pestañas.addTab("Mis Juegos", crearPanelBibliotecas());
        pestañas.addTab("Mis Musicas", crearPanelMusica());

        // Añadir las pestañas a la ventana
        add(scrollPane, BorderLayout.CENTER);
        add(pestañas, BorderLayout.CENTER);

        setVisible(true);
    }

    // Panel para datos del usuario
    private JPanel crearPanelUsuario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información del Usuario"));
        panel.setPreferredSize(new Dimension(400, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Etiqueta de título
        JLabel lblTitulo = new JLabel("Perfil de Usuario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Foto de perfil
        lblFotoPerfil = new JLabel();
        lblFotoPerfil.setIcon(cargarImagen(getClass().getResource("/img_menuprin/perfil.jpg").getPath()));
        lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoPerfil.setPreferredSize(new Dimension(150, 150));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(lblFotoPerfil, gbc);

        // Botón para cambiar foto de perfil
        JButton btnCambiarFoto = new JButton("Cambiar Foto de Perfil");
        btnCambiarFoto.addActionListener(e -> cambiarFotoPerfil());
        gbc.gridy = 2;
        panel.add(btnCambiarFoto, gbc);

        // Nombre de usuario
        JLabel lblUsuario = new JLabel("Usuario: " + usuario);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(lblUsuario, gbc);

        // Cambiar contraseña
        JLabel lblContrasena = new JLabel("Nueva Contraseña:");
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(lblContrasena, gbc);

        txtNuevaContrasena = new JPasswordField();
        txtNuevaContrasena.setMaximumSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(txtNuevaContrasena, gbc);

        JButton btnCambiarContrasena = new JButton("Actualizar Contraseña");
        btnCambiarContrasena.addActionListener(e -> cambiarContrasena());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(btnCambiarContrasena, gbc);

        // Botón para volver al menú principal
        JButton volver = new JButton("Volver");
        volver.addActionListener(e -> {
            try {
                dispose();
                Volver();
            } catch (IOException ex) {
                Logger.getLogger(Pantalla_Perfil.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        gbc.gridy = 6;
        panel.add(volver, gbc);

        return panel;
    }

// Cambiar contraseña
    private void cambiarContrasena() {
        String nuevaContrasena = new String(txtNuevaContrasena.getPassword());
        if (nuevaContrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ManejoUsuarios manejoUsuarios = new ManejoUsuarios();
        Usuario usuarioObj = manejoUsuarios.ObtenerUsuario(usuario);

        if (usuarioObj != null) {
            usuarioObj.setPassword(nuevaContrasena);
            manejoUsuarios.GuardarUsuarios();
            JOptionPane.showMessageDialog(this, "Contraseña actualizada correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Panel para mostrar las bibliotecas
    private JPanel crearPanelBibliotecas() throws IOException {
        JPanel panel = new JPanel(new BorderLayout());

        // Crear y asignar paneles
        panelJuegos = new JPanel(new GridLayout(0, 3, 10, 10));
        scrollJuegos = new JScrollPane(panelJuegos);

        // Cargar y añadir los juegos
        ArrayList<Juego> juegos = juegosSteam.cargarJuegosDescargados();
        for (Juego juego : juegos) {
            System.out.println("Juego cargado: " + juego.getNombre());
            panelJuegos.add(crearPanelJuego(juego));
        }

        panel.add(scrollJuegos, BorderLayout.CENTER);
        return panel;
    }

    private void Volver() throws IOException {

        MenuPrincipal m = new MenuPrincipal(usuario, entrarmenu);
        m.setVisible(true);

    }

    private JPanel crearPanelJuego(Juego juego) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(245, 245, 245)); // Color de fondo claro

        // Imagen de caratula
        JLabel lblCaratula = new JLabel();
        lblCaratula.setHorizontalAlignment(SwingConstants.CENTER);
        byte[] caratula = juego.getCaratula();
        if (caratula != null) {
            lblCaratula.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(caratula).getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        } else {
            lblCaratula.setText("Sin Imagen");
            lblCaratula.setForeground(Color.GRAY);
            lblCaratula.setFont(new Font("Arial", Font.ITALIC, 12));
        }

        // Información del juego en un panel vertical
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(Color.WHITE);

        JLabel lblNombreJuego = new JLabel("<html><b>Titulo:</b> " + juego.getNombre() + "</html>");
        lblNombreJuego.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombreJuego.setForeground(Color.BLACK);

        JLabel lblGenero = new JLabel("<html><b>Genero:</b> " + juego.getGenero() + "</html>");
        lblGenero.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblDesarrollador = new JLabel("<html><b>Desarrollador:</b> " + juego.getDesarrollador() + "</html>");
        lblDesarrollador.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblFechaLanzamiento = new JLabel("<html><b>Lanzamiento:</b> " + juego.getFechaLanzamiento() + "</html>");
        lblFechaLanzamiento.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblRutaInstalacion = new JLabel("<html><b>Ruta:</b> " + juego.getRutaInstalacion() + "</html>");
        lblRutaInstalacion.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JButton EliminarJuego =new JButton("Eliminar Juego");
        EliminarJuego.addActionListener(e-> {
            
            try {
                elimnarjueguito(juego);
            } catch (IOException ex) {
                Logger.getLogger(Pantalla_Perfil.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });

        
        panelInfo.add(lblNombreJuego);
        panelInfo.add(Box.createVerticalStrut(5)); // Espaciado entre líneas
        panelInfo.add(lblGenero);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblDesarrollador);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblFechaLanzamiento);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblRutaInstalacion);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(EliminarJuego);

        // Combinar imagen y detalles en el panel principal
        panel.add(lblCaratula, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.CENTER);

        return panel;
    }

  
    private JPanel crearPanelMusica() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel para las canciones
        JPanel panelCanciones = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollMusica = new JScrollPane(panelCanciones);

        
        for (int i = 1; i <= 10; i++) {
            panelCanciones.add(crearPanelCancion("Titulo " + i, "Artista " + i, "Album ", "3:30 ", "Juegos"));
        }

        panel.add(scrollMusica, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelCancion(String titulo, String artista, String Album, String duracion,String rutaArchivo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        // Imagen de portada
        JLabel lblPortada = new JLabel("Portada");
        lblPortada.setHorizontalAlignment(SwingConstants.CENTER);
        lblPortada.setPreferredSize(new Dimension(100, 100));

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.add(new JLabel("Titulo: " + titulo));
        panelInfo.add(new JLabel("Artista: " + artista));
        panelInfo.add(new JLabel("Album: " + Album));
        panelInfo.add(new JLabel("Duracion: " + duracion));
        panelInfo.add(new JLabel("Ruta Archivo: "+rutaArchivo));

        // Añadir a panel principal
        panel.add(lblPortada, BorderLayout.WEST);
        panel.add(panelInfo, BorderLayout.CENTER);

        return panel;
    }

    // Cambiar foto de perfil
    private void cambiarFotoPerfil() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imagenes", "jpg", "png", "jpeg"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            lblFotoPerfil.setIcon(cargarImagen(ruta));
            JOptionPane.showMessageDialog(this, "Foto de perfil actualizada.");
        }
    }

    private ImageIcon cargarImagen(String ruta) {
        return new ImageIcon(new ImageIcon(ruta).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
    }

    private void elimnarjueguito(Juego juego) throws IOException {

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estas seguro de que quieres eliminar este juego?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            
            String basedir= System.getProperty("user.dir");
            
            String rutaJuego=basedir+ "/UsuariosGestion/" +usuario+ "/Juegos/" + juego.getNombre() + ".dat";
            
            File archivoJuego = new File(rutaJuego);
            if (archivoJuego.exists() && archivoJuego.delete()) {
                JOptionPane.showMessageDialog(this, "Juego eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            
             
           actualizarPanelJuegos();
        }

    }

    public void actualizarPanelJuegos() throws IOException {
        panelJuegos.removeAll(); // Limpia el contenido actual

       
        ArrayList<Juego> JuegosDescargados= juegosSteam.cargarJuegosDescargados();
        for (Juego juego : JuegosDescargados) {
            System.out.println("Juego cargado: " + juego.getNombre());
            panelJuegos.add(crearPanelJuego(juego));
            
        }
        
        panelJuegos.revalidate();
        panelJuegos.repaint();
        
    }
    


}
