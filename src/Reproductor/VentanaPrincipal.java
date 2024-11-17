/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import javafx.scene.layout.BorderPane;

/**
 *
 * @author royum
 *///
public class VentanaPrincipal extends BorderPane{
    
    public VentanaPrincipal(){
        
        setTop(new BarraSuperior());
        setCenter(new InfoCancion());
        setBottom(new BarraInferior());
        
    }
    
    
}
