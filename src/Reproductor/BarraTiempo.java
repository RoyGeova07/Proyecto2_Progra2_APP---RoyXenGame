/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;

/**
 *
 * @author royum
 */
public class BarraTiempo extends VBox{
    
    private static BarraTiempo objClase=new BarraTiempo();
    private static MediaPlayer cancion;//aqui guardamos la cancion
    private static Label etTiempo;
    private static Slider barra;
    
    private BarraTiempo(){
        etTiempo=new Label("00:00");
        //aqui le damos un margen
        VBox.setMargin(etTiempo, new Insets(0,15,0,0));
        //fuente mas grande 
        etTiempo.setFont(new Font(15));
        
        barra=new Slider();
        barra.setMin(0);
        barra.setMax(100);//
        barra.setValue(0);
        
        //aqui los alineamos al centro
        this.setAlignment(Pos.CENTER_RIGHT);
        getChildren().addAll(etTiempo,barra);
        
    }
    
      private static void EstablecerListeners(){
            //     Duracion de tiempo     cambiara                valor que tenia1    nuevo numero2 
            cancion.currentTimeProperty().addListener((observable,   oldValue ,      newValue)->{
                //aqui  guardar porcentaje, tambien en el parentesis obtendremos los segundos de la cancion
                double porcentaje=(newValue.toSeconds()/cancion.getTotalDuration().toSeconds()*100);//10 segundos de 100 totales
                barra.setValue(porcentaje);
                
                etTiempo.setText(formatoTiempo(newValue.toSeconds()));
            });//esto nos devuelve el tiempo que dur la cancion
            barra.valueProperty().addListener((observable,   oldValue ,      newValue)->{
               if(barra.isValueChanging()) {//aqui se verificca quien esta moviendo la barra si el usuario o la compu
                   cancion.seek(cancion.getTotalDuration().multiply(newValue.doubleValue()/100));//calcular para mover la barra y se sincronize con la musica
               }
            });
        }
      
      //aqui cambiar el valor de la etiqta  
      private static String formatoTiempo(double segundos){
          int minutos= (int) segundos/60;
          int seg=(int) segundos% 60; 
          return String.format("%02d:%02d", minutos,seg); //aqui formate el tiempo
          
      }
      
      //aqui establecer la cancion
      public static void setCancion(MediaPlayer cancion){
          BarraTiempo.cancion=cancion;
          EstablecerListeners();
      }
      
      //aqui metodo getter para acceder a la barra de tiempo
      public static BarraTiempo getBarra(){
          return objClase;
      }
    
}
