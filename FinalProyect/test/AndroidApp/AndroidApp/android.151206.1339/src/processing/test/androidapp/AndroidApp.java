package processing.test.androidapp;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import android.content.Intent; 
import android.os.Bundle; 
import ketai.net.bluetooth.*; 
import ketai.ui.*; 
import ketai.net.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AndroidApp extends PApplet {

/* A simple example to controll your arduino board via bluetooth of your android smartphone or tablet. Tested with Android 4.
Requirements:
Arduino Board
Bluetooth shield
Android Smartphone (Android 2.3.3 min.)
Ketai library for processing
Jumper D0 to TX
Jumper D1 to RX
Set bluetooth, bluetooth admin and internet sketch permissions in processing.
Processing Code:
*/

//required for BT enabling on startup







PFont fontMy;
boolean bReleased = true; //no permament sending when finger is tap
KetaiBluetooth bt;
boolean isConfiguring = true;
boolean done = false;
String info = "";
KetaiList klist;
ArrayList devicesDiscovered = new ArrayList();

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
 
 frameRate(10);
 orientation(PORTRAIT);
 background(0);
 
 //start listening for BT connections
 bt.start();
 //at app start select device\u2026
 isConfiguring = true;
 //font size
 fontMy = createFont("SansSerif", 40);
 textFont(fontMy);
}

public void draw() {
 //at app start select device
 if (isConfiguring)
 {
  ArrayList names;
  background(78, 93, 75);
  klist = new KetaiList(this, bt.getPairedDeviceNames());
  isConfiguring = false;
 }
 else
 {
   background(0,50,0);
  //if((mousePressed) && (bReleased == true))
  //{
 //send with BT
  byte[] data = {'u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u','u'};
  bt.broadcast(data);
 //first tap off to send next message
 // bReleased = false;
  //}
  //if(mousePressed == false)
  //{
  //bReleased = true; //finger is up
  //}
 //print received data
  fill(255);
  noStroke();
  textAlign(LEFT);
  text(info, 20, 104);
  done = true;
  }
}


public void onKetaiListSelection(KetaiList klist) {
 String selection = klist.getSelection();
 bt.connectToDeviceByName(selection);
 //dispose of list for now
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
  public void settings() {  size(displayWidth, displayHeight); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AndroidApp" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
