/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;

/**
 *
 * @author royum
 */
public class ArchivosUsuarios {

     private TreeView<String> arbol;
    private File carpetaRaiz;
    private File directorioActual;
    private VBox panelArchivos;
    private String nombreUsuario;

    public ArchivosUsuarios(File carpetaUsuario, String nombre) {
        this.carpetaRaiz = carpetaUsuario; // Directorio del usuario logueado
        this.nombreUsuario = nombre;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("APP RoyXen -> Archivos de " + nombreUsuario);
        BorderPane root = new BorderPane();

        // Panel izquierdo: Árbol de directorios
        VBox panelIzquierdo = new VBox();
        panelIzquierdo.setPrefWidth(250);
        panelIzquierdo.setStyle("-fx-background-color: black;");

        // Nodo raíz del árbol
        TreeItem<String> rootNode = new TreeItem<>(carpetaRaiz.getName());
        arbol = new TreeView<>(rootNode);
        cargarDirectorio(carpetaRaiz, rootNode); // Cargar el árbol desde el directorio del usuario

        // Listener para selección de nodos
        arbol.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                directorioActual = null;
                return;
            }

            String rutaRelativa = getRutaDesdeNodo(newSelection).trim();
            File directorioSeleccionado = new File(carpetaRaiz, rutaRelativa);

            if (directorioSeleccionado.exists() && directorioSeleccionado.isDirectory()) {
                directorioActual = directorioSeleccionado;
                mostrarContenidoCarpeta(directorioActual);
            } else {
                directorioActual = null;
            }
        });

        ScrollPane scrollTree = new ScrollPane(arbol);
        panelIzquierdo.getChildren().add(scrollTree);
        root.setLeft(panelIzquierdo);

        // Panel central: Mostrar contenido de las carpetas
        panelArchivos = new VBox();
        panelArchivos.setSpacing(15);
        panelArchivos.setPadding(new Insets(10));
        panelArchivos.setStyle("-fx-background-color: darkgray;");
        ScrollPane scrollArchivos = new ScrollPane(panelArchivos);
        root.setCenter(scrollArchivos);

        // Panel superior: Botones
        HBox panelBotones = new HBox();
        panelBotones.setSpacing(10);
        panelBotones.setPadding(new Insets(10));
        panelBotones.setStyle("-fx-background-color: #2d2d2d;");

        Button volver = new Button("Volver");
        configurarBoton(volver, Color.RED);
        volver.setOnAction(e -> primaryStage.close());
        panelBotones.getChildren().add(volver);

        root.setTop(panelBotones);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void configurarBoton(Button boton, Color color) {
        boton.setTextFill(Color.WHITE);
        boton.setStyle("-fx-background-color: " + toRgbString(color) + "; -fx-focus-color: transparent;");
    }

    private void cargarDirectorio(File directorio, TreeItem<String> node) {
        if (directorio == null || !directorio.exists()) return;
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            Arrays.sort(archivos);
            for (File archivo : archivos) {
                TreeItem<String> childNode = new TreeItem<>(archivo.getName());
                node.getChildren().add(childNode);
                if (archivo.isDirectory()) cargarDirectorio(archivo, childNode);
            }
        }
    }

    private String getRutaDesdeNodo(TreeItem<String> node) {
        StringBuilder rutaRelativa = new StringBuilder();
        TreeItem<String> current = node;

        while (current.getParent() != null) {
            rutaRelativa.insert(0, current.getValue());
            current = current.getParent();
            if (current.getParent() != null) rutaRelativa.insert(0, File.separator);
        }

        return rutaRelativa.toString();
    }

    private void mostrarContenidoCarpeta(File carpeta) {
        directorioActual = carpeta;
        panelArchivos.getChildren().clear();
        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            Arrays.sort(archivos);
            for (File archivo : archivos) {
                Label label = new Label(archivo.getName());
                label.setTextAlignment(TextAlignment.CENTER);
                label.setTextFill(Color.WHITE);
                label.setStyle("-fx-background-color: #3a3a3a; -fx-padding: 5;");
                panelArchivos.getChildren().add(label);

                // Detectar si se hace clic en un archivo de audio
                label.setOnMouseClicked((MouseEvent event) -> {
                    if (archivo.isFile() && esArchivoDeAudio(archivo)) {
                        reproducirArchivo(archivo); // Reproducir el archivo de audio seleccionado
                    }
                });
            }
        }
    }

    private void reproducirArchivo(File archivo) {
        try {
            Media archivoSonido = new Media(archivo.toURI().toString());
            Musica musica = new Musica(archivoSonido); // Crear instancia de Musica con el nuevo archivo
            Musica.Play(); // Iniciar la reproducción automáticamente
        } catch (Exception e) {
            System.out.println("Error al reproducir el archivo de audio: " + e.getMessage());
        }
    }

    private boolean esArchivoDeAudio(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".mp3") || nombre.endsWith(".wav") || nombre.endsWith(".acc") || nombre.endsWith(".m4a");
    }

    private String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}