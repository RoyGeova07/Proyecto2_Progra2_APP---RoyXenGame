/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import Pantallas_Principales.MenuPrincipal;
import java.io.File;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author royum
 */
public class BarraSuperior extends MenuBar {
    private Menu menuArvchivoSistema, menuArchivoUsuario;
    private MenuItem iAbrir, iSalir, AbrirMusicasUsuario,reiniciar;
    private String nombre;
    private MenuPrincipal menuPrincipal; // Agrega esta referencia

    public BarraSuperior(String nombreUsuario, MenuPrincipal menuPrincipal) {
        this.nombre = nombreUsuario;
        this.menuPrincipal = menuPrincipal; // Inicializa la referencia

        menuArvchivoSistema = new Menu("Archivos Computadora");
        menuArchivoUsuario = new Menu("Archivos Usuario " + nombre);
        iAbrir = new MenuItem("Abrir");
        iSalir = new MenuItem("Salir");
        reiniciar=new MenuItem("reiniciar");
        AbrirMusicasUsuario = new MenuItem("Abrir musicas de " + nombre);
        prepararListener();
        prepararMenus();
    }
    
    private void prepararMenus() {

        menuArvchivoSistema.getItems().addAll(iAbrir, iSalir);

        menuArchivoUsuario.getItems().addAll(AbrirMusicasUsuario, reiniciar);

        this.getMenus().addAll(menuArvchivoSistema, menuArchivoUsuario);
    }

    private void prepararListener() {
        try {
            iAbrir.setOnAction(e -> ExploradorArchivosSistema.SeleccionarArchivo());
            iSalir.setOnAction(e -> Reproductor_Musica.getStage().close());

            reiniciar.setOnAction(e -> {
                // Usar la referencia de menuPrincipal para acceder al método
                menuPrincipal.cargarReproductorMusica(menuPrincipal);
            });
            
            AbrirMusicasUsuario.setOnAction((e-> {
                
                File directorioUsuario=new File(System.getProperty("user.dir"));
                ArchivosUsuarios archivosUsuarios=new ArchivosUsuarios(directorioUsuario,nombre);
                
                Stage stage=new Stage();
                archivosUsuarios.start(stage);
                
            }));
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al abrir un explorador" + e.getMessage());
        }
    }

   
}