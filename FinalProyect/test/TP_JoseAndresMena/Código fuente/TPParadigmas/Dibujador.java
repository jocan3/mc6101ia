import java.awt.event.*;

/**
 * Clase que implementa un MouseListener que permite dibujar vocales en la matriz que representan los valores de entrada de la red neuronal
 * 
 * @author José Andrés Mena Arias 
 * @version 30/05/2012
 */
public class Dibujador implements MouseListener
{

    private Interfaz interfaz;
    private boolean mousePresionado;
    
    
    public Dibujador(Interfaz i){
        interfaz = i;
        mousePresionado = false;
    }
    
    public void setInterfaz(Interfaz i){
        interfaz = i;
    }


    public void mousePressed(MouseEvent e) {
      interfaz.cambiarColorBoton(e.getSource());            
      mousePresionado = true;
    }
    


    public void mouseReleased(MouseEvent e) {
          mousePresionado = false;
    }


    public void mouseEntered(MouseEvent e) {
        if (mousePresionado){
            interfaz.cambiarColorBoton(e.getSource());            
        }
    }


    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {

    }

}
