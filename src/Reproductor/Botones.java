/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

/**
 *
 * @author royum
 */
public class Botones extends HBox{
    
    private Button play, pause, stop;
    private Slider controladorVolumen;
    private Label etVolumen; 
    
    public Botones(){
        this.setAlignment(Pos.CENTER);//se alinean al centro
        play=new Button();
        pause=new Button();
        stop=new Button();
        EstiloBoton(play, "play");
        EstiloBoton(pause, "pause");
        EstiloBoton(stop, "stop");
        
        //de 0 a 100 y comienza en 50
        controladorVolumen=new Slider(0,100,50);
        controladorVolumen.setPrefWidth(120);  //a,de,ab,izq
        controladorVolumen.setPadding(new Insets(0,10,0,10));
        
        //etVolumen=new Label("Vol: 50%");
        
        PonerenAccion();
        
        MontarBotones();
    }
    
    private void MontarBotones() {
        //este nos devuelve una lista de todos los componentes que tenemos, todos los nodos 
        getChildren().addAll(stop,play,pause);
        
    }
                                          //nombre boton
    private void EstiloBoton(Button boton, String nombre){
        
        setMargin(boton, new Insets(20));
        
        boton.setMinHeight(80);
        boton.setMinWidth(80);
        boton.setStyle(//comando en css para obtener imagen y color
        "-fx-background-color: transparent; " + 
        "-fx-background-image: url('img_repro/" +nombre+ ".png');"+
        "-fx-background-size: cover;"
        );
        
        boton.setOnMousePressed(e-> boton.setOpacity(0.7));
        boton.setOnMouseReleased(e-> boton.setOpacity(1));
        
    }

    private void PonerenAccion() {
        
        
       play.setOnAction(e-> Musica.Play());
       pause.setOnAction(e-> Musica.pause());
       stop.setOnAction(e-> Musica.stop());
       
       
    }

    
        
    
}
