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
    private String nombre;
    private String password;
    private boolean esAdmin;
    private ArrayList<Musica> bibliotecaMusical;
    private ArrayList<Juego> bibliotecaJuego;
    private ArrayList<MensajeChat> AreaChat;

    public Usuario(String nombre, String password, boolean esAdmin) throws IOException {
        this.nombre = nombre;
        this.password = password;
        this.esAdmin = esAdmin;
        this.bibliotecaMusical = new ArrayList<>();
        this.bibliotecaJuego = new ArrayList<>();
        this.AreaChat = new ArrayList<>();
        crearCarpetas();

    }

    private void crearCarpetas() {
        
        File carpetaPrincipal=new File(nombre);

        File CarpetaMusica = new File(carpetaPrincipal, "Musica");
        File CarpetaJuegos = new File(carpetaPrincipal, "Juegos");

        if(!carpetaPrincipal.exists()){
            carpetaPrincipal.mkdir();
        }
        
        if (!CarpetaMusica.exists()) {
            CarpetaMusica.mkdir();
        }
        if (!CarpetaJuegos.exists()) {
            CarpetaJuegos.mkdir();
        }

    }

    public void ListaMusicas() {
        String mensaje;
        mensaje = "Biblioteca Musical";
        bibliotecaMusical.forEach(System.out::println);

    }


    public void listarJuegos() {
        System.out.println("Biblioteca de Juegos:");
        bibliotecaJuego.forEach(System.out::println);
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

    public ArrayList<Musica> getBibliotecaMusical() {
        return bibliotecaMusical;
    }

    public ArrayList<Juego> getBibliotecaJuego() {
        return bibliotecaJuego;
    }

    public ArrayList<MensajeChat> getAreaChat() {
        return AreaChat;
    }

}
