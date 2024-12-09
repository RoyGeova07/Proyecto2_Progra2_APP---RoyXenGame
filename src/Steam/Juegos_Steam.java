/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Steam;

import Base_De_Datos.ManejoUsuarios;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 *  
 * @author royum
 */
public final class Juegos_Steam extends JFrame {

    private String nombreUsuario;
    private File carpetaUsuariosGestion;
    private File carpetaUsuario;
    private boolean Esadmin;
    private ArrayList<Juego> juegos;
    Juego juego;
    JButton btnDescargar;
    File archivoDestino;
    

    public Juegos_Steam(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;

        this.Esadmin = new ManejoUsuarios().esAdmin(nombreUsuario);

        carpetaUsuariosGestion = new File("UsuariosGestion");
        if (!carpetaUsuariosGestion.exists() || !carpetaUsuariosGestion.isDirectory()) {
            JOptionPane.showMessageDialog(null,
                    "La carpeta raiz 'UsuariosGestion' no existe. Por favor, verifica la configuración.");
            dispose();
            return;
        }

        carpetaUsuario = new File(carpetaUsuariosGestion, nombreUsuario);
        if (!carpetaUsuario.exists() || !carpetaUsuario.isDirectory()) {
            JOptionPane.showMessageDialog(null,
                    "El usuario \"" + nombreUsuario + "\" no tiene una carpeta asignada. Por favor, verifica el sistema.");
            dispose();
            return;
        }

        juegos = cargarJuegos();

        setTitle("APP RoyXen -> Biblioteca Steam de la cuenta - " + nombreUsuario);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panelJuegos = new JPanel();
        panelJuegos.setLayout(new GridLayout(0, 3, 10, 10));
        panelJuegos.setBackground(new Color(240, 240, 240)); // Fondo claro
        JScrollPane scrollPane = new JScrollPane(panelJuegos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Añadir cada juego al panel
        for (Juego juego : juegos) {
            panelJuegos.add(crearPanelJuego(juego));
        }

        JButton btnVolver = crearBoton("Volver", "volver.png");
        btnVolver.setForeground(Color.BLACK);
        btnVolver.addActionListener(e -> {
            MenuSteam m = new MenuSteam(nombreUsuario, carpetaUsuario);
            m.setVisible(true);
            dispose();
        });
        
        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.add(btnVolver);

        if (Esadmin) {
            JButton btnEliminar = crearBoton("Eliminar Juego", "Eliminar.png");
            btnEliminar.setForeground(Color.BLACK);
            btnEliminar.addActionListener(e -> {
                try {
                    eliminarJuego();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"Error al eliminar el juego: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panelInferior.add(btnEliminar);
        }

        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel crearPanelJuego(Juego juego) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            byte[] caratulaBytes = juego.getCaratula();
            if (caratulaBytes != null && caratulaBytes.length > 0) {
                Image img = Toolkit.getDefaultToolkit().createImage(caratulaBytes);
                ImageIcon icon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                lblImagen.setIcon(icon);
            } else {
                lblImagen.setText("Sin Imagen");
                lblImagen.setForeground(Color.GRAY);
            }
        } catch (Exception ex) {
            lblImagen.setText("Error al cargar imagen");
            lblImagen.setForeground(Color.RED);
        }

        panel.add(lblImagen, BorderLayout.NORTH);

        JButton btnBuscar = crearBoton("Informacion", "search.png");
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.addActionListener(e -> mostrarInformacion(juego));
        panel.add(btnBuscar, BorderLayout.CENTER);

        btnDescargar = crearBoton("Descargar", "Descarga.png");
        btnDescargar.setForeground(Color.BLACK);
        
        File CarpetaUsuariosJuegos=new File(carpetaUsuario, "Juegos");
        File ArchivoDescargado=new File(CarpetaUsuariosJuegos,juego.getNombre() + ".dat");
        
        if(ArchivoDescargado.exists()){
            
            btnDescargar.setText("Ya descargado");
            btnDescargar.setEnabled(false);
            
        }else{  
            
            btnDescargar.addActionListener(e-> descargarJuego(juego));
            
        }
        
        
        btnDescargar.addActionListener(e -> descargarJuego(juego));
        panel.add(btnDescargar, BorderLayout.SOUTH);

        return panel;
    }

    private JButton crearBoton(String texto, String nombreIcono) {
        JButton boton = new JButton(texto);

        try {
            
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

    public ArrayList<Juego> cargarJuegos() {
        File archivoJuegos = new File("juegos.dat");
        ArrayList<Juego> juegos = new ArrayList<>();

        if (archivoJuegos.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoJuegos))) {
                juegos = (ArrayList<Juego>) ois.readObject();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,"Error al cargar los juegos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se encontro el archivo de juegos. Se cargara una lista vacia.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        return juegos;
    }
    
    //ORDENAR FUNCION DONDE SI DESCARGAS EL JUEGO APARECERA EL JUEGO APARECERA EN LA CLASE PANTALLA PERFIL SOLUCIONAR ESO
    public ArrayList<Juego> cargarJuegosDescargados()throws IOException {
        ArrayList<Juego> juegosDescargados=new ArrayList<>();
        
        File CarpetaUsuarioJuegos=new File(carpetaUsuario,"Juegos");
        if (!CarpetaUsuarioJuegos.exists() || !CarpetaUsuarioJuegos.isDirectory()) {
            // Si la carpeta no existe, devolvemos una lista vacia
            return juegosDescargados;
        }
        
        try{
            File ArchivoJuego=new File("juegos.dat");
            if(ArchivoJuego.exists()){
                try(ObjectInputStream des=new ObjectInputStream(new FileInputStream(ArchivoJuego))){
                    ArrayList<Juego> TodoslosJuegos=(ArrayList<Juego>) des.readObject();                    
                    //aqui se filtran lode juegos descargados con foreach
                    for (Juego juego : TodoslosJuegos) {
                        File ArchivoDescargado=new File(CarpetaUsuarioJuegos,juego.getNombre() + ".dat");
                        if(ArchivoDescargado.exists()){
                            juegosDescargados.add(juego);
                        }
                    }
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "ERROR AL CAGAR LOS JUEGOS DECARGADOS "+e.getMessage());
        }
        return juegosDescargados;
       
    }

    private void mostrarInformacion(Juego juego) {
        String info = String.format(
                "Titulo: %s\nGenero: %s\nDesarrollador: %s\nFecha de Lanzamiento: %s\nRuta de Instalacion: %s",
                juego.getNombre(), juego.getGenero(), juego.getDesarrollador(), juego.getFechaLanzamiento(), juego.getRutaInstalacion());
        JOptionPane.showMessageDialog(this, info, "Informacion del Juego", JOptionPane.INFORMATION_MESSAGE);
    }

    private void descargarJuego(Juego juego) {
        if (juego.getRutaInstalacion()==null||juego.getRutaInstalacion().isEmpty()) {
            JOptionPane.showMessageDialog(this,"Ruta de instalacion no valida para este juego.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Obtener ruta base del proyecto
            String Rutabase = System.getProperty("user.dir");
            System.out.println("Ruta base: " + Rutabase);

            // Archivo original
            File archivoOriginal = new File(Rutabase + File.separator + "juegos.dat");
            System.out.println("Ruta archivo original: " + archivoOriginal.getAbsolutePath());
            if (!archivoOriginal.exists()) {
                JOptionPane.showMessageDialog(this,
                        "El archivo del juego no existe en: " + archivoOriginal.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Carpeta destino
            File carpetaDestino = new File(Rutabase + File.separator + "UsuariosGestion" + File.separator
                    + nombreUsuario + File.separator + juego.getRutaInstalacion());
            System.out.println("Ruta carpeta destino: " + carpetaDestino.getAbsolutePath());
            if (!carpetaDestino.exists()) {
                carpetaDestino.mkdirs(); // Crear la carpeta si no existe
            }

            // Archivo destino 
            archivoDestino = new File(carpetaDestino, juego.getNombre() + ".dat");
            System.out.println("Archivo destino: " + archivoDestino.getAbsolutePath());
            
             if (archivoDestino.exists()) {

                JOptionPane.showMessageDialog(null, "El juego ya se no podra decargar nuevamente");
                btnDescargar.setText("Ya descargado");
                btnDescargar.setEnabled(false);
                return;

            }

            // Copiar archivo
            Files.copy(archivoOriginal.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            JOptionPane.showMessageDialog(this,
                    "Juego descargado exitosamente en: " + archivoDestino.getAbsolutePath(), "Descarga Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al descargar el juego: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarJuego() throws IOException {
        String nombreJuego = JOptionPane.showInputDialog(this, "Ingrese el nombre del juego a eliminar:");
        if (nombreJuego == null || nombreJuego.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre valido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean eliminado = juegos.removeIf(juego -> juego.getNombre().equalsIgnoreCase(nombreJuego));
        if(eliminado){
            guardarJuegos();
            JOptionPane.showMessageDialog(this, "El juego ha sido eliminado exitosamente.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new Juegos_Steam(nombreUsuario).setVisible(true); // Recargar la ventana
        }else{
            JOptionPane.showMessageDialog(this, "No se encontro ningun juego con el nombre " + nombreJuego + ".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarJuegos() throws IOException {
        File archivoJuegos = new File("juegos.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoJuegos))) {
            oos.writeObject(juegos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar los juegos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
}
    

