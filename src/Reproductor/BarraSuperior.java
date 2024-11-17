/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 *
 * @author royum
 */
public class BarraSuperior extends MenuBar { //aqui barra vacia

    private Menu menuArchivo,menuVolver;
    private MenuItem iAbrir, iSalir,iVolver;

    public BarraSuperior() {

        menuArchivo = new Menu("Archivo");
        menuVolver=new Menu("Volver");
        iAbrir = new MenuItem("Abrir");
        iSalir = new MenuItem("Salir");
        iVolver = new MenuItem("Volver al Menú Principal");
        prepararListener();
        prepararMenus();

    }

    private void prepararMenus() {
        //este nos devuelve una lista de todos los items
        menuArchivo.getItems().addAll(iAbrir, iSalir);
        
        menuVolver.getItems().add(iVolver);
        
        this.getMenus().addAll(menuArchivo,menuVolver);

    }

    private void prepararListener() {

        iAbrir.setOnAction(e -> ExploradorArchivos.SeleccionarArchivo());
        iSalir.setOnAction(e -> Reproductor_Musica.getStage().close());// aqui cerrara la ventana y terminar la ejecucion/
        
        iVolver.setOnAction(e-> {
            
            
        });

    }

}
