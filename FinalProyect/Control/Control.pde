/* A simple example to controll RedBot using an Android device as Remote control 
 @author: Jose Andres Mena
 */

//required for BT enabling on startup
import controlP5.*;
import android.content.Intent;
import android.os.Bundle;
import ketai.net.bluetooth.*;
import ketai.ui.*;
import ketai.net.*;
import ketai.sensors.*;

final int DEFAULT_NUM_UNITS_INPUT_LAYER = 1024;
final double DEFAULT_LEARNING_FACTOR = 0.5;
final int DEFAULT_NUM_HIDDEN_LAYERS = 1;
final int DEFAULT_NUM_UNITS_HIDDEN_LAYERS = 512;
final int DEFAULT_UNITS_OUTPUT_LAYER = 1;
final double DEFAULT_TOLERANCE = 0.3;

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

void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  bt = new KetaiBluetooth(this);
}

void onActivityResult(int requestCode, int resultCode, Intent data) {
  bt.onActivityResult(requestCode, resultCode, data);
}

void setup() {
  size(displayWidth, displayHeight);
  vScreenWidth = displayWidth;
  vScreenHeight = displayHeight;
  
  textSize(34);
  
  mic = new KetaiAudioInput(this);
  //start listening for BT connections
  bt.start();
  //at app start select device…
  orientation(LANDSCAPE);
  background(0); 
  isConfiguring = true;
  //font size
  
  brain = new BPN(0, DEFAULT_LEARNING_FACTOR, DEFAULT_NUM_HIDDEN_LAYERS, DEFAULT_NUM_UNITS_INPUT_LAYER, DEFAULT_UNITS_OUTPUT_LAYER, DEFAULT_NUM_UNITS_HIDDEN_LAYERS, DEFAULT_TOLERANCE);
  brainManager = new BPNManager(brain);
  
  fourier = new FFT();
  
  initUI();
  
}

void initUI(){
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

void UP(){
  if (isConfiguring || !connected)
    return;
  info = "UP pressed";
  valueToSend = 'u';
}

void DOWN(){
  if (isConfiguring || !connected)
    return;
  info = "DOWN pressed";
  valueToSend = 'd';
}

void LEFT(){
  if (isConfiguring || !connected)
    return;
  info = "LEFT pressed";
  valueToSend = 'l';
}

void RIGHT(){
  if (isConfiguring || !connected)
    return;
  info = "RIGHT pressed";
  valueToSend = 'r';
}

void START_STOP(){
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

void MIC(){
  if (mic.isActive())
    mic.stop(); 
  else
    mic.start();
}

void NEURAL_NETWORK(){
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

void TRAIN(){
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

void activateDance(int w){
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

void FAST(){
  if (isConfiguring)
    return;
  activateDance(100);
  
}

void DANCE(){
  if (isConfiguring)
    return;
  activateDance(10);
}

void SLOW(){
  if (isConfiguring)
    return;
  activateDance(1000);
}

void MEDIUM(){
  if (isConfiguring)
    return;
  activateDance(500);
  
}




void draw() {
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
        
        double lowerBound = 0.9-brain.getTolerancia();
        double upperBound = 0.9+brain.getTolerancia();
        
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
      int baseX = (int)((0.5*wSize)+wSize);
      int baseY = height/2;//(int)((5*hSize)+(hSize/2)); 
      for (int i = 0; i < data.length; i++)
      {
        if(i != data.length-1)
          line(baseX+i, baseY+map(data[i], -32768, 32767,displayHeight,0), baseX+i+1, baseY+map(data[i+1], -32768, 32767,displayHeight,0));
      }
    } 
  }

  
  
  byte[] getRandomInputs(int numInputs){
    byte[] result = new byte[numInputs];
    for (int i = 0; i < numInputs; ++i){
      result[i] = (byte)(((int)random(2) == 1) ? '1' : '0');
    }
    return result;
  }
  
  short[] getRandomInputsShort(int numInputs){
    short[] result = new short[numInputs];
    for (int i = 0; i < numInputs; ++i){
      result[i] = (short)random(16000);
    }
    return result;
  }
  
  byte getRandomElement(byte[] list){
    return list[(int)random(list.length)];
  }
  
  void onKetaiListSelection(KetaiList klist) {
    String selection = klist.getSelection();
    connectionInfo = "Connecting to " + selection + "." + str(bt.connectToDeviceByName(selection));
    //dispose of list for now
    connected = true;
    klist = null;
  }
  
  int [] findPeaksAndValleys(short [] array){
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
  
  void onAudioEvent(short[] _data)
  {
    trainTap = mousePressed;
    data= _data;
  }
  
//Call back method to manage data received
  void onBluetoothDataEvent(String who, byte[] data) {
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