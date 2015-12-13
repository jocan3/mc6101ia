import javax.sound.sampled.*;

TargetDataLine targetLine = null;


int counter = 0;
int frate = 12;

void setup(){
   //frameRate(frate);
  // Setup a Line.Info instance specifically of the TargetDataLine class.
    Line.Info targetDLInfo = new Line.Info(TargetDataLine.class);
    
    // Get all the mixers from the Java AudioSystem
    Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
    
    // Iterate through each mixer and see if it supports TargetDataLine
    for(int cnt = 0; cnt < mixerInfo.length; cnt++) {
    
    // Get a temporary instance of the current mixer
      Mixer currentMixer = AudioSystem.getMixer(mixerInfo[cnt]);
      
      if( currentMixer.isLineSupported(targetDLInfo) ) {
      // This mixer supports recording
        try{
          targetLine = (TargetDataLine)currentMixer.getLine(targetDLInfo);
          targetLine.open();
          targetLine.start();
        }catch(Exception e){}
        break;
      }
    }
    
    //int[] meh = {10,  42,  10,  8,  24,  13,  10,  15,  18,  13,  6,  13,  13,  19,  22,  26,  30,  22,  24,  18,  23,  27,  26,  19,  16,  34,  51,  47,  24,  19,  30,  16,  29,  20,  17,  16,  22,  19,  21,  28,  24,  34,  29,  20,  23,  44,  40,  45,  43,  32,  36,  28,  22,  29,  30,  24,  20,  16,  15,  18,  22,  15,  13,  8,  19,  15,  17,  26,  19,  18,  11,  15,  17,  20,  22,  27,  19,  13,  22,  17,  23,  23,  24,  18,  12,  25,  22,  19,  17,  43,  38,  20,  20,  16,  20,  20,  23,  19,  29,  22,  12,  30,  30,  26,  21,  31,  30,  27,  25,  25,  40,  34,  17,  32,  35,  23,  19,  32,  22,  17,  28,  34,  37,  25,  26,  21,  20,  22};
    //int[] meh = {23,  21,  19,  24,  34,  21,  18,  29,  40,  38,  36,  38,  24,  23,  23,  18,  13,  15,  23,  31,  37,  39,  33,  29,  28,  25,  16,  14,  27,  17,  14,  17,  23,  20,  17,  12,  12,  12,  16,  10,  12,  23,  21,  17,  13,  18,  12,  23,  16,  25,  30,  28,  32,  37,  35,  31,  28,  34,  35,  36,  33,  33,  34,  27,  33,  32,  37,  39,  31,  34,  29,  31,  32,  30,  39,  41,  35,  33,  32,  33,  30,  37,  41,  34,  43,  45,  46,  49,  47,  43,  44,  49,  48,  32,  33,  42,  47,  38,  31,  36,  48,  48,  42,  35,  38,  49,  49,  39,  38,  42,  49,  47,  38,  37,  46,  50,  45,  38,  35,  46,  46,  39,  45,  47,  50,  51,  40,  39};
   // int[] meh = {52,  51,  51,  52,  51,  51,  49,  46,  48,  48,  53,  49,  47,  50,  50,  53,  53,  52,  52,  52,  53,  53,  52,  52,  52,  52,  52,  53,  53,  52,  52,  51,  52,  50,  49,  48,  42,  43,  44,  50,  49,  52,  40,  52,  44,  35,  50,  52,  52,  52,  52,  51,  50,  53,  52,  50,  47,  40,  21,  24,  31,  30,  51,  27,  20,  27,  51,  42,  34,  38,  48,  52,  52,  52,  53,  52,  52,  52,  51,  51,  51,  48,  48,  52,  50,  46,  32,  37,  40,  34,  30,  52,  51,  44,  44,  50,  52,  52,  52,  52,  52,  52,  53,  53,  52,  52,  52,  52,  52,  52,  49,  47,  52,  49,  35,  46,  49,  38,  36,  40,  53,  52,  52,  52,  53,  53,  52,  52};
    int[] meh = {48,  42,  50,  52,  52,  52,  53,  53,  53,  53,  52,  52,  53,  52,  51,  52,  53,  52,  52,  52,  51,  46,  52,  52,  52,  52,  50,  50,  49,  50,  47,  48,  50,  41,  28,  24,  24,  25,  21,  23,  20,  18,  16,  18,  17,  21,  24,  24,  19,  18,  36,  37,  31,  35,  34,  25,  29,  22,  19,  19,  20,  21,  25,  19,  20,  18,  18,  24,  25,  17,  21,  23,  23,  22,  22,  20,  20,  22,  20,  27,  23,  21,  19,  21,  20,  16,  20,  21,  19,  18,  25,  20,  23,  28,  25,  23,  21,  26,  28,  22,  30,  30,  29,  23,  27,  31,  41,  33,  22,  21,  25,  24,  21,  22,  26,  24,  28,  25,  17,  17,  21,  22,  29,  28,  30,  21,  24,  41};
    
    System.out.println(meh.length);
    Complex [] input = new Complex[meh.length];
    for (int i = 0; i < meh.length; ++i){
      input[i] = new Complex(meh[i],0);
    }
    FFT f = new FFT();
    Complex [] output = f.fft(input);
    f.show(output,"Result");
    
    
    
    
    /*
    int numBytesRead;
    byte[] targetData = new byte[targetLine.getBufferSize() / 5];
    numBytesRead = targetLine.read(targetData, 0, targetData.length);
    if ((numBytesRead > 0)&&(counter%frate==0)){
       System.out.println(calculateRMSLevel(targetData));
    }
    counter++;*/
}


void draw(){/*
  int numBytesRead;
  byte[] targetData = new byte[targetLine.getBufferSize() / 5];
  numBytesRead = targetLine.read(targetData, 0, targetData.length);
  if ((numBytesRead > 0)&&(counter%frate==0)){
     System.out.println(calculateRMSLevel(targetData));
  }
  counter++;*/
}


public static int calculateRMSLevel(byte[] audioData)
{ 
  // audioData might be buffered data read from a data line
  
  long lSum = 0;
  for(int i=0; i < audioData.length; i++)
  lSum = lSum + audioData[i];
  
  double dAvg = lSum / audioData.length;
  
  double sumMeanSquare = 0d;
  
  for(int j=0; j < audioData.length; j++)
  sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);
  double averageMeanSquare = sumMeanSquare / audioData.length;
  return (int)(Math.pow(averageMeanSquare,0.5d) + 0.5);
}