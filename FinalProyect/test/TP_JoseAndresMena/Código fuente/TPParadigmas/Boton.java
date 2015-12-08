import javax.swing.JButton;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.awt.*;
import java.net.URL;

/**
 * Define una subclase de JButton guardando su ubicaci�n en t�rminos de fila y columna
 * @author Jose Andres Mena Arias 
 * @version 16 de Octubre, 2009
 */
class Boton extends JButton {
    private Point posicion;
    private ImageIcon imagen;
    
    /**
     * Constructor que define el bot�n caracteriz�ndolo por su fila y columna
     * @param laFila el valor m�nimo puede ser cero
     * @param laColumna el valor m�nimo puede ser cero
     */
    public Boton(int laFila, int laColumna) {
        posicion = new Point(laColumna, laFila);
    }
    
    /**
     * Devuelve la fila y la columna del bot�n encapsulados en un objeto de tipo Point
     * @return la posici�n del bot�n, en donde el atributo X corresponde a la columna y el atributo Y corresponde a la fila
     */
    public Point getPosicion() {
        return posicion;
    }
    /**
     * @return la fila del bot�n
     */
    public double getFila() {
        return posicion.getY();
    }
    /**
     * @return la columna del bot�n
     */
    public double getColumna() {
        return posicion.getX();
    }
    
   
}
