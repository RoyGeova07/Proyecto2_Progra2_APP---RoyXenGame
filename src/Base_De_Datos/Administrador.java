/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Base_De_Datos;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author royum
 */
public final class Administrador extends Usuario {
    
    private ArrayList<Usuario> listaUsuarios;
    private static final long serialVersionUID = 1L;

    // Constructor
    public Administrador(String nombre, String password) throws IOException {
        super(nombre, password, true);
        this.listaUsuarios = new ArrayList<>();
    }



    public void ListarUsuarios() {
        if (listaUsuarios.isEmpty()) {
            
            JOptionPane.showMessageDialog(null, "No hay Usuarios","Informacion",JOptionPane.INFORMATION_MESSAGE);
            return;
        } 
        
        String[] Columnas={"Nombre Usuario","Tipo (Admin/Normal)"};
        Object[][] Datos=new Object[listaUsuarios.size()][2];
        
        for (int lista = 0; lista < listaUsuarios.size(); lista++) {
            
            Usuario usu=listaUsuarios.get(lista);
            Datos[lista][0]=usu.getNombre();
            Datos[lista][1]=usu.EsAdmin() ? "Admin" : "Normal";
            
        }
        
        JTable Tablitausu=new JTable(Datos,Columnas);
        Tablitausu.setEnabled(false);
        JScrollPane scroll=new JScrollPane(Tablitausu);
        
        JFrame frame=new JFrame("Usuarios Registrados");
        frame.add(scroll);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//sirve para solo cerrar esta ventana
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void setListaUsuarios(ArrayList<Usuario> usuarios) {
        this.listaUsuarios = usuarios;
    }
    
}
