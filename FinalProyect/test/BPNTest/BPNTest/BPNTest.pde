
final int DEFAULT_NUM_UNITS_INPUT_LAYER = 5000;
final double DEFAULT_LEARNING_FACTOR = 1.1;
final int DEFAULT_NUM_HIDDEN_LAYERS = 1;
final int DEFAULT_NUM_UNITS_HIDDEN_LAYERS = 2500;
final int DEFAULT_UNITS_OUTPUT_LAYER = 1;
final double DEFAULT_TOLERANCE = 0.1;

BPN brain;
BPNManager brainManager;


void setup(){
  size(10,10);
 brain = new BPN(0, DEFAULT_LEARNING_FACTOR, DEFAULT_NUM_HIDDEN_LAYERS, DEFAULT_NUM_UNITS_INPUT_LAYER, DEFAULT_UNITS_OUTPUT_LAYER, DEFAULT_NUM_UNITS_HIDDEN_LAYERS, DEFAULT_TOLERANCE);
 brainManager = new BPNManager(brain);
}


void draw(){
  
      short[] meh =getRandomInputsShort(DEFAULT_NUM_UNITS_INPUT_LAYER);
      double result = brainManager.getWaitTime(meh);     
      
       
      String str = "Result: " + result;
      
      System.out.println(str);
    
}



short[] getRandomInputsShort(int numInputs){
  short[] result = new short[numInputs];
  for (int i = 0; i < numInputs; ++i){
    result[i] = (short)random(16000);
  }
  return result;
}

byte[] getRandomInputs(int numInputs){
  byte[] result = new byte[numInputs];
  for (int i = 0; i < numInputs; ++i){
    result[i] = (byte)(((int)random(2) == 1) ? '1' : '0');
  }
  return result;
}