/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import java.io.File;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author royum
 */
public class ArchivosUsuarios {
 private String nombreUsuario; // Nombre del usuario logueado
    private File userFolder; // Carpeta del usuario

    // Constructor que recibe el nombre del usuario
    public ArchivosUsuarios(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        inicializarDirectorioUsuario(); // Crear el directorio si no existe
    }

    private void inicializarDirectorioUsuario() {
        userFolder = new File(System.getProperty("user.dir") + File.separator + nombreUsuario);
        if (!userFolder.exists()) {
            userFolder.mkdirs(); // Crear el directorio si no existe
        }
    }

    public void abrirFileChooserYReproducir(Stage stage) {
        FileChooser fileChooser = new FileChooser();

      
        fileChooser.setInitialDirectory(userFolder);

        // Filtro para archivos de la musica
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Música", "*.mp3", "*.wav"));

        //aqui abre el dialogo de seleccion de archivos
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);

        //aqui Reproducir el archivo seleccionado automaticamente
        if (archivoSeleccionado != null) {
            reproducirMusica(archivoSeleccionado);
        } else {
            System.out.println("No se selecciono ningin archivo.");
        }
    }

    private void reproducirMusica(File archivo) {
        try {
            Media archivoSonido = new Media(archivo.toURI().toString());
            new Musica(archivoSonido); // Clase encargada de manejar la reproduccion
        } catch (Exception e) {
            System.out.println("Error al reproducir el archivo: " + e.getMessage());
        }
    }
}
