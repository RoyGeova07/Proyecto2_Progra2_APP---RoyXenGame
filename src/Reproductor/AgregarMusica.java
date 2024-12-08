/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reproductor;

import RoyExceptions.MusicasDuplicadasException;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author royum
 */
public class AgregarMusica {
    
    private static final String RUTA_ARCHIVO_CANCIONES= "canciones.dat";
    
    public static void  SeleccionaryAgregarCancion(){
        
        JFileChooser AbrirArchivo = new JFileChooser();
        FileNameExtensionFilter filtroArchivos = new FileNameExtensionFilter("Archivos de musica (*.mp3)", "mp3");
        AbrirArchivo.setFileFilter(filtroArchivos);
        
        int Devolver=AbrirArchivo.showOpenDialog(null);
        if(Devolver==JFileChooser.APPROVE_OPTION){
            
            File ArchivoSeleccionado=AbrirArchivo.getSelectedFile();
            
            if(ArchivoSeleccionado.exists()){
                
                try{
                    
                    Cancion NuevaCancion=new Cancion("Desconocido","Desconocido","Desconocido","Desconocido",null,ArchivoSeleccionado.getAbsolutePath());
                    NuevaCancion.CargarMetaDatos();
                    
                    JOptionPane.showMessageDialog(null,
                            "Archivo de musica agregado:\n"
                            + "Titulo: " + NuevaCancion.getTitulo() + "\n"
                            + "Artista: " + NuevaCancion.getArtista() + "\n"
                            + "Album: " + NuevaCancion.getAlbum() + "\n"
                            + "Duracion: " + NuevaCancion.getDuracion()+ "\n"
                            + "Ruta Archivo: " +NuevaCancion.getRutaArchivo());
                    
                    GuardarCancionEnArchivo(NuevaCancion);
                    
                }catch(Exception e){
                    
                    JOptionPane.showMessageDialog(null, "El archivo seleccionado no existe","ERROR",JOptionPane.ERROR_MESSAGE);
                    
                }
                
            }else{
                
                JOptionPane.showMessageDialog(null, "El archivo seleccionado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                
            }
            
        }else{
            
            JOptionPane.showMessageDialog(null, "No se selecciono ningun archivo.");
            
        }
        
    }

    private static void GuardarCancionEnArchivo(Cancion NuevaCancion) {
        
        try{
            
            File ArchivoCanciones=new File(RUTA_ARCHIVO_CANCIONES);
            ArrayList<Cancion> ListaCanciones=new ArrayList<>();//SIN ESTA LISTA VACIA TODO SE VA FOC
            
            //aqui si el archivo existe, se cargaran las canciones existentes
            if(ArchivoCanciones.exists() && ArchivoCanciones.length() > 0){
                try(ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ArchivoCanciones))){
                    Object objetoLeido = entrada.readObject();
                    if(objetoLeido instanceof ArrayList){
                        ListaCanciones = (ArrayList<Cancion>) objetoLeido;
                    }else{
                        throw new IOException("El archivo no contiene una lista válida de canciones.");
                    }
                }catch(IOException | ClassNotFoundException e){
                    e.printStackTrace(); // Para ver detalles del error en la consola
                    JOptionPane.showMessageDialog(null,"Error al cargar canciones existentes: " + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
                    ListaCanciones = new ArrayList<>(); // Si falla,  se inicializ una nueva lista pta
                    return;
                }
            }
            
            //aqui se verifica que no haya duplicados
            for (Cancion cancioncita : ListaCanciones) {
                
                if(cancioncita.getTitulo().equalsIgnoreCase(NuevaCancion.getTitulo())&&cancioncita.getArtista().equalsIgnoreCase(NuevaCancion.getArtista())&&cancioncita.getAlbum().equalsIgnoreCase(NuevaCancion.getAlbum())){
                    
                    throw new MusicasDuplicadasException("La cancion ya esta en la lista");
                    
                }
                
            }

            ListaCanciones.add(NuevaCancion);
            
            try(ObjectOutputStream salida=new ObjectOutputStream(new FileOutputStream(ArchivoCanciones))){
                
                salida.writeObject(ListaCanciones);
                
            }
            
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null, "Error al guardar la nueva cancion: " + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
            
        }
        
    }
    
}
