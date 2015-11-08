/**
* 
* Controller that uses ControlP5 to create a sample PObject to calculate probabilitie
* authors:
* - José Andrés Mena Arias
* - Oscar Rodriguez Arroyo
*/


import controlP5.*;

ControlP5 cp5;

PObject p = null;
PFont font = createFont("arial",14);

double PValue = 0;
double PConditionalValue = 0;
double PBayesValue = 0;


String textValue = "";

void setup() {
  background(0);
  size(600,600);
  
  
  cp5 = new ControlP5(this);
  
  /*
  cp5.addTextfield("rows")
     .setPosition(20,10)
     .setSize(40,40)
     .setFont(font)
     .setFocus(true)
     .setColor(color(255,255,255))
     ;
                 
  cp5.addTextfield("columns")
     .setPosition(65,10)
     .setSize(40,40)
     .setFont(font)
    .setColor(color(255,255,255))
     ;*/
       
  cp5.addButton("CreatePObject")
     .setPosition(115,10)
     .setSize(80,40)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
     ;    



    cp5.addTextfield("XP")
    .setCaptionLabel("")
     .setPosition(220,400)
     .setSize(40,40)
     .setFont(font)
     .setFocus(true)
     .setColor(color(255,255,255))
     ;
                 
    cp5.addTextfield("xP")
    .setCaptionLabel("")
     .setPosition(265,400)
     .setSize(40,40)
     .setFont(font)
    .setColor(color(255,255,255))
     ;
  
     cp5.addButton("CalculateP")
     .setPosition(315,400)
     .setSize(80,40)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
     ;
     
     
     cp5.addTextfield("XPConditional")
    .setCaptionLabel("")
     .setPosition(220,450)
     .setSize(40,40)
     .setFont(font)
     .setFocus(true)
     .setColor(color(255,255,255))
     ;
                 
    cp5.addTextfield("xPConditional")
    .setCaptionLabel("")
     .setPosition(265,450)
     .setSize(40,40)
     .setFont(font)
    .setColor(color(255,255,255))
     ;
     
     
      cp5.addButton("PConditional")
     .setPosition(315,450)
     .setSize(80,40)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
     ;
     
     
    cp5.addTextfield("XPBayes")
    .setCaptionLabel("")
     .setPosition(220,500)
     .setSize(40,40)
     .setFont(font)
     .setFocus(true)
     .setColor(color(255,255,255))
     ;
                 
    cp5.addTextfield("xPBayes")
    .setCaptionLabel("")
     .setPosition(265,500)
     .setSize(40,40)
     .setFont(font)
    .setColor(color(255,255,255))
     ;
     
     
      cp5.addButton("PBayes")
     .setPosition(315,500)
     .setSize(80,40)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
     ;
     
     
  /*
     cp5.addButton("CalculatePConditional")
     .setPosition(315,400)
     .setSize(80,40)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
     ;
    */ 
     
  textFont(font);
}

/*
void CreatePObject(){ //<>//
  try{
  String s = cp5.get(Textfield.class,"rows").getText();
  String o = s; //<>//
  
  int rows = Integer.parseInt(cp5.get(Textfield.class,"rows").getText());
  int columns = Integer.parseInt(cp5.get(Textfield.class,"columns").getText());
  
  p = new PObject(rows, columns);
  
  drawPObject();}
  catch(Exception e){}
}

*/

void CreatePObject(){
  try{
   //<>//
  int rows = 8;
  int columns = 3;   
  
  p = new PObject(rows, columns);
  
  p.matrix[0][0] = "sum";
  p.matrix[0][1] = "hot";
  p.matrix[0][2] = "wind";
  p.matrix[1][0] = "sum";
  p.matrix[1][1] = "hot";
  p.matrix[1][2] = "rain";
  p.matrix[2][0] = "sum";
  p.matrix[2][1] = "cold";
  p.matrix[2][2] = "sun";
  p.matrix[3][0] = "sum";
  p.matrix[3][1] = "cold";
  p.matrix[3][2] = "wind";
  p.matrix[4][0] = "win";
  p.matrix[4][1] = "hot";
  p.matrix[4][2] = "sun";
  p.matrix[5][0] = "win";
  p.matrix[5][1] = "hot";
  p.matrix[5][2] = "rain";
  p.matrix[6][0] = "win";
  p.matrix[6][1] = "cold";
  p.matrix[6][2] = "wind";
  p.matrix[7][0] = "win";
  p.matrix[7][1] = "cold";
  p.matrix[7][2] = "rain";
  
  
  drawPObject();}
  catch(Exception e){}
}

void CalculateP(){
  try{
  fillPObject(); //<>//
  int X = Integer.parseInt(cp5.get(Textfield.class,"XP").getText()); //<>//
  String x = cp5.get(Textfield.class,"xP").getText();
  PValue = p.P(X,x);
  }catch(Exception e){}
}


void PConditional(){
  try{
  fillPObject(); //<>//
  String [] X = split(cp5.get(Textfield.class,"XPConditional").getText(),',');
  String [] x = split(cp5.get(Textfield.class,"xPConditional").getText(),',');
  
  PConditionalValue = p.PConditional(Integer.parseInt(X[0]),x[0],Integer.parseInt(X[1]),x[1]);
  }catch(Exception e){}
}

void PBayes(){
  try{
  fillPObject(); //<>//
  String [] X = split(cp5.get(Textfield.class,"XPBayes").getText(),',');
  String [] x = split(cp5.get(Textfield.class,"xPBayes").getText(),',');
  
  PBayesValue = p.PBayes(Integer.parseInt(X[0]),x[0],Integer.parseInt(X[1]),x[1]);
  }catch(Exception e){}
}

void fillPObject(){
  for (int i = 0; i < p.rows; ++i){
      for (int j = 0; j < p.columns; ++j){
          p.matrix[i][j] = cp5.get(Textfield.class,i+""+j).getText();
      }
    }
}

void drawPObject(){
  int x = 20;
  int xOrig = 20;
  int y = 80;
  if (p != null){
    for (int i = 0; i < p.rows; ++i){
      for (int j = 0; j < p.columns; ++j){
          cp5.addTextfield(""+i+""+j)
          .setCaptionLabel(""+j)
         .setPosition(x,y)
         .setSize(40,20)
         .setFont(font)
          .setColor(color(255,255,255))
          .setText(p.matrix[i][j]);
         x += 40;
      }
      y += 35;
      x = 20;
      
    }
  }

}

void draw() {
  println(PValue);
 background(0);
  text(PValue+"", 420,420);
  text(PConditionalValue + "",420,470);
  text(PBayesValue + "", 420, 520);
 // fill(255);
  //text(cp5.get(Textfield.class,"input").getText(), 360,130);
  //text(textValue, 360,180);
}

public void clear() {
  cp5.get(Textfield.class,"textValue").clear();
}

void controlEvent(ControlEvent theEvent) {
  if(theEvent.isAssignableFrom(Textfield.class)) {
    println("controlEvent: accessing a string from controller '"
            +theEvent.getName()+"': "
            +theEvent.getStringValue()
            );
  }
}


public void input(String theText) {
  // automatically receives results from controller input
  println("a textfield event for controller 'input' : "+theText);
}




/*
a list of all methods available for the Textfield Controller
use ControlP5.printPublicMethodsFor(Textfield.class);
to print the following list into the console.

You can find further details about class Textfield in the javadoc.

Format:
ClassName : returnType methodName(parameter type)

controlP5.Textfield : String getText() 
controlP5.Textfield : Textfield clear() 
controlP5.Textfield : Textfield keepFocus(boolean) 
controlP5.Textfield : Textfield setAutoClear(boolean) 
controlP5.Textfield : Textfield setFocus(boolean) 
controlP5.Textfield : Textfield setFont(ControlFont) 
controlP5.Textfield : Textfield setFont(PFont) 
controlP5.Textfield : Textfield setFont(int) 
controlP5.Textfield : Textfield setText(String) 
controlP5.Textfield : Textfield setValue(String) 
controlP5.Textfield : Textfield setValue(float) 
controlP5.Textfield : boolean isAutoClear() 
controlP5.Textfield : int getIndex() 
controlP5.Textfield : void draw(PApplet) 
controlP5.Textfield : void keyEvent(KeyEvent) 
controlP5.Textfield : void setInputFilter(int) 
controlP5.Textfield : void setPasswordMode(boolean) 
controlP5.Controller : CColor getColor() 
controlP5.Controller : ControlBehavior getBehavior() 
controlP5.Controller : ControlWindow getControlWindow() 
controlP5.Controller : ControlWindow getWindow() 
controlP5.Controller : ControllerProperty getProperty(String) 
controlP5.Controller : ControllerProperty getProperty(String, String) 
controlP5.Controller : Label getCaptionLabel() 
controlP5.Controller : Label getValueLabel() 
controlP5.Controller : List getControllerPlugList() 
controlP5.Controller : PImage setImage(PImage) 
controlP5.Controller : PImage setImage(PImage, int) 
controlP5.Controller : PVector getAbsolutePosition() 
controlP5.Controller : PVector getPosition() 
controlP5.Controller : String getAddress() 
controlP5.Controller : String getInfo() 
controlP5.Controller : String getName() 
controlP5.Controller : String getStringValue() 
controlP5.Controller : String toString() 
controlP5.Controller : Tab getTab() 
controlP5.Controller : Textfield addCallback(CallbackListener) 
controlP5.Controller : Textfield addListener(ControlListener) 
controlP5.Controller : Textfield bringToFront() 
controlP5.Controller : Textfield bringToFront(ControllerInterface) 
controlP5.Controller : Textfield hide() 
controlP5.Controller : Textfield linebreak() 
controlP5.Controller : Textfield listen(boolean) 
controlP5.Controller : Textfield lock() 
controlP5.Controller : Textfield plugTo(Object) 
controlP5.Controller : Textfield plugTo(Object, String) 
controlP5.Controller : Textfield plugTo(Object[]) 
controlP5.Controller : Textfield plugTo(Object[], String) 
controlP5.Controller : Textfield registerProperty(String) 
controlP5.Controller : Textfield registerProperty(String, String) 
controlP5.Controller : Textfield registerTooltip(String) 
controlP5.Controller : Textfield removeBehavior() 
controlP5.Controller : Textfield removeCallback() 
controlP5.Controller : Textfield removeCallback(CallbackListener) 
controlP5.Controller : Textfield removeListener(ControlListener) 
controlP5.Controller : Textfield removeProperty(String) 
controlP5.Controller : Textfield removeProperty(String, String) 
controlP5.Controller : Textfield setArrayValue(float[]) 
controlP5.Controller : Textfield setArrayValue(int, float) 
controlP5.Controller : Textfield setBehavior(ControlBehavior) 
controlP5.Controller : Textfield setBroadcast(boolean) 
controlP5.Controller : Textfield setCaptionLabel(String) 
controlP5.Controller : Textfield setColor(CColor) 
controlP5.Controller : Textfield setColorActive(int) 
controlP5.Controller : Textfield setColorBackground(int) 
controlP5.Controller : Textfield setColorCaptionLabel(int) 
controlP5.Controller : Textfield setColorForeground(int) 
controlP5.Controller : Textfield setColorValueLabel(int) 
controlP5.Controller : Textfield setDecimalPrecision(int) 
controlP5.Controller : Textfield setDefaultValue(float) 
controlP5.Controller : Textfield setHeight(int) 
controlP5.Controller : Textfield setId(int) 
controlP5.Controller : Textfield setImages(PImage, PImage, PImage) 
controlP5.Controller : Textfield setImages(PImage, PImage, PImage, PImage) 
controlP5.Controller : Textfield setLabelVisible(boolean) 
controlP5.Controller : Textfield setLock(boolean) 
controlP5.Controller : Textfield setMax(float) 
controlP5.Controller : Textfield setMin(float) 
controlP5.Controller : Textfield setMouseOver(boolean) 
controlP5.Controller : Textfield setMoveable(boolean) 
controlP5.Controller : Textfield setPosition(PVector) 
controlP5.Controller : Textfield setPosition(float, float) 
controlP5.Controller : Textfield setSize(PImage) 
controlP5.Controller : Textfield setSize(int, int) 
controlP5.Controller : Textfield setStringValue(String) 
controlP5.Controller : Textfield setUpdate(boolean) 
controlP5.Controller : Textfield setValueLabel(String) 
controlP5.Controller : Textfield setView(ControllerView) 
controlP5.Controller : Textfield setVisible(boolean) 
controlP5.Controller : Textfield setWidth(int) 
controlP5.Controller : Textfield show() 
controlP5.Controller : Textfield unlock() 
controlP5.Controller : Textfield unplugFrom(Object) 
controlP5.Controller : Textfield unplugFrom(Object[]) 
controlP5.Controller : Textfield unregisterTooltip() 
controlP5.Controller : Textfield update() 
controlP5.Controller : Textfield updateSize() 
controlP5.Controller : boolean isActive() 
controlP5.Controller : boolean isBroadcast() 
controlP5.Controller : boolean isInside() 
controlP5.Controller : boolean isLabelVisible() 
controlP5.Controller : boolean isListening() 
controlP5.Controller : boolean isLock() 
controlP5.Controller : boolean isMouseOver() 
controlP5.Controller : boolean isMousePressed() 
controlP5.Controller : boolean isMoveable() 
controlP5.Controller : boolean isUpdate() 
controlP5.Controller : boolean isVisible() 
controlP5.Controller : float getArrayValue(int) 
controlP5.Controller : float getDefaultValue() 
controlP5.Controller : float getMax() 
controlP5.Controller : float getMin() 
controlP5.Controller : float getValue() 
controlP5.Controller : float[] getArrayValue() 
controlP5.Controller : int getDecimalPrecision() 
controlP5.Controller : int getHeight() 
controlP5.Controller : int getId() 
controlP5.Controller : int getWidth() 
controlP5.Controller : int listenerSize() 
controlP5.Controller : void remove() 
controlP5.Controller : void setView(ControllerView, int) 
java.lang.Object : String toString() 
java.lang.Object : boolean equals(Object) 


*/