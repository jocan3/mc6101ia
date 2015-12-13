package processing.test.ketaiaudio;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ketai.sensors.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class KetaiAudio extends PApplet {




KetaiAudioInput mic;
short[] data;

int time = 0;
int wait = 15000;
int counter = 0;
int counterMeh = 0;
int counterSnapshot = 0;

public void setup()
{
  orientation(LANDSCAPE);
  mic = new KetaiAudioInput(this);
  fill(255,0,0);
  textSize(48);
  time = millis();
  counter = 0;
}


public void draw()
{
  background(128);
  if (data != null)
  {  
    for (int i = 0; i < data.length; i++)
    {
      if(i != data.length-1)
        line(i, map(data[i], -32768, 32767,height,0), i+1, map(data[i+1], -32768, 32767,height,0));
    }
    
    if (millis()-time < wait){
      counter += data.length;
    }else{
      time = millis();
      counterMeh = counter;
      counterSnapshot = data.length;
      counter = 0;
    }
    
  }
  
  if(mic.isActive()){
    text("READING MIC", width/2, height/2);
    text("Data length in 15s: " + counterMeh, (width/2), (height/2)+(height/6));
    text("Data snapshot Length: " + counterSnapshot, (width/2), (height/2)+2*(height/6));
  }
  else
    text("NOT READING MIC", width/2, height/2);
  
}


public void onAudioEvent(short[] _data)
{
  data= _data;
}

public void mousePressed()
{
  if (mic.isActive())
    mic.stop(); 
  else
    mic.start();
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "KetaiAudio" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
