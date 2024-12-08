/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author royum
 */
public class InfoCancion extends VBox{
    
    private static Label etTitulo, etArtista,etAlbum,etDuracion,etRuta;
    private static ImageView imagenCaratula;
    
    public InfoCancion(){
        
        etTitulo=new Label("Titulo: No disponible.");
        etArtista=new Label("Artista: No disponible.");
        etAlbum=new Label("Album: No disponible.");
        etDuracion=new Label("Duracion: No dispnible.");
        etRuta=new Label("Ruta: No disponible.");
        imagenCaratula=new ImageView();
        
        etTitulo.setFont(new Font(18));
        etArtista.setFont(new Font(18));
        etAlbum.setFont(new Font(18));
        etDuracion.setFont(new Font(18));
        etRuta.setFont(new Font(18));
        
        imagenCaratula.setFitWidth(150);
        imagenCaratula.setFitHeight(150);
        
        imagenCaratula.setPreserveRatio(true);//conserve el ratio de aspecto de la imagen
        
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        this.setSpacing(10);
        
        getChildren().addAll(imagenCaratula,etTitulo,etArtista,etAlbum,etDuracion,etRuta);
        
    }
    
    //funcion qur cuando elijamso una cancion los datos cambien
    public static void ActualizarInformacion(String titulo,String artista,String album,String Duracion,String RutaArchivo, Image caratula){
        
        //si no tiene metada datos saldra el mensaje no disponible
        etTitulo.setText("Titulo: "+(titulo !=null ? titulo: "No disponible"));
        etArtista.setText("Artista: "+(artista !=null ? artista: "No disponible"));
        etAlbum.setText("Album: "+(album !=null ? album: "No disponible"));
        etDuracion.setText("Duracion: "+(Duracion !=null ? Duracion: "No disponible"));
        etRuta.setText("Ruta: "+(RutaArchivo !=null ? RutaArchivo: "No disponible"));
        
        // si la musica no tiene imagen, se le pone una por defecto
        imagenCaratula.setImage(caratula !=null ? caratula : new Image("img_repro/block.png"));
        
    }
    
    //ESTA FUNCION ES CLAVEEEE
    public static boolean DosCamposDispnibles() {
        
    int numero=0;

    if (!etTitulo.getText().contains("No disponible")) numero++;
    if (!etArtista.getText().contains("No disponible")) numero++;
    if (!etAlbum.getText().contains("No disponible")) numero++;
    if (!etDuracion.getText().contains("No disponible")) numero++;
    if (!etRuta.getText().contains("No disponible")) numero++;

    return numero >= 2; // Devuelve true si al menos dos campos tienen informacion
    
    }

    
}
