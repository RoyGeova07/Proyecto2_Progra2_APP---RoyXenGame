/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Base_De_Datos;

import AreaChat.MensajeChat;
import Reproductor.Musica;
import Steam.Juego;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author royum
 */
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;//ID de serializacion unico
       private static final String FONDO_POR_DEFECTO = "/img_menuprin/h.gif"; 
    private String nombre;
    private String password;
    private boolean esAdmin;
    private ArrayList<Musica> bibliotecaMusical;
    private ArrayList<Juego> bibliotecaJuego;
    private ArrayList<MensajeChat> AreaChat;
    private String Fondopersonalizado;

    public Usuario(String nombre, String password, boolean esAdmin) throws IOException {
        this.nombre = nombre;
        this.password = password;
        this.esAdmin = esAdmin;
        this.bibliotecaMusical = new ArrayList<>();
        this.bibliotecaJuego = new ArrayList<>();
        this.AreaChat = new ArrayList<>();
        this.Fondopersonalizado=null;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public boolean EsAdmin() {
        return esAdmin;
    }
    
    public String getFondoPersonalizado(){
        
        return Fondopersonalizado!=null ? Fondopersonalizado: FONDO_POR_DEFECTO;
        
    }

    public ArrayList<Musica> getBibliotecaMusical() {
        return bibliotecaMusical;
    }

    public ArrayList<Juego> getBibliotecaJuego() {
        return bibliotecaJuego;
    }

    public ArrayList<MensajeChat> getAreaChat() {
        return AreaChat;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void recibirMensaje(MensajeChat mensaje) {
        this.AreaChat.add(mensaje);
    }

    public void setFondoPersonalizado(String rutaFondo) {
        this.Fondopersonalizado = rutaFondo;
    }

}
