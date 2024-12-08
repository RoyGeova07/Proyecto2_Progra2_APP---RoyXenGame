/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Perfil_De_Usuario;

import Base_De_Datos.ManejoUsuarios;
import Base_De_Datos.Usuario;
import Pantallas_Principales.MenuPrincipal;
import Reproductor.Cancion;
import Reproductor.Musicas;
import Steam.Juego;
import Steam.Juegos_Steam;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author royum
 */
public final class Pantalla_Perfil extends JFrame {
//====DE ESTA CLASE======================================================================
    private String usuario;
    private JLabel lblFotoPerfil;
    private JPasswordField txtNuevaContrasena;
    private File entrarmenu;
//==DE JUEGOS=============================================================================
    private Juegos_Steam juegosSteam;
    private JPanel panelJuegos;
    private JScrollPane scrollJuegos;
//==DE MUSICAS============================================================================    
    private Musicas musicasPerfil;
    private JPanel panelMusicas;
    private JScrollPane scrollMusicas;

    public Pantalla_Perfil(String usuario) throws IOException {
        this.usuario = usuario;
        this.juegosSteam = new Juegos_Steam(usuario);
        juegosSteam.setVisible(false);
        
        this.musicasPerfil=new Musicas(usuario);
        musicasPerfil.setVisible(false);
            
        setTitle("APP RoyXen -> Perfil de " + usuario);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ventana maximizada
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        // Crear pestañas
        JTabbedPane pestañas = new JTabbedPane();

        panelJuegos = new JPanel();
        panelJuegos.setLayout(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(panelJuegos);
        
        panelMusicas=new JPanel();
        panelMusicas.setLayout(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPanemusica = new JScrollPane(panelMusicas);
        
        try {
            ActualizarPanelMusica();
            actualizarPanelJuegos();
        } catch (IOException ex) {
            Logger.getLogger(Pantalla_Perfil.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Crear y agregar los paneles a las pestañas
        pestañas.addTab("Mis datos de Usuario -> " + usuario, crearPanelUsuario());
        pestañas.addTab("Mis Juegos", crearPanelBibliotecas());
        pestañas.addTab("Mis Musicas", crearPanelMusica());

        // Añadir las pestañas a la ventana
        add(scrollPanemusica,BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
        add(pestañas, BorderLayout.CENTER);

        setVisible(true);
    }

    // Panel para datos del usuario
    private JPanel crearPanelUsuario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.LIGHT_GRAY);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false); // Transparente para mantener el fondo general
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                "Informacion del Usuario",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16),
                Color.BLACK
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Etiqueta de título
        JLabel lblTitulo = new JLabel("Perfil de Usuario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        infoPanel.add(lblTitulo, gbc);

        // Foto de perfil
        lblFotoPerfil = new JLabel();
        Usuario usuariobj=new ManejoUsuarios().ObtenerUsuario(usuario);
        
        if(usuariobj!=null&&usuariobj.getRutaFotoPerfil()!=null && !usuariobj.getRutaFotoPerfil().isEmpty()){
            
            lblFotoPerfil.setIcon(cargarImagen(usuariobj.getRutaFotoPerfil()));
            
        }else{
            
            lblFotoPerfil.setIcon(cargarImagen(getClass().getResource("/img_menuprin/perfil.jpg").getPath()));
            
        }
        
       
        lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoPerfil.setPreferredSize(new Dimension(150, 150));
        lblFotoPerfil.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        infoPanel.add(lblFotoPerfil, gbc);

        JButton btnCambiarFoto = new JButton("Cambiar Foto de Perfil");
        estilizarBoton(btnCambiarFoto);
        btnCambiarFoto.addActionListener(e -> cambiarFotoPerfil());
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        infoPanel.add(btnCambiarFoto, gbc);

        // Nombre de usuario
        JLabel lblUsuario = new JLabel("Usuario: " + usuario);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUsuario.setForeground(Color.BLACK);
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        infoPanel.add(lblUsuario, gbc);

        // Botón para cambiar contraseña usando InputDialog
        JButton btnCambiarContrasena = new JButton("Cambiar Contraseña");
        estilizarBoton(btnCambiarContrasena);
        btnCambiarContrasena.addActionListener(e -> {
            JPasswordField passwordField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(this,passwordField,"Ingrese nueva contraseña",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                String nuevaContrasena = new String(passwordField.getPassword());
                if (!nuevaContrasena.isEmpty()) {
                    cambiarContrasena(nuevaContrasena);
                } else {
                    JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        infoPanel.add(btnCambiarContrasena, gbc);

        JButton volver = new JButton("Volver");
        estilizarBoton(volver);
        volver.addActionListener(e -> {
            try {
                dispose();
                Volver();
            } catch (IOException ex) {
                Logger.getLogger(Pantalla_Perfil.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        gbc.gridy = 5;
        infoPanel.add(volver, gbc);

        // Añadir infoPanel al panel principal
        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }


    private void cambiarContrasena(String nuevaContrasena) {
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


    // Cambiar foto de perfil
    private void cambiarFotoPerfil() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imagenes", "jpg", "png", "jpeg"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            lblFotoPerfil.setIcon(cargarImagen(ruta));
            
           ManejoUsuarios manejousuarios=new ManejoUsuarios();
           Usuario usua=manejousuarios.ObtenerUsuario(usuario);
           
           if(usua!=null){
               
               usua.setRutaFotoPerfil(ruta);
               manejousuarios.GuardarUsuarios(); // Guardar los cambios
               JOptionPane.showMessageDialog(this, "Foto de perfil actualizada y guardada.");

           }else{
               
               JOptionPane.showMessageDialog(this, "Error: Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);

            }

        }
    }

    // Panel para mostrar las bibliotecas
    private JPanel crearPanelBibliotecas() throws IOException {
        JPanel panel = new JPanel(new BorderLayout());

        panelJuegos = new JPanel(new GridLayout(0, 3, 10, 10));
        scrollJuegos = new JScrollPane(panelJuegos);

        // Cargar y añadir los juegos
        ArrayList<Juego> juegos = juegosSteam.cargarJuegosDescargados();
        for (Juego juego : juegos) {
            System.out.println("Juego cargado: " + juego.getNombre());
            panelJuegos.add(crearPanelJuego(juego));
        }

        panel.add(scrollJuegos, BorderLayout.CENTER); // También agrega scrollJuegos para los juegos
        return panel;
    }

    private JPanel crearPanelMusica() throws IOException {

        JPanel panel = new JPanel(new BorderLayout());

        panelMusicas = new JPanel(new GridLayout(0, 3, 10, 10));
        scrollMusicas = new JScrollPane(panelMusicas);

        ArrayList<Cancion> canciones = musicasPerfil.CargarMusicasDescargadas();
        for (Cancion cancion : canciones) {
            System.out.println("Cancion cargada: " + cancion.getTitulo());
            panelMusicas.add(CrearPanelCancion(cancion));
        }

        panel.add(scrollMusicas, BorderLayout.CENTER);

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

        JPanel panelInfo = new JPanel(new GridLayout(0, 1, 5, 5));
        panelInfo.setBackground(Color.WHITE);

        JLabel lblNombreJuego=new JLabel("Titulo: " + juego.getNombre());
        lblNombreJuego.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombreJuego.setForeground(Color.BLACK);

        JLabel lblGenero=new JLabel("Genero: " + juego.getGenero());
        lblGenero.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblDesarrollador=new JLabel("Desarrollador: " + juego.getDesarrollador());
        lblDesarrollador.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblFechaLanzamiento=new JLabel("Lanzamiento: " + juego.getFechaLanzamiento());
        lblFechaLanzamiento.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblRutaInstalacion=new JLabel("Ruta: " + juego.getRutaInstalacion());
        lblRutaInstalacion.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton EliminarJuego=new JButton("Eliminar Juego");
        EliminarJuego.addActionListener(e -> {
            try {
                elimnarjueguito(juego);
            } catch (IOException ex) {
                Logger.getLogger(Pantalla_Perfil.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        panelInfo.add(lblNombreJuego);
        panelInfo.add(lblGenero);
        panelInfo.add(lblDesarrollador);
        panelInfo.add(lblFechaLanzamiento);
        panelInfo.add(lblRutaInstalacion);
        panelInfo.add(EliminarJuego);

        panel.add(lblCaratula, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel CrearPanelCancion(Cancion cancion) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(245, 245, 245)); // Color de fondo claro

        JLabel lblCaratula = new JLabel();
        lblCaratula.setHorizontalAlignment(SwingConstants.CENTER);
        byte[] caratula = cancion.getImagen();
        if (caratula != null) {
            lblCaratula.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(caratula).getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        } else {
            lblCaratula.setText("Sin Imagen");
            lblCaratula.setForeground(Color.GRAY);
            lblCaratula.setFont(new Font("Arial", Font.ITALIC, 12));
        }

        JPanel panelInfo = new JPanel(new GridLayout(0, 1, 5, 5));
        panelInfo.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Titulo: " + cancion.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(Color.BLACK);

        JLabel lblArtista = new JLabel("Artista: " + cancion.getArtista());
        lblArtista.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblAlbum = new JLabel("Album: " + cancion.getAlbum());
        lblAlbum.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblDuracion = new JLabel("Duracion: " + cancion.getDuracion());
        lblDuracion.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel lblRutaArchivo = new JLabel("Ruta de Archivo: " + cancion.getRutaArchivo());
        lblRutaArchivo.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton btnEliminar = new JButton("Eliminar Cancion");
        btnEliminar.addActionListener(e -> {
            try {
                EliminarCancion(cancion);
            } catch (IOException ex) {
                Logger.getLogger(Pantalla_Perfil.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        panelInfo.add(lblTitulo);
        panelInfo.add(lblArtista);
        panelInfo.add(lblAlbum);
        panelInfo.add(lblDuracion);
        panelInfo.add(lblRutaArchivo);
        panelInfo.add(btnEliminar);

        panel.add(lblCaratula, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.CENTER);

        return panel;
    }

  
    private ImageIcon cargarImagen(String ruta) {
        return new ImageIcon(new ImageIcon(ruta).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
    }

    private void elimnarjueguito(Juego juego) throws IOException {

        int confirm = JOptionPane.showConfirmDialog(this,"¿Estas seguro de que quieres eliminar este juego?","Confirmar eliminacion",JOptionPane.YES_NO_OPTION);

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
    
    private void EliminarCancion(Cancion cancion) throws IOException{
        
        int confirm = JOptionPane.showConfirmDialog(this,"¿Estass seguro de que quieres eliminar esta cancion?","Confirmar eliminacion",JOptionPane.YES_NO_OPTION);
        
        String basedir= System.getProperty("user.dir");
            
        String rutaJuego=basedir+ "/UsuariosGestion/" +usuario+ "/Musica/" + cancion.getTitulo() + ".mp3";
        
        
        if (confirm == JOptionPane.YES_OPTION) {
            File archivoCancion=new File(rutaJuego);
            if (archivoCancion.exists() && archivoCancion.delete()) {
                JOptionPane.showMessageDialog(this, "Cancion eliminada correctamente.");
                try {
                    ActualizarPanelMusica(); // Refrescar el panel
                } catch (IOException ex) {
                    Logger.getLogger(Pantalla_Perfil.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la canción.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
    
     private void ActualizarPanelMusica() throws IOException {
        panelMusicas.removeAll(); // Limpia el panel antes de actualizar

        ArrayList<Cancion> cancionesDescargadas = musicasPerfil.CargarMusicasDescargadas();
        if (cancionesDescargadas.isEmpty()) {
            System.out.println("No se encontraron canciones para mostrar.");
        } else {
            for (Cancion cancion : cancionesDescargadas) {
                System.out.println("Cancion cargada: " + cancion.getTitulo());
                panelMusicas.add(CrearPanelCancion(cancion));
            }
        }

        panelMusicas.revalidate();
        panelMusicas.repaint();
    }
     
    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(30, 144, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
}
