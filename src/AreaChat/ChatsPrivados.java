/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AreaChat;

import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


/**
 *
 * @author royum
 */
public class ChatsPrivados {

    private final String CarpetasChatsPrivados = "ChatsPrivados";
    private final SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ChatsPrivados() {

        File Carpeta=new File(CarpetasChatsPrivados);
        if(!Carpeta.exists()){
            
            Carpeta.mkdir();
            
        }
        
        
    }
    
    private File ArchivoConversacion(String usuario1,String usuario2){
        
        if(usuario1.equalsIgnoreCase(usuario2)){
            
            throw new IllegalArgumentException("Los nombres de usuario no pueden ser iguales");
            
        }
        
        //aqui se ordenaran alfabeticamente los usuarios para evitar duplicados
        String[] usuarios={usuario1,usuario2};
        Arrays.sort(usuarios);
        
        String Archivo=usuarios[0]+"_"+usuarios[1]+ ".dat";
        return new File(CarpetasChatsPrivados,Archivo);
        
        
    }
    
    public void GuardarMensajes(String usuario1, String usuario2, String mensaje) throws IOException{
        
        if(mensaje==null||mensaje.trim().isEmpty()) return; 
        
        File Archivo=ArchivoConversacion(usuario1,usuario2); 
        String fecha=FormatoFecha.format(new Date());
        String Entrada= "[" +fecha+ "]" +usuario1+ ": "+mensaje + System.lineSeparator();
        
        try(RandomAccessFile Guardar=new RandomAccessFile(Archivo,"rw")){
            
            Guardar.seek(Guardar.length());//aqui el putero se movera al final del archivo
            Guardar.write(Entrada.getBytes());
            System.out.println("Mensaje guardado: " + Entrada); // Debug
        }catch(IOException e){
            
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR EL MENSAJE "+e.getMessage());
            
        }
           
    }
    
    public ArrayList<String> CargarMensajes(String usuario1,String usuario2) throws IOException{
        
        ArrayList<String> Mensajes=new ArrayList<>();
        File Archivo=ArchivoConversacion(usuario1,usuario2);
        
        if(!Archivo.exists()){
             System.out.println("Archivo no encontrado: " + Archivo.getAbsolutePath());
            return Mensajes;//aqui si no existe el archivo, regresara una lista vacia

        }

        try (BufferedReader leer = new BufferedReader(new FileReader(Archivo))) {
            String linea;
            while ((linea = leer.readLine()) != null) {

                Mensajes.add(linea);

            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los mensajes: " + e.getMessage());
        }
        System.out.println("Mensajes cargados: " + Mensajes); 
        return Mensajes;

    }
    
    
    public ArrayList<String> ObtenerConversaciones(String usuario){
        
        ArrayList<String> Conversaciones=new ArrayList<>();
        
        File Carpeta=new File(CarpetasChatsPrivados);
        
        File[] Archivos=Carpeta.listFiles();
        
        if(Archivos!=null){
            
            for (File Archivo : Archivos) {
                
                String NombreArchivito=Archivo.getName();
                if(NombreArchivito.contains(usuario)){
                    
                    Conversaciones.add(NombreArchivito.replace(".dat", ""));
                    
                }
                
            }
            
        }   
        return Conversaciones;
        
    }


}
