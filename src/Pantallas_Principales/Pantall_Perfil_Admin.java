/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas_Principales;


import Base_De_Datos.ManejoUsuarios;
import Base_De_Datos.Usuario;
import Perfil_De_Usuario.Pantalla_Perfil;
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

/**
 *
 * @author royum
 */
public class Pantall_Perfil_Admin extends JFrame{
    
     //====DE ESTA CLASE======================================================================
    private String adminUsuario; // Usuario administrador
    private String usuarioPerfil; // Usuario cuyo perfil se está viendo
    private JLabel lblFotoPerfil;
    private File entrarmenu;
    private MenuPrincipal menuprin;

    //==DE JUEGOS=============================================================================
    private Juegos_Steam juegosSteam;
    private JPanel panelJuegos;
    private JScrollPane scrollJuegos;

    //==DE MUSICAS============================================================================    
    private Musicas musicasPerfil;
    private JPanel panelMusicas;
    private JScrollPane scrollMusicas;

    public Pantall_Perfil_Admin(String adminUsuario, String usuarioPerfil,MenuPrincipal menuprin) throws IOException {
        this.adminUsuario = adminUsuario;
        this.usuarioPerfil = usuarioPerfil;
        this.menuprin=menuprin;

        this.juegosSteam = new Juegos_Steam(usuarioPerfil);
        juegosSteam.setVisible(false);

        this.musicasPerfil = new Musicas(usuarioPerfil);
        musicasPerfil.setVisible(false);

        setTitle("Admin View -> Perfil de " + usuarioPerfil);
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

        panelMusicas = new JPanel();
        panelMusicas.setLayout(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPanemusica = new JScrollPane(panelMusicas);

        try {
            ActualizarPanelMusica();
            actualizarPanelJuegos();
        } catch (IOException ex) {
            Logger.getLogger(Pantall_Perfil_Admin.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Crear y agregar los paneles a las pestañas
        pestañas.addTab("Datos del Usuario -> " + usuarioPerfil, crearPanelUsuario());
        pestañas.addTab("Juegos del Usuario", crearPanelBibliotecas());
        pestañas.addTab("Musica del Usuario", crearPanelMusica());

        add(scrollPanemusica, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
        add(pestañas, BorderLayout.CENTER);

        CargaFotoPerfil();
        
        setVisible(true);
    }


        private JPanel crearPanelUsuario() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            panel.setBackground(Color.LIGHT_GRAY);

            JPanel infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setOpaque(false);
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

            JLabel lblTitulo = new JLabel("Perfil de Usuario (Vista Administrador)");
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
            lblTitulo.setForeground(Color.DARK_GRAY);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            infoPanel.add(lblTitulo, gbc);

            // Nombre de usuario
            JLabel lblUsuario = new JLabel("Usuario: " + usuarioPerfil);
            lblUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
            lblUsuario.setForeground(Color.BLACK);
            gbc.gridy = 1;
            infoPanel.add(lblUsuario, gbc);

            // Foto de perfil
            lblFotoPerfil = new JLabel();
            lblFotoPerfil.setIcon(cargarImagen(getClass().getResource("/img_menuprin/perfil.jpg").getPath()));
            lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
            lblFotoPerfil.setPreferredSize(new Dimension(150, 150));
            lblFotoPerfil.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            gbc.gridy = 2;
            infoPanel.add(lblFotoPerfil, gbc);

            JButton volver = new JButton("Volver");
            volver.addActionListener(e -> volver());
            gbc.gridy = 3;
            infoPanel.add(volver, gbc);

            // Añadir infoPanel al panel principal
            panel.add(infoPanel, BorderLayout.CENTER);
            return panel;
        }

    private void ActualizarPanelMusica() throws IOException {
        ArrayList<Cancion> canciones = musicasPerfil.CargarMusicasDescargadas();
        for (Cancion cancion : canciones) {
            panelMusicas.add(CrearPanelCancion(cancion));
        }
        panelMusicas.revalidate();
        panelMusicas.repaint();
    }

    private void actualizarPanelJuegos()throws IOException{
        
        ArrayList<Juego>juegos=juegosSteam.cargarJuegosDescargados();
        for (Juego juego:juegos) {
            panelJuegos.add(crearPanelJuego(juego));
        }
        panelJuegos.revalidate();
        panelJuegos.repaint();
        
    }

    private JPanel crearPanelBibliotecas() throws IOException {
        
        JPanel panel=new JPanel(new BorderLayout());
        panelJuegos=new JPanel(new GridLayout(0, 3, 10, 10));
        scrollJuegos=new JScrollPane(panelJuegos);
        actualizarPanelJuegos();
        panel.add(scrollJuegos, BorderLayout.CENTER);
        return panel;
        
    }

    private JPanel crearPanelMusica() throws IOException {
        JPanel panel=new JPanel(new BorderLayout());
        panelMusicas=new JPanel(new GridLayout(0, 3, 10, 10));
        scrollMusicas=new JScrollPane(panelMusicas);
        ActualizarPanelMusica();
        panel.add(scrollMusicas, BorderLayout.CENTER);
        return panel;
    }

    private ImageIcon cargarImagen(String ruta) {
        return new ImageIcon(new ImageIcon(ruta).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
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

        panelInfo.add(lblNombreJuego);
        panelInfo.add(lblGenero);
        panelInfo.add(lblDesarrollador);
        panelInfo.add(lblFechaLanzamiento);
        panelInfo.add(lblRutaInstalacion);

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


        panelInfo.add(lblTitulo);
        panelInfo.add(lblArtista);
        panelInfo.add(lblAlbum);
        panelInfo.add(lblDuracion);
        panelInfo.add(lblRutaArchivo);

        panel.add(lblCaratula, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.CENTER);

        return panel;
    }

    private void volver() {
        
        menuprin.setVisible(true);
        dispose();
        
    }
    
    private void CargaFotoPerfil(){
        
        ManejoUsuarios manejousaurios=new ManejoUsuarios();
        Usuario usuario=manejousaurios.ObtenerUsuario(usuarioPerfil);
        
        if(usuario!=null&&usuario.getRutaFotoPerfil()!=null){
            
            String Rutaperfil=usuario.getRutaFotoPerfil();
            lblFotoPerfil.setIcon(cargarImagen(Rutaperfil));
            
        }else{
            
            lblFotoPerfil.setIcon(cargarImagen(getClass().getResource("/img_menuprin/perfil.jpg").getPath()));
            
        }
        
    }
    
}