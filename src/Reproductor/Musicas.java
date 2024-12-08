/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import Base_De_Datos.ManejoUsuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
        

/**
 *
 * @author royum
 */
public final class Musicas extends JFrame{
    
    private String Nombreusuario;
    private File CarpetaUsuarioGestion;
    private File CarpetaUsuario;
    private ArrayList<Cancion> Canciones;
    private File archiEntrar;
    JButton btnDescargar;
    JButton btnInformacion;
    private boolean esAdmin;
    File ArchivoDestino;
    
    public Musicas(String Nombreusuario){
        this.Nombreusuario=Nombreusuario;
        
        this.esAdmin=new ManejoUsuarios().esAdmin(Nombreusuario);
        
        CarpetaUsuarioGestion=new File("UsuariosGestion");
        if (!CarpetaUsuarioGestion.exists() || !CarpetaUsuarioGestion.isDirectory()) {
            JOptionPane.showMessageDialog(null, "La carpeta 'UsuariosGestion' no existe. Por favor, verifica.");
            dispose();
            return;
        }
        
        CarpetaUsuario=new File(CarpetaUsuarioGestion,Nombreusuario);
         if (!CarpetaUsuario.exists() || !CarpetaUsuario.isDirectory()) {
            JOptionPane.showMessageDialog(null, 
                    "El usuario \"" + Nombreusuario + "\" no tiene una carpeta asignada.");
            dispose();
            return;
        }
         
        Canciones = CargarCanciones(); 
        
        setTitle("APP RoyXen -> Biblioteca Spotify de la cuenta de "+Nombreusuario);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar la ventana
        this.setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
         // Crear panel para las canciones
        JPanel panelCanciones = new JPanel();
        panelCanciones.setLayout(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(panelCanciones);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        for (Cancion cancion : Canciones) {
            
            panelCanciones.add(crearPanelCancion(cancion));
            
        }
        
        JButton btnVolver = crearBoton("Volver","volver.png");
        btnVolver.setForeground(Color.BLACK);
        btnVolver.addActionListener(e -> {
            dispose();
            MenuMusica music=new MenuMusica(Nombreusuario,archiEntrar);
            music.setVisible(true);
            
        });
        
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelInferior.setBackground(Color.white);
        panelInferior.add(btnVolver);

         if (esAdmin) {
            JButton btnEliminar = crearBoton("Delet Music", "Eliminar.png");
            btnEliminar.setForeground(Color.BLACK);
            btnEliminar.addActionListener(e -> {
                try {
                    EliminarMusica();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar la musica: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panelInferior.add(btnEliminar);
        }
            
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    
    }
    
    private JPanel crearPanelCancion(Cancion cancion) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // Borde visible
        panel.setBackground(new Color(240, 240, 240)); // Fondo inicial del panel

        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon ImagenEscalada = cancion.getImagenEscalada(300, 300);
        lblImagen.setIcon(ImagenEscalada);

        btnInformacion = crearBoton("Informacion", "info.png");
        btnInformacion.setForeground(Color.BLACK);
        btnInformacion.addActionListener(e -> mostrarInformacion(cancion));

        btnDescargar = crearBoton("Descargar", "descarga_1.png");
        btnDescargar.setForeground(Color.BLACK);
        
        File CarpetaUsuarioMusica=new File(CarpetaUsuario,"Musica");
        File ArchivoDescargado=new File(CarpetaUsuarioMusica,cancion.getTitulo() + ".mp3");
        
        if(ArchivoDescargado.exists()){
            
            btnDescargar.setText("Ya Descargada");
            btnDescargar.setEnabled(false);
            
        }else{
            
            btnDescargar.addActionListener(e -> DescargarCancion(cancion));
            
        }
        
           
        panel.add(lblImagen, BorderLayout.NORTH);
        panel.add(btnInformacion, BorderLayout.CENTER);
        panel.add(btnDescargar, BorderLayout.SOUTH);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                panel.setBackground(new Color(255, 235, 235)); // Fondo al pasar el mouse
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                panel.setBackground(new Color(240, 240, 240)); // Fondo inicial
            }
        });

        return panel;
    }

    private JButton crearBoton(String texto, String nombreIcono) {
        JButton boton = new JButton(texto);

        try {
            // Ruta de la imagen desde la carpeta img_Steam
            String rutaIcono = "/img_Steam/" + nombreIcono;
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
            Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Tamaño del ícono
            boton.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el ícono: " + nombreIcono);
        }

        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setFont(new Font("Consolas", Font.PLAIN, 14));
        boton.setPreferredSize(new Dimension(120, 120));
        boton.setContentAreaFilled(false); // Fondo transparente inicial
        boton.setOpaque(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Borde inicial
        boton.setForeground(Color.BLACK);

        // Añadir el MouseListener reutilizable
        boton.addMouseListener(crearMouseListenerBoton(boton));

        return boton;
    }

    private MouseAdapter crearMouseListenerBoton(JButton boton) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Borde azul
                boton.setBackground(new Color(220, 230, 250)); // Fondo azul claro
                boton.setOpaque(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Borde inicial
                boton.setOpaque(false); // Volver a fondo transparente
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boton.setBackground(new Color(180, 200, 255)); // Fondo al hacer clic
                boton.setOpaque(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boton.setBackground(new Color(220, 230, 250)); // Fondo al soltar clic
                boton.setOpaque(true);
            }
        };
    }

    private void mostrarInformacion(Cancion cancion) {
        String info = String.format(
                "Titulo: %s\nArtista: %s\nAlbum: %s\nDuracion: %s\nRuta Archivo: %s",
                cancion.getTitulo(),
                cancion.getArtista(),
                cancion.getAlbum(),
                cancion.getDuracion(),
                cancion.getRutaArchivo() // Aquí se añade la ruta
        );
        JOptionPane.showMessageDialog(this, info, "Informacion de la Cancion", JOptionPane.INFORMATION_MESSAGE);
    }
    private void DescargarCancion(Cancion cancion){

        if(cancion.getRutaArchivo()==null||cancion.getRutaArchivo().isEmpty()){
            
            JOptionPane.showMessageDialog(null, "La ruta del archivo musica no es valida");
            return;
            
        }
        
        try{
            
            String RutaBase=System.getProperty("user.dir");
            System.out.println("Ruta base" + RutaBase);

            File archivoOriginal = new File(cancion.getRutaArchivo());
            System.out.println("Ruta archivo original: " + archivoOriginal.getAbsolutePath());
            if (!archivoOriginal.exists()) {
                JOptionPane.showMessageDialog(this, "El archivo de musica no existe en: " + archivoOriginal.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            File carpetaDestino = new File(RutaBase + File.separator + "UsuariosGestion" + File.separator
                    + Nombreusuario + File.separator + "Musica");
            System.out.println("Ruta carpeta destino: " + carpetaDestino.getAbsolutePath());
            if (!carpetaDestino.exists()) {
                carpetaDestino.mkdirs();
            }
            
            ArchivoDestino = new File(carpetaDestino, cancion.getTitulo() + ".mp3");
            System.out.println("Archivo Destino: " + ArchivoDestino.getAbsolutePath());

            if(ArchivoDestino.exists()){
                JOptionPane.showMessageDialog(this, "El archivo ya fue descargado: " + archivoOriginal.getName(), "Cancion ya descargada", JOptionPane.ERROR_MESSAGE);
                btnDescargar.setText("Ya Descargada");
                btnDescargar.setEnabled(false);
                return;
            }

            //se copiar el archivo
            Files.copy(archivoOriginal.toPath(), ArchivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            JOptionPane.showMessageDialog(this,"Cancion descargada exitosamente en: " + ArchivoDestino.getAbsolutePath(), "Descarga Exitosa", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(this, "Error al descargar la cancion: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    public ArrayList<Cancion> CargarCanciones(){
        
        File ArchivoCanciones=new File("canciones.dat");
        ArrayList<Cancion> canciones=new ArrayList<>();
        
        if(ArchivoCanciones.exists()){
            
            try(ObjectInputStream cargar=new ObjectInputStream(new FileInputStream(ArchivoCanciones))){
                
                canciones=(ArrayList<Cancion>) cargar.readObject();
                
            }catch(Exception e){
                
                JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LAS CANCIONES","ERROR",JOptionPane.ERROR_MESSAGE);
                
            }
            
        }else{
            
               JOptionPane.showMessageDialog(this,
                    "No se encontro el archivo de musicas.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            
        }
        return canciones;
        
    }
    
    public ArrayList<Cancion> CargarMusicasDescargadas()throws IOException{
        ArrayList<Cancion> MusicasDescargadas=new ArrayList<>();
        
        File CarpetaUsuarioMusicas=new File(CarpetaUsuario,"Musica");
        if(!CarpetaUsuarioMusicas.exists()||!CarpetaUsuarioMusicas.isDirectory()){
            
            return MusicasDescargadas;
            
        }
        
        try{     
            File ArchivoMusica=new File("canciones.dat");
            if(ArchivoMusica.exists()){
                
                try(ObjectInputStream descargas=new ObjectInputStream(new FileInputStream(ArchivoMusica))){                    
                    ArrayList<Cancion> TodaslasMusicas=(ArrayList<Cancion>) descargas.readObject();
                    for(Cancion musiquita : TodaslasMusicas){                        
                        File ArchivoMusicaDescargado=new File(CarpetaUsuarioMusicas,musiquita.getTitulo()+ ".mp3");
                        System.out.println("Buscando archivo: " + ArchivoMusicaDescargado.getAbsolutePath());
                        if(ArchivoMusicaDescargado.exists()){    
                            musiquita.setRutaArchivo(ArchivoMusicaDescargado.getAbsolutePath());
                            MusicasDescargadas.add(musiquita);
                              System.out.println("Archivo encontrado y agregado: " + ArchivoMusicaDescargado.getName());
                            
                        }else{
                              System.out.println("Archivo no encontrado: " + ArchivoMusicaDescargado.getName());
                        }
                    }
                }  
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LAS MUSICAS","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        return MusicasDescargadas;  
    }

    private void EliminarMusica() {
        
        String nombreMusica=JOptionPane.showInputDialog(null, "Ingrese la duracion de la musica");
        if(nombreMusica==null||nombreMusica.trim().isEmpty()){
            
            JOptionPane.showMessageDialog(null, "Error, ingrese bien la duracion de la musica","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean Eliminado=Canciones.removeIf(musica-> musica.getDuracion().equalsIgnoreCase(nombreMusica));
        if(Eliminado){
        GuardarMusicas();
        JOptionPane.showMessageDialog(null, "LA MUSICA A SIDO ELIMINADA EXITOSAMENTE");
        dispose();
        new Musicas(Nombreusuario).setVisible(true);
           
        }else{
            
            JOptionPane.showMessageDialog(null, "NO SE PUDO ELIMNAR LA MUSICA");
            
        }
        
    }
    
    private void GuardarMusicas(){
        
        File ArchivosMusicas=new File("canciones.dat");
        try(ObjectOutputStream guardar=new ObjectOutputStream(new FileOutputStream(ArchivosMusicas))){
            guardar.writeObject(Canciones);
            
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR LAS CANCIONES ");
            
        }
        
    }
    
    
    
//    private ArrayList<Cancion> leerCancionesDesdeArchivo(String rutaArchivo) {
//        ArrayList<Cancion> lista=new ArrayList<>();
//        File archivo = new File(rutaArchivo);
//
//        if(archivo.exists()){
//            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
//                lista = (ArrayList<Cancion>) ois.readObject();
//            } catch (Exception e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Error al leer canciones: " + e.getMessage(),
//                        "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//        return lista;
//    }

}
