import javax.sound.sampled.*;

TargetDataLine targetLine = null;

void setup(){
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
}


void draw(){
  int numBytesRead;
  byte[] targetData = new byte[targetLine.getBufferSize() / 5];
  numBytesRead = targetLine.read(targetData, 0, targetData.length);
  if (numBytesRead > 0){
     System.out.println(calculateRMSLevel(targetData));
  }
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