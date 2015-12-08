final int DEFAULT_NUM_UNITS_INPUT_LAYER = 10;
final double DEFAULT_LEARNING_FACTOR = 1.1;
final int DEFAULT_NUM_HIDDEN_LAYERS = 1;
final int DEFAULT_NUM_UNITS_HIDDEN_LAYERS = 5;
final int DEFAULT_UNITS_OUTPUT_LAYER = 10;
final double DEFAULT_TOLERANCE = 0.1;
BPN brain;
BPNManager brainManager;


void setup(){
  size(10,10);
 brain = new BPN(0, DEFAULT_LEARNING_FACTOR, DEFAULT_NUM_HIDDEN_LAYERS, DEFAULT_NUM_UNITS_INPUT_LAYER, DEFAULT_UNITS_OUTPUT_LAYER, DEFAULT_NUM_UNITS_HIDDEN_LAYERS, DEFAULT_TOLERANCE);
 brainManager = new BPNManager(brain);
}


void draw(){
  
      byte[] meh =getRandomInputs(DEFAULT_NUM_UNITS_INPUT_LAYER);
      byte[] result = brainManager.getMovesPattern(meh);     
      
       
      String str = "";
      for (int i = 0; i < meh.length; ++i){
        str += (char)meh[i];
      }
      System.out.println(str);
    
      
      str = "";
      for (int i = 0; i < result.length; ++i){
        str += (char)result[i];
      }
      System.out.println(str);
    
}




byte[] getRandomInputs(int numInputs){
  byte[] result = new byte[numInputs];
  for (int i = 0; i < numInputs; ++i){
    result[i] = (byte)(((int)random(2) == 1) ? '1' : '0');
  }
  return result;
}