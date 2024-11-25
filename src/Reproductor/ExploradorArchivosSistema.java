/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author royum
 */
public class ExploradorArchivosSistema {
    
    //stage nuevo escenario
    private static final Stage escenario;
    private static final FileChooser explorador;
        
    static {
        escenario=new Stage();
        explorador=new FileChooser();
        explorador.setTitle("Selecciona cancion");
        ExtensionFilter filtro=new ExtensionFilter("Audios","*wav", "*.mp3", "*.acc","*.m4a");
        explorador.getExtensionFilters().add(filtro);//este nos devuelve una lista  
    }
    
    public static void SeleccionarArchivo(){
        try{    
            
        File archivo = explorador.showOpenDialog(escenario);//este nos devuelve un file 
        Media archivoSonido=new Media(archivo.toURI().toString());
        new Musica(archivoSonido);
            
        }catch(NullPointerException e){
            System.out.println("No has seleccinado ninguna musica ");
            
        }
        
    }
}
