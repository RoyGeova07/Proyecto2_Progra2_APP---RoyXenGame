/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Base_De_Datos;

/**
 *
 * @author royum
 */
public interface ManejoDeDatos {
    
    public boolean RegistroUsuario(String nombre,String password,boolean esAdmin);
    public void GuardarUsuarios();
    public void CargarUsuarios();
    public void ListarUsuarios();
    
}
