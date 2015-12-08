import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Clase que controla la creación de objetos y el manejo de eventos involucrados en el entrenamiento y uso de una red
 * neuronal que aprende a reconocer las vocales
 * 
 * @author Andres Mena 
 * @version 28/05/2012
 */
public class Controlador implements ActionListener
{
    private BPN redNeuronal;
    private BPNManager administradorRedNeuronal;
    private Interfaz interfaz;
    private ManejadorArchivos manejadorArchivos;
    private Dibujador dibujador;
    
    private final int CANTIDAD_FILAS_MATRIZ = 20;
    private final int CANTIDAD_COLUMNAS_MATRIZ = 20;
    private final double DEFAULT_FACTOR_APRENDIZAJE = 1.1;
    private final int DEFAULT_CANTIDAD_UNIDADES_CAPA_OCULTA = 200;
    private final int DEFAULT_UNIDADES_SALIDA = 10;
    private final double DEFAULT_TOLERANCIA = 0.1;
    
    private String imagenFondo = "";
    private String [][] menu = {{"Acciones","Entrenar Red", "Probar Red", "Salir"}, {"Ayuda","Acerca de", "Créditos"}};
    private String textoComandoBotones = "boton";
    private int anchoMarcoVentana = 750;
    private int altoMarcoVentana = 500;
    
    public Controlador(){
        redNeuronal = new BPN(0,DEFAULT_FACTOR_APRENDIZAJE,1,CANTIDAD_FILAS_MATRIZ*CANTIDAD_COLUMNAS_MATRIZ,DEFAULT_UNIDADES_SALIDA, DEFAULT_CANTIDAD_UNIDADES_CAPA_OCULTA, DEFAULT_TOLERANCIA);    
        dibujador = new Dibujador(null);
        interfaz = new Interfaz(this, anchoMarcoVentana, altoMarcoVentana, menu, imagenFondo, CANTIDAD_FILAS_MATRIZ, CANTIDAD_COLUMNAS_MATRIZ, textoComandoBotones, dibujador);
        dibujador.setInterfaz(interfaz);
        manejadorArchivos = new ManejadorArchivos();
        administradorRedNeuronal = new BPNManager(redNeuronal, CANTIDAD_FILAS_MATRIZ, CANTIDAD_COLUMNAS_MATRIZ, manejadorArchivos, interfaz);
    }
    
    
    public static void main(String args[]){
        Controlador controlador = new Controlador();
        controlador.iniciar();
    }
        
    public void iniciar(){
        interfaz.activar(true);
    }
    
    public void actionPerformed(ActionEvent evento){
        if (evento.getActionCommand().equals("Salir"))
            System.exit(0);
        else if (evento.getActionCommand().equals("Acerca de")) 
            interfaz.decirMensaje("TP1 Paradigmas");
        else if (evento.getActionCommand().equals("Créditos")) 
            interfaz.decirMensaje("Programador: Andres Mena");
        else if (evento.getActionCommand().equals("boton")) {
            //Point posicionBotonPresionado = interfaz.darPosicionBoton(evento.getSource());
            //interfaz.activarBoton(posicionBotonPresionado, false);      // EJEMPLO DE USO
        }
        else if (evento.getActionCommand().equals("Probar Red")) {
            interfaz.quitarPaneles();
            interfaz.agregarPanelUsar();
            interfaz.activar(true);
            //Point posicionBotonPresionado = interfaz.darPosicionBoton(evento.getSource());
            //interfaz.activarBoton(posicionBotonPresionado, false);      // EJEMPLO DE USO
        }
    
        else if (evento.getActionCommand().equals("Entrenar Red")) {
            interfaz.quitarPaneles();
            interfaz.agregarPanelEntrenar();
            interfaz.activar(true);
            //Point posicionBotonPresionado = interfaz.darPosicionBoton(evento.getSource());
            //interfaz.activarBoton(posicionBotonPresionado, false);      // EJEMPLO DE USO
        }
        
        else if (evento.getActionCommand().equals("prueba")) {
            File archivo = interfaz.pedirArchivo(true);
            if (archivo != null){
                administradorRedNeuronal.generarArchivoDePesos(archivo);
                interfaz.decirMensaje("Archivo de pesos generado");            
            }
            //administradorRedNeuronal.cargarArchivoDePesos(interfaz.pedirArchivo(false));
            //interfaz.decirMensaje("Archivo de pesos cargado");
        }
        
        else if (evento.getActionCommand().equals("Cargar pesos")) {
            //administradorRedNeuronal.generarArchivoDePesos(interfaz.pedirArchivo(true));
            //interfaz.decirMensaje("Archivo de pesos generado");            
            File archivo = interfaz.pedirArchivo(false);
            if (archivo != null){
                administradorRedNeuronal.cargarArchivoDePesos(archivo);
                interfaz.decirMensaje("Archivo de pesos cargado");
            }
        }
        
        else if (evento.getActionCommand().equals("Cargar archivo")) {           
            File archivo = interfaz.pedirArchivo(false);
            if (archivo != null){
                interfaz.setArchivoCasos(archivo);
                interfaz.setCampoArchivoCasos(archivo.getName());
            }
        }
        
        else if (evento.getActionCommand().equals("Boton")) {                       
            interfaz.cambiarColorBoton(evento.getSource());            
        }
        
        else if (evento.getActionCommand().equals("Entrenar")) {           
            Object [] datos = interfaz.getDatosEntrenamiento();
            boolean valido = true;            
            try{
               redNeuronal = new BPN(0,Double.parseDouble(datos[0].toString()),1,CANTIDAD_FILAS_MATRIZ*CANTIDAD_COLUMNAS_MATRIZ,DEFAULT_UNIDADES_SALIDA, Integer.parseInt(datos[2].toString()), Double.parseDouble(datos[1].toString()));                
               administradorRedNeuronal.setRedNeuronal(redNeuronal);
            }catch(Exception e){
                valido = false;
                interfaz.decirMensaje("Verifique que todos los campos estén llenos\ny con el formato correcto");
            }
            if (valido){
               
               administradorRedNeuronal.entrenarRedNeuronal((File)datos[3]);
               
            }            
        }
        
        else if (evento.getActionCommand().equals("Comprobar letra")) {                       
            administradorRedNeuronal.comprobarLetra();            
        }
        
        else if (evento.getActionCommand().equals("Cargar caso")) {                       
            File archivo = interfaz.pedirArchivo(false);
            try{
            if (archivo != null){
                ArrayList<String> casoS = new ArrayList<String>(); 
                casoS = manejadorArchivos.leerDesdeArchivo(archivo);
                int c = 1;
                boolean [][] casoB = new boolean[CANTIDAD_FILAS_MATRIZ][CANTIDAD_COLUMNAS_MATRIZ];
                for (int i = 0; i < CANTIDAD_FILAS_MATRIZ; ++i){                    
                    StringTokenizer st = new StringTokenizer(casoS.get(c), " ");
                    for (int j = 0; j < CANTIDAD_COLUMNAS_MATRIZ; ++j){
                        if(Double.parseDouble(st.nextToken()) == 0.9){
                            casoB[i][j] = true;
                        }else{
                            casoB[i][j] = false;
                        }                        
                    }
                    ++c;
                }                                
                interfaz.cargarCaso(casoB);
            }            
            }catch(Exception e){
                interfaz.decirMensaje("Archivo de caso inválido");
            }
            
        }
        
        else if (evento.getActionCommand().equals("Limpiar matriz")) {                       
            interfaz.limpiarMatriz();            
        }  
        /*
        else if (evento.getActionCommand().equals("crear caso")) {                       
            boolean [][] matriz = interfaz.getColor();
            ArrayList<String> caso = new ArrayList<String>();
            caso.add("Casoa");
            for (int i = 0; i < CANTIDAD_FILAS_MATRIZ; ++i){                    
                    String sTemp = "";
                    for (int j = 0; j < CANTIDAD_COLUMNAS_MATRIZ; ++j){
                        if (matriz[i][j]){
                            sTemp += "0.9 ";
                        }else{
                            sTemp += "0.1 ";
                        }
                    }
                    caso.add(sTemp);
            }
            caso.add("0.1 0.1 0.1 0.1 0.1 0.9 0.1 0.1 0.1 0.1 ");
            
            manejadorArchivos.escribirEnArchivo(interfaz.pedirArchivo(true), caso);
            
        }
       */ 
       
       else if (evento.getActionCommand().equals("crear caso")) {                       
            File archivoCasos = interfaz.pedirArchivo(true);
            ArrayList<String> casos = new ArrayList<String>();
            String salida = "";
            for (int i = 1; i <= 20; ++i){
                for (int j = 1; j <= 10; ++j){
                    File archivo = null;
                    if (i%5 != 0){
                    switch (j){
                        case 1:
                            archivo = new File("casoA"+i+".txt");
                            salida = "0.9 0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.1 ";
                        break;
                        case 2:
                            archivo = new File("casoE"+i+".txt");
                            salida = "0.1 0.9 0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.1 ";
                        break;
                        case 3:
                            archivo = new File("casoI"+i+".txt");
                            salida = "0.1 0.1 0.9 0.1 0.1 0.1 0.1 0.1 0.1 0.1 ";
                        break;
                        case 4: 
                            archivo = new File("casoO"+i+".txt");
                            salida = "0.1 0.1 0.1 0.9 0.1 0.1 0.1 0.1 0.1 0.1 ";
                        break;
                        case 5:
                            archivo = new File("casoU"+i+".txt");
                            salida = "0.1 0.1 0.1 0.1 0.9 0.1 0.1 0.1 0.1 0.1 ";
                        break;                        
                        case 6:
                            archivo = new File("casoam"+i+".txt");
                            salida = "0.1 0.1 0.1 0.1 0.1 0.9 0.1 0.1 0.1 0.1 ";
                        break;
                        case 7:
                            archivo = new File("casoem"+i+".txt");
                            salida = "0.1 0.1 0.1 0.1 0.1 0.1 0.9 0.1 0.1 0.1 ";
                        break;
                        case 8:
                            archivo = new File("casoim"+i+".txt");
                            salida = "0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.9 0.1 0.1 ";
                        break;
                        case 9: 
                            archivo = new File("casoom"+i+".txt");
                            salida = "0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.9 0.1 ";
                        break;
                        case 10:
                            archivo = new File("casoum"+i+".txt");
                            salida = "0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.1 0.9 ";
                        break;
                        
                        
                    }
                    ArrayList<String> lista = manejadorArchivos.leerDesdeArchivo(archivo);
                    for (int k = 0; k < lista.size()-1; ++k){
                        casos.add(lista.get(k));
                    }
                    lista.add(salida);
                    casos.add(lista.get(lista.size()-1));
                    }
                    
                } 
            }
            manejadorArchivos.escribirEnArchivo(archivoCasos, casos);
            
            
        }
        
        
        
                                   
    }

}
