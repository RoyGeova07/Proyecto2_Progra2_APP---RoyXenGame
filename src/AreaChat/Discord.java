    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AreaChat;

import Base_De_Datos.Usuario;
import Pantallas_Principales.MenuPrincipal;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author royum
 */
public class Discord extends JFrame{
    
        
    private static final String ARCHIVO_CHAT = "historial_chat.dat";//variable para el chat en general.
    private  String usuarioEnSesion;
    private JPanel panelMensajes;
    private JTextField campoMensaje;
    private File archivosHistorial;//variable para el historial, del usuario unico
    private File archivosUSUARIO;//variable para abrir el menuPrincipal     
    private Usuario user;
      private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private Thread listenerThread;
    

    public Discord(String usuarioEnSesion) throws IOException {
        this.usuarioEnSesion = usuarioEnSesion;

        File CarpetasHistorial = new File("UsuariosGestion" + File.separator + usuarioEnSesion + File.separator + "MiChatHistorial");

        if (!CarpetasHistorial.exists() || !CarpetasHistorial.isDirectory()) {
            throw new IOException("La carpeta 'MiChatHistorial' no existe para el usuario: " + usuarioEnSesion);
        }

        //aqui se inicializara el archivo del usuario logueado
        this.archivosHistorial = new File(CarpetasHistorial, usuarioEnSesion + "_historial.dat");

        if (!archivosHistorial.exists()) {
            archivosHistorial.createNewFile(); // Crear el archivo si no existe
        }

        configurarConexionTiempoReal();
        configurarVentana();
        CargarMensajes();
    }

    private void configurarVentana() {
        setTitle("APP RoyXen -> Discord del Usuario "+usuarioEnSesion);
        setSize(900,800);
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
        
        JButton botonEnviar = crearBoton("Enviar", "/img_Discord/enviar.png");
        JButton botonVolver = crearBoton("Volver", "/img_Discord/volver.png");
        JButton botonHistorial = crearBoton("Historial", "/img_Discord/historial.png");
        JButton botonChatPrivado=crearBoton("Chats Privados","/img_Discord/wa.png");
        
        //aqui las acciones 
        botonEnviar.addActionListener(e-> {
            try {
                EnviarMensaje();
            } catch (IOException ex) {
                Logger.getLogger(Discord.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        botonVolver.addActionListener(e-> {
            try {
                volverAlMenu();
            } catch (IOException ex) {
                Logger.getLogger(Discord.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        botonHistorial.addActionListener(e-> {
            try {
                mostrarHistorial();
            } catch (IOException ex) {
                Logger.getLogger(Discord.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        botonChatPrivado.addActionListener(e->{
            
            try {
                dispose();
                GUIPrivados priv=new GUIPrivados(usuarioEnSesion);
                priv.setVisible(true);
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
        panelBotones.add(botonChatPrivado);
        Panelinferior.add(panelBotones,BorderLayout.EAST);
        add(Panelinferior,BorderLayout.SOUTH);
        
        setVisible(true);
        
        
    }
    
    private JButton crearBoton(String texto, String rutaIcono) {
        JButton boton = new JButton(texto);

        // Cargar el icono.
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
            Image img = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Tamaño del icono.
            boton.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + rutaIcono);
        }

        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setFont(new Font("Consolas", Font.PLAIN, 12));
        boton.setPreferredSize(new Dimension(100, 100));

        // Transparencia en reposo.
        boton.setContentAreaFilled(false); // Quitar el fondo en reposo.
        boton.setOpaque(false);           // Garantizar la transparencia.
        boton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50))); // Borde transparente claro.

        // Cambiar el color al hacer clic.
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(true); // Fondo visible al hacer clic.
                boton.setBackground(new Color(200, 200, 200, 100)); // Color suave al presionar.
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(false); // Regresar a transparente.
            }
        });

        boton.setForeground(Color.black); // Mantener el texto blanco para visibilidad.

        return boton;
    }
    
    private void EnviarMensaje() throws IOException{
            
        String texto=campoMensaje.getText().trim();
        if(!texto.isEmpty()){
            
            MensajeChat mensaje=new MensajeChat(texto,usuarioEnSesion);
            //se entra al servidor
            salida.writeObject(mensaje);
            salida.flush();
            
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
                
                String[] datos=linea.split("::", 3);//:: para el separados
                if(datos.length==3){
                    String timestamp=datos[0];
                    String remitente=datos[1];
                    String mensaje=datos[2];
                    AgregarMensajePanel(new MensajeChat(mensaje,remitente,timestamp));
                    
                }
                
            }
            
        }catch(IOException e){
            
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS CHATS");
            
        }
        
    }
    
    //se guarda el archivo
    private void GuardarMensajeArchivoGeneral(MensajeChat mensaje) throws IOException {

        try (RandomAccessFile guardar = new RandomAccessFile(ARCHIVO_CHAT, "rw")) {
            guardar.seek(guardar.length());//vamos al final del archivo
            guardar.writeBytes(mensaje.getTimestamp() + "::" + mensaje.getRemitente() + "::" + mensaje.getMensaje() + "\n");

        }catch (IOException e) {

            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR EL MENSAJE EN EL CHAT GENERAL");

        }

    }
    
    private void GuardarMensajeArchivoIndividual(MensajeChat mensaje) throws IOException{
        
        try(RandomAccessFile individual= new RandomAccessFile(archivosHistorial,"rw")){
            
            individual.seek(individual.length());
            individual.writeBytes(mensaje.getTimestamp()+ "::"+mensaje.getRemitente()+ "::"+mensaje.getMensaje()+"\n");
            
        }catch(IOException e){
            
            JOptionPane.showMessageDialog(null, "ERROR AL GUARDAR EL HISTORIAL ");
            
        }
        
    }
    
    private void mostrarHistorial() throws IOException{
        
        JFrame ventanaHistorial=new JFrame("APP RoyXen -> Historal de mensajes de "+usuarioEnSesion);
        ventanaHistorial.setSize(450,650);
        ventanaHistorial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaHistorial.setLayout(new BorderLayout());
        ventanaHistorial.setLocationRelativeTo(null);
        
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
    
    private void volverAlMenu() throws IOException {
        dispose(); // Cerrar la ventana actual
        MenuPrincipal m =new MenuPrincipal( usuarioEnSesion,archivosUSUARIO);
        m.setVisible(true);
    }

    
      private void configurarConexionTiempoReal() {
        try {
            socket = new Socket("localhost", 12345);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            listenerThread = new Thread(() -> {
                try {
                    while (true) {
                        MensajeChat mensaje = (MensajeChat) entrada.readObject();
                        SwingUtilities.invokeLater(() -> AgregarMensajePanel(mensaje));
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Conexión cerrada: " + e.getMessage());
                }
            });
            listenerThread.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error conectándose al servidor de chat.");
        }
    }
      
      
   
}