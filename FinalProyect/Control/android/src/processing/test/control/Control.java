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
import ketai.sensors.*; 
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








final int DEFAULT_NUM_UNITS_INPUT_LAYER = 1024;
final double DEFAULT_LEARNING_FACTOR = 0.5f;
final int DEFAULT_NUM_HIDDEN_LAYERS = 1;
final int DEFAULT_NUM_UNITS_HIDDEN_LAYERS = 512;
final int DEFAULT_UNITS_OUTPUT_LAYER = 1;
final double DEFAULT_TOLERANCE = 0.3f;

final int INTESITY_THRESHOLD = 400000;

ControlP5 cp5;

int vScreenWidth;
int vScreenHeight;
int wSize;
int hSize;

KetaiBluetooth bt;
KetaiAudioInput mic;
short[] data;
double[] BPNInput;
int sumPeaks;

FFT fourier;

BPN brain;
BPNManager brainManager;

boolean useBrain = false;
boolean dance = false;
boolean training = false;

boolean trainTap = false;

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
  
  mic = new KetaiAudioInput(this);
  //start listening for BT connections
  bt.start();
  //at app start select device\u2026
  orientation(LANDSCAPE);
  background(0); 
  isConfiguring = true;
  //font size
  
  brain = new BPN(0, DEFAULT_LEARNING_FACTOR, DEFAULT_NUM_HIDDEN_LAYERS, DEFAULT_NUM_UNITS_INPUT_LAYER, DEFAULT_UNITS_OUTPUT_LAYER, DEFAULT_NUM_UNITS_HIDDEN_LAYERS, DEFAULT_TOLERANCE);
  brainManager = new BPNManager(brain);
  
  fourier = new FFT();
  
  initUI();
  
}

public void initUI(){
  cp5 = new ControlP5(this);
  
  wSize = (int)(vScreenWidth/6);
  hSize = (int)(vScreenHeight/4);
  
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
    .setPosition(0*wSize,(1*hSize))
    .setSize(wSize,hSize)
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
  ;
  
  cp5.addButton("NEURAL_NETWORK")
    .setPosition(2*wSize,1*hSize)
    .setSize(wSize,hSize)
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
  ;  
  
  cp5.addButton("TRAIN")
    .setPosition(4*wSize,1*hSize)
    .setSize(wSize,hSize)
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
  ;
  
  
  cp5.addButton("DANCE")
    .setPosition(0*wSize+wSize,3*hSize)
    .setSize(wSize,hSize)
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
  ;
  
  cp5.addButton("MIC")
    .setPosition(2*wSize+wSize,3*hSize)
    .setSize(wSize,hSize)
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
  ;
  
  /*
   cp5.addButton("SLOW")
   .setPosition(0*wSize,3*hSize)
   .setSize(wSize,hSize)
   .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
   ;
   
   cp5.addButton("MEDIUM")
   .setPosition(2*wSize,3*hSize)
   .setSize(wSize,hSize)
   .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
   ;
   
   cp5.addButton("FAST")
   .setPosition(4*wSize,3*hSize)
   .setSize(wSize,hSize)
   .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
   ;    
   
   cp5.addButton("MIC")
   .setPosition(0.5*wSize,5*hSize)
   .setSize(wSize,hSize)
   .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER).setSize(17);
   ;
   
   */
  
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

public void MIC(){
  if (mic.isActive())
    mic.stop(); 
  else
    mic.start();
}

public void NEURAL_NETWORK(){
  if (isConfiguring)
    return;
  if (useBrain){
    useBrain = false;
  }
  else{
    useBrain = true;  
    training = false;
    dance = false;  
    if (!mic.isActive()){
      mic.start();
    }
  }
}

public void TRAIN(){
  if (isConfiguring)
    return;
  if (training){
    training = false;
  }
  else{
    training = true;  
    dance = false;
    useBrain = false;
    if (!mic.isActive()){
      mic.start();
    }
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
    if (!mic.isActive()){
      mic.start();
    }
  }
}

public void FAST(){
  if (isConfiguring)
    return;
  activateDance(100);
  
}

public void DANCE(){
  if (isConfiguring)
    return;
  activateDance(10);
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
      
      if (data != null){
       
        findPeaksAndValleys(data);
        double output = brainManager.move(BPNInput);
        
        double lowerBound = 0.9f-brain.getTolerancia();
        double upperBound = 0.9f+brain.getTolerancia();
        
        boolean dance = (output > lowerBound && output < upperBound); 
        if (dance){
          byte[] result = {getRandomElement(moves)};
          bt.broadcast(result);
          
          if (sumPeaks < INTESITY_THRESHOLD){
            brainManager.train(BPNInput, false);
          }
          
        }else{
          if (sumPeaks > INTESITY_THRESHOLD){
            brainManager.train(BPNInput, true);
          }
          
        }
        
         info = "Using neural network. Value returned: " + output;
      }
      }else if (training){   
        
        //Do something!!
        
      }else if (dance){
     
        if (data != null){
          findPeaksAndValleys(data);
          info = "Dancing. " +". Thresh: " + INTESITY_THRESHOLD + ". Current: " + sumPeaks;
          
           if (sumPeaks > INTESITY_THRESHOLD){
            byte[] result = {getRandomElement(moves)};
            bt.broadcast(result);
          }
          
          /*int sum = 0;
          int [] peaks = findPeaksAndValleys(data);
          
          for (int i = 0; i < peaks.length; ++i){
            sum += Math.abs(data[peaks[i]]);
          }
          info = "Dancing. " +". Thresh: " + INTESITY_THRESHOLD + ". Current: " + sum  ;
          
          if (sum > INTESITY_THRESHOLD){
            byte[] result = {getRandomElement(moves)};
            bt.broadcast(result);
          } */
        }
 
      }else if (mousePressed){
        byte[] data = {(byte)valueToSend};
        bt.broadcast(data);
      }else{
        info = "";
      }
    }
    
    if (data != null)
    {     
      stroke(204, 102, 0);
      int baseX = (int)((0.5f*wSize)+wSize);
      int baseY = height/2;//(int)((5*hSize)+(hSize/2)); 
      for (int i = 0; i < data.length; i++)
      {
        if(i != data.length-1)
          line(baseX+i, baseY+map(data[i], -32768, 32767,displayHeight,0), baseX+i+1, baseY+map(data[i+1], -32768, 32767,displayHeight,0));
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
  
  public short[] getRandomInputsShort(int numInputs){
    short[] result = new short[numInputs];
    for (int i = 0; i < numInputs; ++i){
      result[i] = (short)random(16000);
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
  
  public int [] findPeaksAndValleys(short [] array){
    int [] indexes = new int[array.length];
    int numPeaks = 0;
    sumPeaks = 0;
    BPNInput = new double[DEFAULT_NUM_UNITS_INPUT_LAYER];
    
    for (int i = 0; i < array.length; ++i){
      if (i < BPNInput.length) BPNInput[i] = Math.abs(array[i])/32767; //normalize to value between 0 and 1
      
      if ((i < array.length-1)&&(i > 0)){
        if ((array[i-1] < array[i] && array[i] > array[i+1]) || (array[i-1] > array[i] && array[i] < array[i+1])){
          sumPeaks += Math.abs(array[i]);
          indexes[numPeaks] = i;
          ++numPeaks;
        } 
      }
    }
    int [] result = new int[numPeaks];
    for (int i = 0; i < result.length; ++i) result[i] = indexes[i];
    return result;
  }
  
  public void onAudioEvent(short[] _data)
  {
    trainTap = mousePressed;
    data= _data;
  }
  
//Call back method to manage data received
  public void onBluetoothDataEvent(String who, byte[] data) {
    if (isConfiguring)
      return;
    //received
// info += new String(data);
    //clean if string to long
    //if(info.length() > 150)
    //info = "";
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
    
    public double getWaitTime(short [] input){      
        double [] inputVector = new double[redNeuronal.getCantidadUnidadesEntrada()];
        for (int i = 0; i < inputVector.length; ++i){
          inputVector[i] = (i < input.length) ? input[i] : 0;
        }
        redNeuronal.setEntradas(inputVector);
        redNeuronal.propagarHaciaAdelante();
        double [] output = redNeuronal.getSalidas();
        return output[0];
    }
    
    
    public boolean tapDetected(Complex [] input){
        double [] inputVector = new double[redNeuronal.getCantidadUnidadesEntrada()];
        for (int i = 0; i < inputVector.length; ++i){
          inputVector[i] = input[i].re;
        }
        redNeuronal.setEntradas(inputVector);
        redNeuronal.propagarHaciaAdelante();
        double [] output = redNeuronal.getSalidas();
        
        double lowerBound = 0.9f-redNeuronal.getTolerancia();
        double upperBound = 0.9f+redNeuronal.getTolerancia();
        
        return (output[0] > lowerBound && output[0] < upperBound);
    }
    
    
    public double move(double [] input){
        redNeuronal.setEntradas(input);
        redNeuronal.propagarHaciaAdelante();
        double [] output = redNeuronal.getSalidas();
        
        return output[0];
        /*
        double lowerBound = 0.9-redNeuronal.getTolerancia();
        double upperBound = 0.9+redNeuronal.getTolerancia();
        return (output[0] > lowerBound && output[0] < upperBound);
        */
    }
    
    public void train(double [] input, boolean result){
                redNeuronal.setEntradas(input);
                double r = result? 0.9f : 0.1f;
                double [] output = {r};
                redNeuronal.setSalidasEsperadas(output);
                redNeuronal.propagarHaciaAdelante();
                redNeuronal.computarErrorSalida();
                if (redNeuronal.getError()){
                    redNeuronal.propagarHaciaAtras();
                    redNeuronal.ajustarPesos();
                }
    }
    
    public boolean highIntensityDetected(short [] input){
        double [] inputVector = new double[redNeuronal.getCantidadUnidadesEntrada()];
        for (int i = 0; i < inputVector.length; ++i){
          inputVector[i] = input[i];
        }
        redNeuronal.setEntradas(inputVector);
        redNeuronal.propagarHaciaAdelante();
        double [] output = redNeuronal.getSalidas();
        
        double lowerBound = 0.9f-redNeuronal.getTolerancia();
        double upperBound = 0.9f+redNeuronal.getTolerancia();
        
        return (output[0] > lowerBound && output[0] < upperBound);
    }
    
    
    public void teachTap(Complex [] input, boolean expected){
                double output = expected? 0.9f:0.1f;
                double [] outputs = {output};
                double [] inputVector = new double[redNeuronal.getCantidadUnidadesEntrada()];
                for (int i = 0; i < inputVector.length; ++i){
                  inputVector[i] = input[i].re;
                }
                redNeuronal.setEntradas(inputVector);                
                redNeuronal.setSalidasEsperadas(outputs);
                redNeuronal.propagarHaciaAdelante();
                redNeuronal.computarErrorSalida();
                if (redNeuronal.getError()){
                    redNeuronal.propagarHaciaAtras();
                    redNeuronal.ajustarPesos();
                }
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
/******************************************************************************
 *  Compilation:  javac Complex.java
 *  Execution:    java Complex
 *
 *  Data type for complex numbers.
 *
 *  The data type is "immutable" so once you create and initialize
 *  a Complex object, you cannot change it. The "final" keyword
 *  when declaring re and im enforces this rule, making it a
 *  compile-time error to change the .re or .im fields after
 *  they've been initialized.
 *
 *  % java Complex
 *  a            = 5.0 + 6.0i
 *  b            = -3.0 + 4.0i
 *  Re(a)        = 5.0
 *  Im(a)        = 6.0
 *  b + a        = 2.0 + 10.0i
 *  a - b        = 8.0 + 2.0i
 *  a * b        = -39.0 + 2.0i
 *  b * a        = -39.0 + 2.0i
 *  a / b        = 0.36 - 1.52i
 *  (a / b) * b  = 5.0 + 6.0i
 *  conj(a)      = 5.0 - 6.0i
 *  |a|          = 7.810249675906654
 *  tan(a)       = -6.685231390246571E-6 + 1.0000103108981198i
 *
 ******************************************************************************/

public class Complex {
    public final double re;   // the real part
    public final double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    // return a string representation of the invoking Complex object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    // return abs/modulus/magnitude and angle/phase/argument
    public double abs()   { return Math.hypot(re, im); }  // Math.sqrt(re*re + im*im)
    public double phase() { return Math.atan2(im, re); }  // between -pi and pi

    // return a new Complex object whose value is (this + b)
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this - b)
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this * b)
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    public Complex times(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // return a new Complex object whose value is the conjugate of this
    public Complex conjugate() {  return new Complex(re, -im); }

    // return a new Complex object whose value is the reciprocal of this
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    // return the real or imaginary part
    public double re() { return re; }
    public double im() { return im; }

    // return a / b
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    // return a new Complex object whose value is the complex exponential of this
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // return a new Complex object whose value is the complex sine of this
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex cosine of this
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex tangent of this
    public Complex tan() {
        return sin().divides(cos());
    }
    


    // a static version of plus
    public Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }
}

/******************************************************************************
 *  Compilation:  javac FFT.java
 *  Execution:    java FFT N
 *  Dependencies: Complex.java
 *
 *  Compute the FFT and inverse FFT of a length N complex sequence.
 *  Bare bones implementation that runs in O(N log N) time. Our goal
 *  is to optimize the clarity of the code, rather than performance.
 *
 *  Limitations
 *  -----------
 *   -  assumes N is a power of 2
 *
 *   -  not the most memory efficient algorithm (because it uses
 *      an object type for representing complex numbers and because
 *      it re-allocates memory for the subarray, instead of doing
 *      in-place or reusing a single temporary array)
 *  
 ******************************************************************************/

public class FFT {

    // compute the FFT of x[], assuming its length is a power of 2
    public Complex[] fft(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }

        // fft of even terms
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }


    // compute the inverse FFT of x[], assuming its length is a power of 2
    public Complex[] ifft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].times(1.0f / N);
        }

        return y;

    }

    // compute the circular convolution of x and y
    public Complex[] cconvolve(Complex[] x, Complex[] y) {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.length != y.length) { throw new RuntimeException("Dimensions don't agree"); }

        int N = x.length;

        // compute FFT of each sequence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT
        return ifft(c);
    }


    // compute the linear convolution of x and y
    public Complex[] convolve(Complex[] x, Complex[] y) {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2*x.length];
        for (int i = 0;        i <   x.length; i++) a[i] = x[i];
        for (int i = x.length; i < 2*x.length; i++) a[i] = ZERO;

        Complex[] b = new Complex[2*y.length];
        for (int i = 0;        i <   y.length; i++) b[i] = y[i];
        for (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;

        return cconvolve(a, b);
    }

    // display an array of Complex numbers to standard output
    public void show(Complex[] x, String title) {
      OutputStream os = createOutput("log.txt");
      PrintWriter writer = null;
      try{
      writer = new PrintWriter("log.txt", "UTF-8");    
      }catch(Exception e){}
        System.out.println(title);
        System.out.println("-------------------");
        
        for (int i = 0; i < x.length; i++) {
          writer.println(i + " " + x[i].re);
          System.out.println(x[i].re);
        }
        System.out.println();
        writer.flush();
        writer.close();
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
