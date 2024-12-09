/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Steam;

import java.io.Serializable;

/**
 *
 * @author royum
 */
public class Juego implements Serializable {

    private static final long serialVersionUID = 1L; // Recomendado para clases serializables

    private String nombre;
    private Enum Genero_Juegos;
    private String Desarrollador;
    private String FechaLanzamiento;
    private String rutaInstalacion;
    private int descargas = 0;
    private byte[] caratula;

    public Juego(String nombre, Generos_Juegos generoJuegos, String desarrollador, String fechaLanzamiento, String rutaInstalacion, byte[] caratula) {
        this.nombre = nombre;
        this.Genero_Juegos = generoJuegos;
        this.Desarrollador = desarrollador;
        this.FechaLanzamiento = fechaLanzamiento;
        this.rutaInstalacion = rutaInstalacion;
        this.caratula = caratula;
    }

    public void sumarDescargas(int puntos) {
        if (puntos > 0) {
            this.descargas += puntos;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Enum getGenero() {
        return Genero_Juegos;
    }

    public String getDesarrollador() {
        return Desarrollador;
    }

    public String getFechaLanzamiento() {
        return FechaLanzamiento;
    }

    public String getRutaInstalacion() {
        return rutaInstalacion;
    }

    public byte[] getCaratula() {
        return caratula;
    }


}
