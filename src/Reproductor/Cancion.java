/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import java.awt.Image;
import java.awt.image.BufferedImage;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;


import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.jaudiotagger.tag.FieldKey;

/**
 *
 * @author royum
 */
public class Cancion implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String Titulo;
    private String Artista;
    private String Album;
    private String Duracion;
    private byte[] Imagen; 
    private  String RutaArchivo;  

    public Cancion(String Titulo, String Artista, String Album, String Duracion, byte[] Imagen, String RutaArchivo) {
        this.Titulo = Titulo;
        this.Artista = Artista;
        this.Album = Album;
        this.Duracion = Duracion;
        this.Imagen = Imagen;
        this.RutaArchivo = RutaArchivo;
    }
    
    public void CargarMetaDatos()throws Exception{
        
        if(RutaArchivo==null||RutaArchivo.isEmpty()){
            
            throw new Exception("El archivo no existe o no es valido");
            
        }
        
        try{
            
            AudioFile audiofile=AudioFileIO.read(new File(RutaArchivo));
            Tag tag= audiofile.getTag();
            AudioHeader  header= audiofile.getAudioHeader();
            
            //informacion de los metadatos
            Titulo=tag!=null&&tag.getFirst(FieldKey.TITLE)!=null ? tag.getFirst(FieldKey.TITLE) : "Desconocido";
            Artista=tag!=null&&tag.getFirst(FieldKey.ARTIST)!=null ? tag.getFirst(FieldKey.ARTIST) : "Desconocido";
            Album=tag!=null&&tag.getFirst(FieldKey.ALBUM)!=null ? tag.getFirst(FieldKey.ALBUM) : "Desconocido";
            Duracion=CalcularDuracion(header.getTrackLength());
            
            //aqui se establece la imagen
            if(tag!=null&&tag.getFirstArtwork()!=null){
                Imagen= tag.getFirstArtwork().getBinaryData();
              
            }else{
                
                Imagen = CargarImagenPredeterminada();
                
            }
            
        }catch(Exception e){
            
            throw new Exception("Error al cargar los metadatos: "+e.getMessage());
            
        }
        
    }
    
    private String CalcularDuracion(int SegundosTotales){
        
        int minutos=SegundosTotales/60;
        int segundos=SegundosTotales%60;
        return String.format("%02d:%02d", minutos, segundos);
        
    }
    
    private byte[] CargarImagenPredeterminada()throws Exception {
        
        try{
            
            BufferedImage imagen=ImageIO.read(getClass().getResource("/img_repro/block.png"));
            ByteArrayOutputStream a=new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", a);
            return a.toByteArray();
            
            
        }catch(Exception e){
            
            throw new Exception ("ERROR AL CARGAR LA IMAGEN PREDETERMINADA : "+e.getMessage());
            
        }
        
    }
    
    public ImageIcon getImagenEscalada(int Ancho, int Alto){
        
        if(Imagen==null){
            
            return null;
            
        }
        
        try{
            
            BufferedImage img= ImageIO.read(new ByteArrayInputStream(Imagen));
            Image ImagenEscalada= img.getScaledInstance(Ancho, Alto, Image.SCALE_SMOOTH);
            return new ImageIcon(ImagenEscalada);
            
        }catch(Exception e){
            
            return null;
            
        }
        
    }
    
    

    public String getTitulo() {
        return Titulo;
    }

    public String getArtista() {
        return Artista;
    }

    public String getAlbum() {
        return Album;
    }

    public String getDuracion() {
        return Duracion;
    }

    public byte[] getImagen() {
        return Imagen;
    }

    public void setRutaArchivo(String RutaArchivo) {
        this.RutaArchivo = RutaArchivo;
    }

    public String getRutaArchivo() {
        return RutaArchivo;
    }

   

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public void setArtista(String Artista) {
        this.Artista = Artista;
    }

    public void setAlbum(String Album) {
        this.Album = Album;
    }

    public void setDuracion(String Duracion) {
        this.Duracion = Duracion;
    }

    public void setImagen(byte[] Imagen) {
        this.Imagen = Imagen;
    }

    
}
