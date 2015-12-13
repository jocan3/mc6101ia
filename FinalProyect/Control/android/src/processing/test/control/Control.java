package processing.test.control;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import android.content.Intent; 
import android.os.Bundle; 
import ketai.net.bluetooth.*; 
import ketai.ui.*; 
import ketai.net.*; 
import java.io.File; 
import java.util.StringTokenizer; 
import java.util.ArrayList; 
import java.lang.Thread; 
import java.util.Random; 
import java.util.Calendar; 
import java.util.GregorianCalendar; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Control extends PApplet {

/* A simple example to controll RedBot using an Android device as Remote control 
@author: Jose Andres Mena
*/

//required for BT enabling on startup







final int DEFAULT_NUM_UNITS_INPUT_LAYER = 10;
final double DEFAULT_LEARNING_FACTOR = 1.1f;
final int DEFAULT_NUM_HIDDEN_LAYERS = 1;
final int DEFAULT_NUM_UNITS_HIDDEN_LAYERS = 5;
final int DEFAULT_UNITS_OUTPUT_LAYER = 10;
final double DEFAULT_TOLERANCE = 0.1f;

ControlP5 cp5;

int vScreenWidth;
int vScreenHeight;

KetaiBluetooth bt;

BPN brain;
BPNManager brainManager;

boolean useBrain = false;
boolean dance = false;

boolean isConfiguring = true;
boolean connected = false;
String info = "";
String connectionInfo = "";
KetaiList klist;
ArrayList devicesDiscovered = new ArrayList();
char valueToSend = 'u';

byte [] moves = {'u','d','l','r'};
int  slowIndex = 0;
int  mediumIndex = 0;
int fastIndex = 0;


int wait = 0;
int time = 0;

//********************************************************************
// The following code is required to enable bluetooth at startup.
//********************************************************************

public void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 bt = new KetaiBluetooth(this);
}

public void onActivityResult(int requestCode, int resultCode, Intent data) {
 bt.onActivityResult(requestCode, resultCode, data);
}

public void setup() {
 
 vScreenWidth = displayWidth;
 vScreenHeight = displayHeight;
 
 textSize(34);
 
 //start listening for BT connections
 bt.start();
 //at app start select device\u2026
 orientation(LANDSCAPE);
 background(0); 
 isConfiguring = true;
 //font size

 brain = new BPN(0, DEFAULT_LEARNING_FACTOR, DEFAULT_NUM_HIDDEN_LAYERS, DEFAULT_NUM_UNITS_INPUT_LAYER, DEFAULT_UNITS_OUTPUT_LAYER, DEFAULT_NUM_UNITS_HIDDEN_LAYERS, DEFAULT_TOLERANCE);
 brainManager = new BPNManager(brain);
 
 initUI();

}

public void initUI(){
  cp5 = new ControlP5(this);
 
  int wSize = (int)(vScreenWidth/6);
  int hSize = (int)(vScreenHeight/4);
       
  int middle = vScreenWidth/2;
  
  cp5.addButton("UP")
     .setPosition(4*wSize,0*hSize)
     .setSize(wSize,hSize)
     .activateBy(ControlP5.PRESSED)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(34);
     ;    

   cp5.addButton("LEFT")
     .setPosition(3*wSize,1*hSize)
     .setSize(wSize,hSize)
     .activateBy(ControlP5.PRESSED)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(34);
     ;    
  
    cp5.addButton("DOWN")
     .setPosition(4*wSize,2*hSize)
     .setSize(wSize,hSize)
     .activateBy(ControlP5.PRESSED)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(34);;
     ;    
  
   cp5.addButton("RIGHT")
     .setPosition(5*wSize,1*hSize)
     .setSize(wSize,hSize)
     .activateBy(ControlP5.PRESSED)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(34);
     ;     
     
      wSize = (int)(vScreenWidth/12);
      hSize = (int)(vScreenHeight/8);
     
     cp5.addButton("START_STOP")
     .setPosition(1*wSize,(3*hSize))
     .setSize(wSize,hSize)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
     ;
     
     cp5.addButton("NEURAL_NETWORK")
     .setPosition(3*wSize,3*hSize)
     .setSize(wSize,hSize)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
     ;  

     cp5.addButton("SLOW")
     .setPosition(0*wSize,5*hSize)
     .setSize(wSize,hSize)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
     ;
     
     cp5.addButton("MEDIUM")
     .setPosition(2*wSize,5*hSize)
     .setSize(wSize,hSize)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
     ;
     
     cp5.addButton("FAST")
     .setPosition(4*wSize,5*hSize)
     .setSize(wSize,hSize)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
     ;    

}

public void UP(){
   if (isConfiguring || !connected)
     return;
  info = "UP pressed";
  valueToSend = 'u';
}

public void DOWN(){
 if (isConfiguring || !connected)
     return;
  info = "DOWN pressed";
  valueToSend = 'd';
}

public void LEFT(){
 if (isConfiguring || !connected)
     return;
  info = "LEFT pressed";
  valueToSend = 'l';
}

public void RIGHT(){
 if (isConfiguring || !connected)
     return;
  info = "RIGHT pressed";
  valueToSend = 'r';
}

public void START_STOP(){
 if (isConfiguring)
     return;
   if (connected == true){
     bt.stop();
     connectionInfo = "Connection stopped";
     connected = false;
   }
   else{
     connectionInfo = "Connection started";
     cp5 = null;
     bt.start();
     isConfiguring=true;
     connected = true;
     initUI();     
     
   }

}


public void NEURAL_NETWORK(){
 if (isConfiguring)
     return;
   if (useBrain){
     useBrain = false;
   }
   else{
    useBrain = true;  
    dance = false;  
   }
}

public void activateDance(int w){
     if (dance){
     dance = false;
   }
   else{
    useBrain = false;  
    dance= true;
    time = millis();
    wait = w;
   }
}

public void FAST(){
 if (isConfiguring)
     return;
  activateDance(100);
  
}

public void SLOW(){
 if (isConfiguring)
     return;
  activateDance(1000);
}

public void MEDIUM(){
   if (isConfiguring)
     return;
  activateDance(500);
  
}




public void draw() {
 //at app start select device
 
 if (isConfiguring)
 {
  background(0);
  klist = new KetaiList(this, bt.getPairedDeviceNames());
  isConfiguring = false;
 }
 else
 {
     fill(0);
     background(255);
     text("Action: " + info,15 ,vScreenHeight - 50);
     text(connectionInfo,15 ,vScreenHeight - 15);
   
   if (useBrain){
     info = "Using neural network";
      byte[] result = brainManager.getMovesPattern(getRandomInputs(DEFAULT_NUM_UNITS_INPUT_LAYER));     
      bt.broadcast(result);
   }else if (dance){ 
     if (millis() - time > wait){
       byte[] result = {getRandomElement(moves)};   
       time = millis();
       bt.broadcast(result);  
     }
   }if (mousePressed){
       byte[] data = {(byte)valueToSend};
       bt.broadcast(data);
     }else{
       info = "";
     }
   }
}


public byte[] getRandomInputs(int numInputs){
  byte[] result = new byte[numInputs];
  for (int i = 0; i < numInputs; ++i){
    result[i] = (byte)(((int)random(2) == 1) ? '1' : '0');
  }
  return result;
}

public byte getRandomElement(byte[] list){
  return list[(int)random(list.length)];
}

public void onKetaiListSelection(KetaiList klist) {
 String selection = klist.getSelection();
 connectionInfo = "Connecting to " + selection + "." + str(bt.connectToDeviceByName(selection));
 //dispose of list for now
 connected = true;
 klist = null;
}

//Call back method to manage data received
public void onBluetoothDataEvent(String who, byte[] data) {
 if (isConfiguring)
 return;
 //received
 info += new String(data);
 //clean if string to long
 if(info.length() > 150)
 info = "";
}

// Arduino+Bluetooth+Processing 
// Arduino-Android Bluetooth communication




/**
 * Manages a neural network able to recognize patterns based on songs tempo/frecuency/strenght
 */
public class BPNManager
{

    private BPN redNeuronal;
    private int iteracionConverge;
    

    public BPNManager(BPN redN){
        redNeuronal = redN;
    }
    
    public void setIteracionConverge(int it){
        iteracionConverge = it;    
    }
    
    public void generarArchivoDePesos(File archivo){
        ArrayList<String> pesos = new ArrayList<String>();
        
        for (int k = 1; k < redNeuronal.getNumeroCapasOcultas() + 2; ++k){
            pesos.add("Pesos capa" + k);
             for (int i = 0; i < redNeuronal.getCapa(k).getTamano(); ++i){
                String pesosTemp = "";
                 for (int j = 0; j < redNeuronal.getCapa(k).getNumeroEntradas(); ++j){
                    pesosTemp+=redNeuronal.getCapa(k).getPesos()[i][j] + " ";                    
                }      
                pesos.add(pesosTemp);
                //System.out.println("Gener\u00f3 pesos en la fila " + i + " de la capa " + k);
             }                
        }
       // manejadorArchivos.escribirEnArchivo(archivo, pesos);        TODO
    }
    
    
    public void cargarArchivoDePesos(File archivo){
        ArrayList<String> st = null; //manejadorArchivos.leerDesdeArchivo(archivo);   TODO                     
        boolean archivoValido = true;
        int c = 0;
        try{
        for (int k = 1; k < redNeuronal.getNumeroCapasOcultas() + 2; ++k){
            ++c;            
             for (int i = 0; i < redNeuronal.getCapa(k).getTamano(); ++i){
                StringTokenizer stTemp = new StringTokenizer(st.get(c), " ");             
                for (int j = 0; j < redNeuronal.getCapa(k).getNumeroEntradas(); ++j){
                    redNeuronal.getCapa(k).getPesos()[i][j] = Double.parseDouble(stTemp.nextToken());
                }
                ++c;
             }                
        }
        }catch(Exception e){
            archivoValido = false; 
            //interfaz.decirMensaje("Archivo de pesos inv\u00e1lido");
        }                
    }
    
    
    public void setRedNeuronal(BPN rn){
        this.redNeuronal = rn;
    }
    
    
    public byte[] getMovesPattern (byte[] input){
        double [] inputVector = new double[input.length];
        for (int i = 0; i < input.length; ++i){
                inputVector[i] = input[i] == '1' ? 0.9f : 0.1f; 
        }
        
        redNeuronal.setEntradas(inputVector);
        redNeuronal.propagarHaciaAdelante();
        double [] salidas = redNeuronal.getSalidas();
        
        double limiteInferior = 0.9f-redNeuronal.getTolerancia();
        double limiteSuperior = 0.9f+redNeuronal.getTolerancia();
        
        byte [] output = new byte[salidas.length];
        
        byte [] values = {'u','d'};
        
        for (int i = 0; i < redNeuronal.getCantidadUnidadesSalida(); ++i){
          if (salidas[i] > 0.5f){
                 output[i] = values[i%2];
            }else{
              output[i] = 'x';
            }
            
        }
        
        return output;
    }
    
        
  /*  public void entrenarRedNeuronal(File archivoC){     
        final File archivoCasos = archivoC;
        interfaz.agregarAGrafico(0, true);
        
        try{
       (new Thread(){
        public void run(){
        interfaz.activarEtiquetaEspere(true); 
        interfaz.activarDesactivarComponentesParaEntrenar(false);
        boolean huboError = true;  
        int contadorIteraciones = 0;
        double promedioErrorIteraciones = 0;
        double promedioErrorIteracion = 0;       
        //File archivoSalida = interfaz.pedirArchivo(true);
        while (huboError){
            ++contadorIteraciones;
            ArrayList<String> st = manejadorArchivos.leerDesdeArchivo(archivoCasos);
                  //  System.out.println("1");
            int cantidadCasos = Integer.parseInt(st.get(0));
            huboError = false;            
            int cl = 1;
            int cantidadCasosSinAprender = 0;
            promedioErrorIteracion = 0;
            for (int k = 0; k < cantidadCasos; ++k){                    
                ++cl;                
                  //      System.out.println("2");
                double [] entrada = new double[redNeuronal.getCantidadUnidadesEntrada()];
                int c = 0;
                //System.out.println("3");
                for (int i = 0; i < CANTIDAD_FILAS; ++i){
                            
                    StringTokenizer stTemp = new StringTokenizer(st.get(cl), " ");
                    for (int j = 0; j < CANTIDAD_COLUMNAS; ++j){
                        String s = stTemp.nextToken();
                  //              System.out.println("4... iteracio " + k +" valor " + s);
                                
                        entrada[c] = Double.parseDouble(s);
                        ++c;                                        
                    }
                    ++cl;
                }
                
                    //    System.out.println("5");
                redNeuronal.setEntradas(entrada);
                      //  System.out.println("6");
                double [] salidasEsperadas = new double[redNeuronal.getCantidadUnidadesSalida()];
                        //System.out.println("7");
                StringTokenizer stTemp = new StringTokenizer(st.get(cl), " ");
                          //                  System.out.println("8");
                                            ++cl;
                for (int i = 0; i < redNeuronal.getCantidadUnidadesSalida(); ++i){

                    salidasEsperadas[i] = Double.parseDouble(stTemp.nextToken());
                }
                            //                System.out.println("9");
                redNeuronal.setSalidasEsperadas(salidasEsperadas);
                              //              System.out.println("10");
                redNeuronal.propagarHaciaAdelante();
                                //            System.out.println("11");
                redNeuronal.computarErrorSalida();
                                  //          System.out.println("12");
                promedioErrorIteracion+= redNeuronal.getPromedioErrores();
                if (redNeuronal.getError()){
                    huboError = true;
                                    //            System.out.println("13");
                    redNeuronal.propagarHaciaAtras();
                                      //          System.out.println("14");
                    redNeuronal.ajustarPesos();

                    ++cantidadCasosSinAprender;
                }                    
            }
                                        //       System.out.println("15");            
            promedioErrorIteracion = promedioErrorIteracion/cantidadCasos;
            promedioErrorIteraciones+=promedioErrorIteracion;
            if (contadorIteraciones%1 == 0){
                promedioErrorIteraciones = promedioErrorIteraciones/100;
                String mensajeError = "Iteraci\u00f3n "+contadorIteraciones+ ". Promedio error: " + promedioErrorIteraciones + ". Cantidad casos sin aprender: "+ cantidadCasosSinAprender;
                                   //                      System.out.println("16");
               // interfaz.setTextoErrores(mensajeError);
                interfaz.graficando = true;
                interfaz.agregarAGrafico(promedioErrorIteraciones, false);
                while (interfaz.graficando){}                
                try{
                Thread.currentThread().sleep(2000);
                }catch(Exception e){}
                promedioErrorIteraciones = 0;  
                //System.out.println(mensajeError);
                //this.generarArchivoDePesos(archivoSalida);
            }
      }
      setIteracionConverge(contadorIteraciones);
      interfaz.decirMensaje("La red convergi\u00f3 en la iteraci\u00f3n "+iteracionConverge+"\nPresione OK para guardar los pesos ");
      File archivo = interfaz.pedirArchivo(true);
      if (archivo != null){
        generarArchivoDePesos(archivo);
        interfaz.decirMensaje("Archivo de pesos generado correctamente");
      }
      interfaz.activarEtiquetaEspere(false); 
        interfaz.activarDesactivarComponentesParaEntrenar(true);
      }
      
      }).start();
      
      
      
                                    
      }catch(Exception e){
                                            //             System.out.println("17");
        interfaz.decirMensaje("Archivo de casos inv\u00e1lido");
      }
      
        
    }
    */
    
/*    public void comprobarLetra(){
        boolean [][] matriz = interfaz.getColor();
        double [] vectorEntrada = new double[redNeuronal.getCantidadUnidadesEntrada()];
        int c = 0;
        for (int i = 0; i < CANTIDAD_FILAS; ++i){
            for (int j = 0; j < CANTIDAD_COLUMNAS; ++j){
                vectorEntrada[c] = matriz[i][j] ? 0.9 : 0.1;
                ++c;
            }            
        }
        
        redNeuronal.setEntradas(vectorEntrada);
        redNeuronal.propagarHaciaAdelante();
        double [] salidas = redNeuronal.getSalidas();
        
        double limiteInferior = 0.9-redNeuronal.getTolerancia();
        double limiteSuperior = 0.9+redNeuronal.getTolerancia();
        
        for (int i = 0; i < redNeuronal.getCantidadUnidadesSalida(); ++i){
            if (salidas[i] > limiteInferior && salidas[i] < limiteSuperior){
                interfaz.setColorEtiquetaVocal(true,i);
                interfaz.setTextoCampoValorVocal(salidas[i], i);
            }else{
                interfaz.setColorEtiquetaVocal(false,i);
                interfaz.setTextoCampoValorVocal(salidas[i], i);
            }
            
        }
                
    
    }
   */ 
    
    
    
    

}   
    



/**
 * Implementa una Red Neuronal de Propagacion hacia Atras (Back Propagation Network), sus estructuras y los metodos para su funcionamiento general
 * @author Andres Mena
 * @version 2012
 
 */
public class BPN
{
    private Capa unidadesEntrada;
    private Capa unidadesSalida;
    private Capa[] capas;
    private double alfa;
    private double eta;
    private int numeroCapasOcultas;
    private int cantidadUnidadesEntrada;
    private int cantidadUnidadesSalida;
    private int cantidadUnidadesCapaOculta;
    private double [] salidasEsperadas;
    private double tolerancia;
    private boolean error;
    private double [] vectorSalidas;
    private double promedioErrores;
    
    public BPN(double alfa, double eta, int numeroCapasOcultas, int cantidadUnidadesEntrada, int cantidadUnidadesSalida, int cantidadUnidadesCapaOculta, double tol){
        vectorSalidas=new double [cantidadUnidadesSalida];
        
        this.error=false;
        this.tolerancia=tol;
        this.alfa=alfa;
        this.eta=eta;
        this.numeroCapasOcultas=numeroCapasOcultas;
        this.cantidadUnidadesEntrada=cantidadUnidadesEntrada+1;
        this.cantidadUnidadesSalida=cantidadUnidadesSalida;
        this.cantidadUnidadesCapaOculta=cantidadUnidadesCapaOculta+1;
        this.promedioErrores = 0;
        
        capas=new Capa[numeroCapasOcultas+2];
        capas[0]= unidadesEntrada = new Capa(this.cantidadUnidadesEntrada,1);
               
        for(int i=1;i<numeroCapasOcultas+1;++i){
            capas[i]=new Capa(this.cantidadUnidadesCapaOculta,capas[i-1].getTamano());
        }
        
        capas[numeroCapasOcultas+1]= unidadesSalida= new Capa(this.cantidadUnidadesSalida,this.cantidadUnidadesCapaOculta);
        salidasEsperadas = new double [this.cantidadUnidadesSalida];
        
        //Se ajustan pesos aleatoriamente cada vez que se crea una nueva red
        Calendar c = new GregorianCalendar();
        Random aleatorio = new Random(c.get(Calendar.SECOND));
        int signo= aleatorio.nextInt(2);
        
        double peso = 0;
        //empieza de 1 porque la primera capa no tiene pesos
        for(int k=1;k<numeroCapasOcultas+2;++k){
            for(int i=0; i<capas[k].getTamano();++i){
                for(int j=0; j<capas[k-1].getTamano();++j){
                    signo= aleatorio.nextInt(2);
                    peso=aleatorio.nextDouble();                    
                    peso = 0.3f*peso;
                    if(signo==1){
                    peso=-1*peso;
                    }
                    capas[k].getDelta()[i][j]=capas[k].getPesos()[i][j]=peso;
                }
            }
        }
    }

    public void setAlfa(double valor){
            alfa=valor;
    }
    
    public void setEta(double valor){
        eta=valor;
    }
    
    public void setNumeroCapasOcultas(int valor){
        numeroCapasOcultas=valor;
    }
    
    public boolean getError(){
        return error;
    }
    
    public double getTolerancia(){
        return tolerancia;
    }
    
    public double getPromedioErrores(){
        return promedioErrores;
    }
    
    
    public int getCantidadUnidadesEntrada(){
        return cantidadUnidadesEntrada-1;
    }
    
    public int getCantidadUnidadesSalida(){
        return cantidadUnidadesSalida;
    }
    
    public Capa getUnidadesEntrada(){
        return unidadesEntrada;
    }
    
    public Capa getUnidadesSalida(){
        return unidadesSalida;
    }
    
    public Capa getCapa(int indice){
        return capas[indice];
    } 
    
    public double getAlfa(){
        return alfa;
    }
    
    public double getEta(){
        return eta;
    }
    
    public int getNumeroCapasOcultas(){
        return numeroCapasOcultas;
    }

    public void setSalidasEsperadas(double[] valor){
        for(int i=0;i<cantidadUnidadesSalida;++i){
            salidasEsperadas[i]=valor[i];
        }
    }
    
   public void setEntradas(double[] entradas){
        unidadesEntrada.setSalidas(0,1);
        for(int i=1;i<cantidadUnidadesEntrada;++i){
            unidadesEntrada.setSalidas(i,entradas[i-1]);
        }    
    }
    
    public void propagarCapa(Capa inferior, Capa superior){
        double [] entradas;
        double [] actual;
        double [][] conexiones;
        double suma;
        
        entradas=inferior.getSalidas();
        actual=superior.getSalidas();
        conexiones=superior.getPesos();
        
        for(int i=0; i < superior.getTamano();++i){
            suma=0;
            for(int j=0; j<inferior.getTamano();++j){
                suma=suma+ entradas[j] * conexiones[i][j];
            }
            actual[i]=1.0f/(1.0f + Math.exp(-suma));

        
        }
        
        
        
        
    }
    
    public void propagarHaciaAdelante(){
        Capa superior;
        Capa inferior;
        
        for(int i=0;i<capas.length-1;++i){
            capas[i].setSalidas(0,1);
            inferior=capas[i];
            superior=capas[i+1];
            propagarCapa(inferior,superior);            
        }
    }
    
   public double[] getSalidas(){
        for(int i=0;i<cantidadUnidadesSalida;++i){
            vectorSalidas[i]=unidadesSalida.getSalidas(i);
        }
        return vectorSalidas;
   }
    
   public void computarErrorSalida(){
        double [] errores = unidadesSalida.getErrores();
        double [] salidas = unidadesSalida.getSalidas();
        double limiteSuperior=0.0f;
        double limiteInferior=0.0f;
        error=false;
        promedioErrores = 0;
        for(int i =0; i< cantidadUnidadesSalida; ++i){
            limiteSuperior=salidasEsperadas[i]+tolerancia;
            limiteInferior=salidasEsperadas[i]-tolerancia;
            
            if(salidas[i]>limiteSuperior || salidas[i]<limiteInferior){
                error=true;
            }
            errores[i]= salidas[i]*(1-salidas[i])*(salidasEsperadas[i]-salidas[i]);
            

            double errorAbs = salidasEsperadas[i]-salidas[i];

            errorAbs = (errorAbs >= 0) ? errorAbs : (-1*errorAbs);
            promedioErrores+= errorAbs;
            
        }
        
        promedioErrores = promedioErrores/cantidadUnidadesSalida;
    }
    
    public void propagacionHaciaAtrasError(Capa inferior, Capa superior){
        double [] emisor = superior.getErrores(); 
        double [] receptor = inferior.getErrores();
        double [][] conexiones=superior.getPesos();
        double unidad;
        for(int i=0; i<inferior.getTamano();++i){
            receptor[i]=0;
            for(int j=0; j<superior.getTamano();++j){

                
                receptor[i] = receptor[i]+ emisor[j]*conexiones[j][i];
            }

            unidad = inferior.getSalidas(i);
            receptor[i]= receptor[i]* unidad * (1-unidad);

            
        }
    }
    
    
    public void propagarHaciaAtras(){
        Capa superior;
        Capa inferior;
        
        for(int i=capas.length-1;i>1;--i){
            inferior=capas[i-1];
            superior=capas[i];
            propagacionHaciaAtrasError(inferior,superior);
        }
    }
    
    public void ajustarPesos(){
        Capa actual;
        double [] entradas;
        double [] unidades;
        double [][] pesos;
        double [][] delta;
        double [] error;
        
        for(int i= 1; i<capas.length;++i){

            actual = capas[i];
            unidades = capas[i].getSalidas();
            entradas = capas[i-1].getSalidas();
            pesos=actual.getPesos();
            delta=actual.getDelta();
            error=capas[i].getErrores();
            for(int j=0;j<unidades.length;++j){

                for(int k=0; k<capas[i-1].getTamano();++k){

                    double pesosAntes = pesos[j][k]; 
                    pesos[j][k]=pesos[j][k]+(entradas[k]*eta*error[j]);// + (alfa*delta[j][k]);
                    delta[j][k]= pesos[j][k]-pesosAntes;
                }
            }
        }
    }

    /**
     * Subclase que implementa una capa de una red neuronal de propagacion hacia atr\ufffds
     * @author Andres Mena
     * @author Hermes Mora
     * @version 2012
     
     */
      public class Capa{
          private double [] salidas;
          private double [] errores;
          private double [][] pesos;
          private double [][] last_delta;//no sabemos
          private int tamano;
          private int numeroEntradas;
          
          public Capa(int t, int numeroE){
                numeroEntradas=numeroE;
                tamano=t;
                salidas=new double[t];
                errores=new double[t];
                pesos=new double [t][numeroE];
                last_delta = new double [t][numeroE];
            }
          
          public int getTamano(){
            return tamano;  
          }
            
         public int getNumeroEntradas(){
            return numeroEntradas;  
         }
        
          public void setSalidas(int indice, double valor){
              salidas [indice]=valor;
          }
    
          public double getSalidas(int indice){
            return salidas[indice];
          }
          
           public double [] getSalidas(){
            return salidas;
          }
          
           public double [][] getPesos(){
            return pesos;
          }
          
           public double [][] getDelta(){
            return last_delta;
          }
          
            public double [] getErrores(){
            return errores;
          }
        
        }
}
  public void settings() {  size(displayWidth, displayHeight); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Control" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
