/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AreaChat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author royum
 */
public class MensajeChat implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String mensaje;
    private String remitente;
    private String Timestamp;

    public MensajeChat(String mensaje, String remitente) {
        this.mensaje = mensaje;
        this.remitente = remitente;
        this.Timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    
    public MensajeChat(String mensaje, String remitente, String timestamp) {
        this.mensaje = mensaje;
        this.remitente = remitente;
        this.Timestamp = timestamp;
    }

    public String getMensaje() {
        return mensaje;
    }


    public String getNombre() {
        return mensaje;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getTimestamp() {
        return Timestamp;
    }
    
    public String toString() {
        return "[" + Timestamp + "] " + remitente + ": " + mensaje;
    }
    
    
    
}
