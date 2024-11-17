/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import javafx.scene.layout.VBox;

/**
 *
 * @author royum
 */
public class BarraInferior extends VBox {
    
    public BarraInferior(){
        
        getChildren().addAll(BarraTiempo.getBarra(),new Botones());//
        
    }
    
}
