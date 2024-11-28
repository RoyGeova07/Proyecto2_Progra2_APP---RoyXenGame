/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Steam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import com.toedter.calendar.JDateChooser;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

/**
 *
 * @author royum
 */
public class Agregar_Juegos extends JFrame {

    private JTextField txtTitulo;
    private JComboBox<Generos_Juegos> cbGenero;
    private JTextField txtDesarrollador;
    private JDateChooser dcFechaLanzamiento;
    private JTextField txtRutaInstalacion;
    private JLabel lblImagen;
    private File imagenSeleccionada;
    private String nombreusuario;
    private File archivo;

    private static final String RUTA_ARCHIVO_JUEGOS = "juegos.dat";

    public Agregar_Juegos() {
        setTitle("Agregar Juegos");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Configurar layout
        setLayout(new GridLayout(7, 2, 10, 10));

        // Crear y añadir campos de texto
        add(new JLabel("Titulo del Juego:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Género:"));
        cbGenero = new JComboBox<>(Generos_Juegos.values());
        add(cbGenero);

        add(new JLabel("Desarrollador:"));
        txtDesarrollador = new JTextField();
        add(txtDesarrollador);

        add(new JLabel("Fecha de Lanzamiento:"));
        dcFechaLanzamiento = new JDateChooser();
        dcFechaLanzamiento.setDateFormatString("dd/MM/yyyy");
        add(dcFechaLanzamiento);

        add(new JLabel("Ruta de Instalación:"));
        txtRutaInstalacion = new JTextField();
        add(txtRutaInstalacion);

        // Selector de imagen
        lblImagen = new JLabel("Seleccione una imagen", SwingConstants.CENTER);
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblImagen.setOpaque(true);
        lblImagen.setBackground(Color.LIGHT_GRAY);
        lblImagen.setPreferredSize(new Dimension(200, 200));
        lblImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                seleccionarImagen();
            }
        });
        add(new JLabel("Imagen del Juego:"));
        add(lblImagen);

        
        JButton btnAgregar = new JButton("Agregar Juego");
        btnAgregar.addActionListener(this::agregarJuego);
        add(btnAgregar);

       
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);

        setVisible(true);
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String ext = getExtension(f);
                return f.isDirectory() || ext.equals("png") || ext.equals("jpg");
            }

            @Override
            public String getDescription() {
                return "Imágenes (PNG, JPG)";
            }

            private String getExtension(File file) {
                String name = file.getName();
                int lastDot = name.lastIndexOf('.');
                return lastDot > 0 ? name.substring(lastDot + 1).toLowerCase() : "";
            }
        });

        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            imagenSeleccionada = fileChooser.getSelectedFile();
            lblImagen.setText(imagenSeleccionada.getName());
        }
    }

    private void agregarJuego(ActionEvent e) {
        try {
            // Validar campos
            if (txtTitulo.getText().isEmpty()
                    || cbGenero.getSelectedItem() == null
                    || txtDesarrollador.getText().isEmpty()
                    || dcFechaLanzamiento.getDate() == null
                    || txtRutaInstalacion.getText().isEmpty()
                    || imagenSeleccionada == null) {
                throw new Exception("Por favor, complete todos los campos y seleccione una imagen válida.");
            }

           
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaLanzamiento = sdf.format(dcFechaLanzamiento.getDate());

            
            byte[] caratulaBytes = Files.readAllBytes(imagenSeleccionada.toPath());

            // Crear juego
            Juego nuevoJuego = new Juego(
                    txtTitulo.getText(),
                    (Generos_Juegos) cbGenero.getSelectedItem(),
                    txtDesarrollador.getText(),
                    fechaLanzamiento,
                    txtRutaInstalacion.getText(),
                    caratulaBytes
            );

            // Guardar el juego en el archivo binario
            guardarJuegoEnArchivo(nuevoJuego);

            JOptionPane.showMessageDialog(this, "Juego agregado exitosamente.");
            MenuSteam m=new MenuSteam(nombreusuario,archivo);
            m.setVisible(true);
            dispose(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarJuegoEnArchivo(Juego juego) {
        try {
            File archivo = new File(RUTA_ARCHIVO_JUEGOS);
            ArrayList<Juego> juegos = new ArrayList<>();

            
            if (archivo.exists() && archivo.length() > 0) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                    juegos = (ArrayList<Juego>) ois.readObject();
                } catch (EOFException eof) {
                    // EOFException ocurre si el archivo esta vacio, ignoramos en este caso.
                    System.out.println("Archivo vacio. Se inicializará una nueva lista de juegos.");
                }
            }

            juegos.add(juego); // Agregar el nuevo juego

            // Guardar la lista actualizada de juegos
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                oos.writeObject(juegos);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
