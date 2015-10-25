/*
* authors:
*  - José Andrés Mena
*  - Oscar Rodríguez
*/

PImage bg;
PImage pacman;
Ghost g1;
int result;
color black = color(0,0,0);
float [] inputs;
float [] lastInputs;
PVector LocationTmp; 
Boolean flagBoundary = true;
ArrayList<PVector> pacmanLocations;
float min = 0.8;
float max = 0.2;
int output = 0;
int numPacmans=15;

void setup() {
  size(500,500);
 // background(0);
  bg = loadImage("background.jpg");
  pacman = loadImage("Pacman.png");
  g1 = new Ghost(250,250);
  g1.brain = new Perceptron(8);
  inputs = new float[8];
  lastInputs = new float[8];
  CreateNewInputs();
  LocationTmp = new PVector();
  pacmanLocations = new ArrayList<PVector>();
  putRandomPacmans(numPacmans);
}


void mousePressed(){
  putRandomPacmans(numPacmans);
}


void draw() {
  noStroke(); 
  //background(1);
  background(bg);
  drawPacmans();
  if (Collided()){ //punish (train!)
    println("Obstacle " + indexOfObstacle() + " output :"+output);
    g1.brain.train(inputs,indexOfObstacle());
  }else{
       println("Obstacle none, output:" + output);
  }
  PVector target = g1.target;
  
  
  println("Location:" + g1.location.x + "," + g1.location.y);
  if(g1.location.y > height - 30 && g1.location.x > width-30){
    int i =0; //<>//
    while(i > 0){} //<>//
  }
  
//    if (g1.location.x < 30 || g1.location.x > width-30||g1.location.y < 30 || g1.location.y > height-30){
  //    target.x = mouseX;
    //  target.y = mouseY;
        
    //} else{ 
      CreateNewInputs();
      output = (int)Math.round(g1.brain.feedforward(inputs));
        LocationTmp.x = g1.location.x;
        LocationTmp.y = g1.location.y;
        target = translateOutput(output);
      //}
  //float [] inputs = new float[3];
  g1.flee(target);
 // g1.seek(target);
   g1.update();
  g1.display();
  
}
/*
void CreateNewInputs(){
  int x = (int)g1.location.x;
  int y = (int)g1.location.y;
  for (int i = 0; i < inputs.length; ++i) lastInputs[i] = inputs[i];
  inputs[0] = (!isBlack(get(x,y-20)))? min:max;
  inputs[1] = (!isBlack(get(x-20,y-20)))? min:max;
  inputs[2] = (!isBlack(get(x-20,y)))? min:max;
  inputs[3] = (!isBlack(get(x-20,y+20)))? min:max;
  inputs[4] = (!isBlack(get(x,y+20)))? min:max;
  inputs[5] = (!isBlack(get(x+20,y+20)))? min:max;
  inputs[6] = (!isBlack(get(x+20,y)))? min:max;
  inputs[7] = (!isBlack(get(x+20,y-20)))? min:max;

}*/

void drawPacmans(){
  for (int i =0; i < pacmanLocations.size(); ++i){
    image(pacman, pacmanLocations.get(i).x,  pacmanLocations.get(i).y, 15,15);
  }
}

void putRandomPacmans(int n){
  pacmanLocations = new ArrayList<PVector>();
  for (int i = 0; i < n; ++i){
    float x = (int)random(0,width);
     float y = (int)random(0,height);
      pacmanLocations.add(new PVector(x,y));
  }

}



void CreateNewInputs(){
  int x = (int)g1.location.x;
  int y = (int)g1.location.y;
  for (int i = 0; i < inputs.length; ++i) lastInputs[i] = inputs[i];
  inputs[0] = (isBlack(get(x,y-20)))? min:max;
  inputs[1] = (isBlack(get(x-20,y-20)))? min:max;
  inputs[2] = (isBlack(get(x-20,y)))? min:max;
  inputs[3] = (isBlack(get(x-20,y+20)))? min:max;
  inputs[4] = (isBlack(get(x,y+20)))? min:max;
  inputs[5] = (isBlack(get(x+20,y+20)))? min:max;
  inputs[6] = (isBlack(get(x+20,y)))? min:max;
  inputs[7] = (isBlack(get(x+20,y-20)))? min:max;

}


Boolean isBlack(color c){
  return red(c) == 0 && blue(c)== 0 && green(c) == 0;
}

Boolean Collided(){
  Boolean result = false;
  int x = (int)g1.location.x - 19;
  int y = (int)g1.location.y - 19;
  color c;
  for (int i = x; i < x+38; ++i){
    for (int j = y; j < y+38; ++j){
      c = get(i,j);
      if (!isBlack(c)){
        g1.displayRed();
        return true;
      }
    }
  }
  return result;
}


int indexOfObstacle(){
  int result = -1;
  for (int i = 0; i < inputs.length; ++i) 
    if(inputs[i] == max){result = i;}
  
  if (result == -1)result=(int)random(0,inputs.length);
    println("result " + result);
  return result;
}

int indexOfSafe(){
  int result = -1;
  for (int i = 0; i < inputs.length; ++i) 
    if(inputs[i] == min){result = i;}
  
  if (result == -1)result=(int)random(0,inputs.length);
    println("result " + result);
  return result;
}




PVector translateOutput(int value){
  int x = (int)g1.location.x;
  int y = (int)g1.location.y;
  switch (value){
    case 0: return new PVector(x,y-20);
    case 1: return new PVector(x-20,y-20);
    case 2: return new PVector(x-20,y);
    case 3: return new PVector(x-20,y+20);
    case 4: return new PVector(x,y+20);
    case 5: return new PVector(x+20,y+20);
    case 6: return new PVector(x+20,y);
    case 7: return new PVector(x+20,y-20);
  }
  if (value > 7){
    return new PVector(x+20,y-20);
  }
  if (value < 0){
    return new PVector(x,y-20);
  }
  return new PVector(0,0);
}

int getOpositeDirection(int value){
  int result = 0;
  switch (value){
    case 0: result = 4; break;
    case 1: result = 5; break;
    case 2: result = 6; break;
    case 3: result = 7; break;
    case 4: result = 0; break;
    case 5: result = 1; break;
    case 6: result = 2; break;
    case 7: result = 3; break;
  }
  return result;
}

class Ghost {

  Perceptron brain;
  PVector gColor;
  int gScareFactor;
  PVector eyesOrientation;
  PVector target;
  
  PVector location;
  PVector velocity;
  PVector acceleration;
  
  // Additional variable for size
  float r;
  float maxforce;
  float maxspeed;

  Ghost(float x, float y) { //TODO: Put parameters as needed
  
    acceleration = new PVector(0,0);
    velocity = new PVector(0,0);
    location = new PVector(x,y);
    target = new PVector(0,0);
 
    //TODO: Set color and ScareFactor randomly
    gColor = new PVector(255,190,88);
    eyesOrientation = new PVector(0,0);

    //[full] Arbitrary values for maxspeed and
    // force; try varying these!
    maxspeed = 0.9;
    maxforce = 0.1;
    //[end]
  }

  // Our standard “Euler integration” motion model
  void update() {
    velocity.add(acceleration);
    velocity.limit(maxspeed);
    location.add(velocity);
    
    eyesOrientation = velocity;
    eyesOrientation =  eyesOrientation.normalize().mult(1.5);
    
    if (location.x < 30 || location.x > width-30){
      location.x -= velocity.x;
    }
    
    if (location.y < 30 || location.y > height-30){
      location.y -= velocity.y;
    }
      
    acceleration.mult(0);
  }

  // Newton’s second law; we could divide by mass if we wanted.
  void applyForce(PVector force) {
    acceleration.add(force);
  }

  PVector getDesired(){
    PVector desired = PVector.add(location,target);
    desired = desired.normalize();
    
    desired.x = desired.x + location.x;
    desired.y = desired.y + location.y;
    
    return desired.mult(1.9);
    
  }

  void seek(PVector target) {
    this.target.x = target.x;
    this.target.y = target.y;
    PVector desired = PVector.sub(target,location);
    desired.normalize();
    desired.mult(maxspeed);
    PVector steer = PVector.sub(desired,velocity);
    steer.limit(maxforce);
    applyForce(steer);
  }

  void flee(PVector target) {
    this.target.x = target.x;
    this.target.y = target.y;
    PVector desired = PVector.sub(location,target);
    desired.normalize();
    desired.mult(maxspeed);
    PVector steer = PVector.sub(desired,velocity);
    steer.limit(maxforce);
    applyForce(steer);
  }

   void displayRed() {
 /*     fill(255,0,0);
      ellipse(location.x,location.y,40,40);
      ellipse(location.x-13,location.y+10,15,30);
      ellipse(location.x,location.y+10,15,30);
      ellipse(location.x+13,location.y+10,15,30);
    fill(255,0,0);   
      ellipse(location.x-9,location.y,10,14);
      ellipse(location.x+9,location.y,10,14);
      fill(0,0,248);
    //  ellipse(location.x-11,location.y+2,6,6);
    //  ellipse(location.x+7,location.y+2,6,6);
      
      ellipse(location.x-9+eyesOrientation.x,location.y+2+eyesOrientation.y,6,6);
      ellipse(location.x+9+eyesOrientation.x,location.y+2+eyesOrientation.y,6,6);*/
  }

  void display() {
      fill(gColor.x,gColor.y,gColor.z);
      ellipse(location.x,location.y,30,30);
      ellipse(location.x-10,location.y+8,11,23);
      ellipse(location.x,location.y+8,11,23);
      ellipse(location.x+10,location.y+8,11,23);
      fill(255);   
      ellipse(location.x-7,location.y,8,10);
      ellipse(location.x+7,location.y,8,10);
      fill(0,0,248);
      
      ellipse(location.x-7+eyesOrientation.x,location.y+0+eyesOrientation.y,5,5);
      ellipse(location.x+7+eyesOrientation.x,location.y+0+eyesOrientation.y,5,5);
    
    /*
      fill(gColor.x,gColor.y,gColor.z);
      ellipse(location.x,location.y,40,40);
      ellipse(location.x-13,location.y+10,15,30);
      ellipse(location.x,location.y+10,15,30);
      ellipse(location.x+13,location.y+10,15,30);
      fill(255);   
      ellipse(location.x-9,location.y,10,14);
      ellipse(location.x+9,location.y,10,14);
      fill(0,0,248);
    //  ellipse(location.x-11,location.y+2,6,6);
    //  ellipse(location.x+7,location.y+2,6,6);
      
      ellipse(location.x-9+eyesOrientation.x,location.y+2+eyesOrientation.y,6,6);
      ellipse(location.x+9+eyesOrientation.x,location.y+2+eyesOrientation.y,6,6); */
      
  }
}