/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Base_De_Datos;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author royum
 */
public  class Administrador extends Usuario {
    
    private ArrayList<Usuario> listaUsuarios;
    private static final long serialVersionUID = 1L;

    // Constructor
    public Administrador(String nombre, String password) throws IOException {
        super(nombre, password, true);
        this.listaUsuarios = new ArrayList<>();
    }

  
    public void agregarUsuario(Usuario usuario) {
        listaUsuarios.add(usuario);
    }

    public void eliminarUsuario(String nombre) {
        listaUsuarios.removeIf(u -> u.getNombre().equalsIgnoreCase(nombre));
    }

    public void listarUsuarios() {
        System.out.println("Lista de Usuarios:");
        listaUsuarios.forEach(u -> System.out.println(u.getNombre()));
    }

    
}
