/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AreaChat;

import Base_De_Datos.ManejoUsuarios;
import Base_De_Datos.Usuario;
import Pantallas_Principales.MenuPrincipal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author royum
 */
public class Discord extends JFrame{
    
    private static final String CARPETA_HISTORIALES = "Historiales_De_ChatsUsuarios"; // Carpeta principal para historiales.
    private static final String ARCHIVO_CHAT = "historial_chat.dat";//variable para el chat en general.
    private  String usuarioEnSesion;
    private JPanel panelMensajes;
    private JTextField campoMensaje;
    private File archivosHistorial;//variable para el historial, del usuario unico
    private File archivosUSUARIO;//variable para abrir el menuPrincipal

    public Discord(String usuarioEnSesion) throws IOException {
        this.usuarioEnSesion = usuarioEnSesion;
        
        //si la carpeta no existe se crea
        File CarpetasHistorial=new File(CARPETA_HISTORIALES);
        if(!CarpetasHistorial.exists()){
            CarpetasHistorial.mkdir();
        }
        
        //aqui se inicializara el archivo del usuario logueado
        this.archivosHistorial=new File(CarpetasHistorial,usuarioEnSesion+ "_historial.dat");
        configurarVentana();
        CargarMensajes();
    }

    private void configurarVentana() {
        setTitle("APP RoyXen -> Discord del Usuario "+usuarioEnSesion);
        setSize(800,750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        
        //aqui panel de mensajes
        panelMensajes=new JPanel();
        panelMensajes.setLayout(new BoxLayout(panelMensajes,BoxLayout.Y_AXIS));
        panelMensajes.setBackground(Color.WHITE);
        JScrollPane scrollPane=new JScrollPane(panelMensajes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Mensajes"));
        add(scrollPane,BorderLayout.CENTER);
        
        //aqui el panelInferior para escribir
        JPanel Panelinferior=new JPanel(new BorderLayout());
        campoMensaje=new JTextField();
        JButton botonEnviar=new JButton("Enviar");
        JButton botonVolver=new JButton("Volver");
        JButton botonHistorial=new JButton("Ver tu historial");
        
        //aqui las acciones 
        botonEnviar.addActionListener(e-> {
            try {
                EnviarMensaje();
            } catch (IOException ex) {
                Logger.getLogger(Discord.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        botonVolver.addActionListener(e-> volverAlMenu());
        
        botonHistorial.addActionListener(e-> {
            try {
                mostrarHistorial();
            } catch (IOException ex) {
                Logger.getLogger(Discord.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        //aqui se agregan al panel
        Panelinferior.add(campoMensaje,BorderLayout.CENTER);
        JPanel panelBotones=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(botonVolver);
        panelBotones.add(botonHistorial);
        panelBotones.add(botonEnviar);
        Panelinferior.add(panelBotones,BorderLayout.EAST);
        add(Panelinferior,BorderLayout.SOUTH);
        
        setVisible(true);
        
        
    }
    
    private void EnviarMensaje() throws IOException{
        
        String texto=campoMensaje.getText().trim();
        if(!texto.isEmpty()){
            
            MensajeChat mensaje=new MensajeChat(texto,usuarioEnSesion);
            
            GuardarMensajeArchivoIndividual(mensaje);
            GuardarMensajeArchivoGeneral(mensaje);
            AgregarMensajePanel(mensaje);
            
            campoMensaje.setText("");
            
        }
        
    }
    
    private void AgregarMensajePanel(MensajeChat mensaje){
        
        JLabel Etiquetamensaje=new JLabel(mensaje.toString());
        Etiquetamensaje.setOpaque(true);
        
        if(mensaje.getRemitente().equals(usuarioEnSesion)){
            
            Etiquetamensaje.setBackground(new Color(173, 216, 230)); // Color azul claro
            Etiquetamensaje.setHorizontalAlignment(SwingConstants.RIGHT);
           
        }else{
            
            Etiquetamensaje.setBackground(Color.LIGHT_GRAY);
            Etiquetamensaje.setHorizontalAlignment(SwingConstants.LEFT);
            
        }
        
        Etiquetamensaje.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelMensajes.add(Etiquetamensaje);
        panelMensajes.revalidate();
        panelMensajes.repaint();
        
    }
    
    private void CargarMensajes() throws IOException{
        
        if(!new File(ARCHIVO_CHAT).exists()){
            new File(ARCHIVO_CHAT).createNewFile();//crea un archivo si no existe
        }
        
        try(RandomAccessFile cargar=new RandomAccessFile(ARCHIVO_CHAT,"r")){
            
            String linea;
            while((linea=cargar.readLine())!=null){
                
                String[] datos=linea.split("::",2);//:: para el separados
                if(datos.length==2){
                    
                    String remitente=datos[0];
                    String mensaje=datos[1];
                    AgregarMensajePanel(new MensajeChat(mensaje,remitente));
                    
                }
                
            }
            
        }catch(IOException e){
            
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS CHATS");
            
        }
        
    }
    
    //se guarda el archivo
    private void GuardarMensajeArchivoGeneral(MensajeChat mensaje) throws IOException{
        
        try(RandomAccessFile guardar=new RandomAccessFile(ARCHIVO_CHAT,"rw")){
            guardar.seek(guardar.length());//vamos al final del archivo
            guardar.writeBytes(mensaje.getRemitente()+ "::"+mensaje.getMensaje()+"\n");
            
        }catch(IOException e){
            
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR EL MENSAJE EN EL CHAT GENERAL");
            
        }
        
    }
    
    private void GuardarMensajeArchivoIndividual(MensajeChat mensaje) throws IOException{
        
        try(RandomAccessFile individual= new RandomAccessFile(archivosHistorial,"rw")){
            
            individual.seek(individual.length());
            individual.writeBytes(mensaje.getRemitente()+ "::"+mensaje.getMensaje()+"\n");
            
        }catch(IOException e){
            
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR EL HISTORIAL ");
            
        }
        
    }
    
    private void mostrarHistorial() throws IOException{
        
        JFrame ventanaHistorial=new JFrame("Historal de mensajes de "+usuarioEnSesion);
        ventanaHistorial.setSize(450,650);
        ventanaHistorial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaHistorial.setLayout(new BorderLayout());

        JTextArea AreaHistorial=new JTextArea();
        AreaHistorial.setEditable(false);
        AreaHistorial.setFont(new Font("Monospaced",Font.PLAIN,12));
        JScrollPane ScrollHistorial=new JScrollPane(AreaHistorial);
        ventanaHistorial.add(ScrollHistorial,BorderLayout.CENTER);
        
        if(!archivosHistorial.exists()){
            archivosHistorial.createNewFile();//si no existe se crea
        }
        
        try(RandomAccessFile mostrar=new RandomAccessFile(archivosHistorial,"r")){
            
            String linea;
            while((linea=mostrar.readLine())!=null){
                
                AreaHistorial.append(linea+ "\n");
                
            }
            
            
        }catch(IOException e){
            
            AreaHistorial.setText("No se pudo cargar el historial");
            
        }
        
        ventanaHistorial.setVisible(true);
    }
    
    
       

    private void volverAlMenu() {
        dispose(); // Cerrar la ventana actual
        MenuPrincipal m =new MenuPrincipal(usuarioEnSesion,archivosUSUARIO);
        m.setVisible(true);
    }

   
}