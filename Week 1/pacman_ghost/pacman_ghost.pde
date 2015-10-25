/*
* author: José Andrés Mena Arias
*/

Ghost g1;

void setup() {
  size(400,400);
  background(0);
  g1 = new Ghost(200,200);
}

void draw() {
  noStroke(); 
  background(0);
  
  g1.flee(new PVector(mouseX,mouseY));
  g1.update();
  g1.display();
  
}

class Ghost {

  PVector gColor;
  int gScareFactor;
  PVector eyesOrientation;
  
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
 
    //TODO: Set color and ScareFactor randomly
    gColor = new PVector(255,190,88);
    eyesOrientation = new PVector(0,0);

    //[full] Arbitrary values for maxspeed and
    // force; try varying these!
    maxspeed = 1;
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
    
    if (location.x < 20 || location.x > width-20){
      location.x -= velocity.x;
    }
    
    if (location.y < 25 || location.y > height-25){
      location.y -= velocity.y;
    }
      
    acceleration.mult(0);
  }

  // Newton’s second law; we could divide by mass if we wanted.
  void applyForce(PVector force) {
    acceleration.add(force);
  }

  void flee(PVector target) {
    PVector desired = PVector.sub(location,target);
    desired.normalize();
    desired.mult(maxspeed);
    PVector steer = PVector.sub(desired,velocity);
    steer.limit(maxforce);
    applyForce(steer);
  }

  void display() {
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
      ellipse(location.x+9+eyesOrientation.x,location.y+2+eyesOrientation.y,6,6);
  }
}