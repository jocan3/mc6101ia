import ketai.sensors.*;


KetaiAudioInput mic;
short[] data;

int time = 0;
int wait = 3000;
int counter = 0;
int counterMeh = 0;
int counterSnapshot = 0;
int sumSnapshot = 0;
int maxSumSnapshot = 0;

String message = "";


void setup()
{
  orientation(LANDSCAPE);
  mic = new KetaiAudioInput(this);
  fill(255,0,0);
  textSize(48);
  time = millis();
  counter = 0;
}


void draw()
{
  background(128);
  if (data != null)
  { 
    try{
    float [] pdata = new float[data.length];
    Complex [] cdata = new Complex[1024];
     
     float [] mappedData = new float[data.length];
      
      sumSnapshot = 0;
    for (int i = 0; i < data.length; i++)
    {
       pdata[i] = data[i];
       if(i < cdata.length) cdata[i] = new Complex(data[i],0);
      
      
      // sumSnapshot += data[i];
      if(i != data.length-1){
        line(i, map(data[i], -32768, 32767,height,0), i+1, map(data[i+1], -32768, 32767,height,0));
          mappedData[i] = map(data[i], -32768, 32767,height,0);
          mappedData[i+1] = map(data[i+1], -32768, 32767,height,0);
       }
  
    }
 
 
    int [] presult = findPeaksAndValleys(mappedData);
    
    message = "Num peaks: " + presult.length;
    
    for (int i = 0; i < presult.length; i++){  
      sumSnapshot += Math.abs(mappedData[presult[i]]);// < 0) ? mappedData[presult[i]]*-1:mappedData[presult[i]]; 
      //ellipse(presult[i],map(data[presult[i]], -32768, 32767,2*(height/3),(height/3)),10,10);
    }
    
    if (sumSnapshot > maxSumSnapshot) maxSumSnapshot = sumSnapshot;  
    /*
    FFT f = new FFT();
    Complex [] cresult = f.fft(cdata);
    
    for (int i = 0; i < cresult.length/2; i++)
    {  
      if(i != cresult.length-1)
        line(i*2, map((float)cresult[i].re, -32768, 32768,height,2*(height/3)), (i+1)*2, map((float)cresult[i+1].re, -32768, 32767,height,2*(height/3)));
    }*/
    
    
    
    if (millis()-time < wait){
      counter += data.length;
    }else{
      time = millis();
      counterMeh = counter;
      counterSnapshot = data.length;
      counter = 0;
    }
    }catch (Exception e){
      message = "Error: "+e;;      
    }
    
  }
  
  if(mic.isActive()){
    text("READING MIC", width/2, height/2);
    text("Max sum snapshot: " + maxSumSnapshot, (width/2), (height/2)+(height/6));
    text("sum snapshot Length: " + sumSnapshot, (width/2), (height/2)+2*(height/6));
    text("Log: " + message, (width/4), (height/2)+3*(height/6));
  }
  else
    text("NOT READING MIC", width/2, height/2);
  
}

int [] findPeaksAndValleys(float [] array){
  int [] indexes = new int[array.length];
  
  int numPeaks = 0;
  
  for (int i = 0; i < array.length; ++i){
    if ((i < array.length-1)&&(i > 0)){
      if ((array[i-1] < array[i] && array[i] > array[i+1]) || (array[i-1] > array[i] && array[i] < array[i+1])){
        indexes[numPeaks] = i;
        ++numPeaks;
      } 
    }
  }
  int [] result = new int[numPeaks];
  for (int i = 0; i < result.length; ++i) result[i] = indexes[i];
  return result;
}


int [] findPeaksAndValleys(short [] array){
  int [] indexes = new int[array.length];
  
  int numPeaks = 0;
  
  for (int i = 0; i < array.length; ++i){
    if ((i < array.length-1)&&(i > 0)){
      if ((array[i-1] < array[i] && array[i] > array[i+1]) || (array[i-1] > array[i] && array[i] < array[i+1])){
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
  data= _data;
}

void mousePressed()
{
  maxSumSnapshot = 0;
  
  if (mic.isActive())
    mic.stop(); 
  else
    mic.start();
}