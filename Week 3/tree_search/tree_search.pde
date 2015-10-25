/*
* authors:
*        - José Andrés Mena
*        - Natalia Marin
*        - Randy Salas
*/


ArrayList<PVector> queue;
ArrayList<String> visited;
PVector goal;

Ghost g1;

void setup() {
  size(300,300);
  frameRate(20);
  background(0);
  g1 = new Ghost(150,150);
  visited = new ArrayList<String>();
  queue = new ArrayList<PVector>();
  queue.add(new PVector(150,150));    
  goal = new PVector((int)random(width/5)*5,(int)random(height/5)*5);
}


boolean isEmpty(ArrayList<PVector> list){
  return list.size() == 1 && list.get(0).x== -1 && list.get(0).y== -1;
}


void depthFirst(Ghost agent){


  boolean moved = false;

    float posX = g1.location.x;
    float posY = g1.location.y;
   

    if (posY > 0 && !visited.contains(posX + "" + (posY-5))){

      g1.location.y = g1.location.y-5;

      queue.add(new PVector(posX, posY));

      visited.add(posX + "" + posY);

      moved = true;

  }else if(posX > 0 && !visited.contains((posX-5) + "" + posY)){
      g1.location.x = g1.location.x-5;
      queue.add(new PVector(posX, posY));
      visited.add(posX + "" + posY);
      moved = true;
    }else if(posY < height && !visited.contains(posX + "" + (posY+5))){
      g1.location.y = g1.location.y+5;
      queue.add(new PVector(posX, posY));
      visited.add(posX + "" + posY);
      moved = true;
    }else if(posX < width && !visited.contains((posX+5) + "" + posY)){
      g1.location.x = g1.location.x+5;
      queue.add(new PVector(posX, posY));
      visited.add(posX + "" + posY);
      moved = true;
    } 
    
    //System.out.println(g1.location.x + "," + g1.location.y + " Visited: " + visited.get(visited.size()-1));
    
//    System.out.println(visited.toString());
    
    
    if (!moved){
      PVector p = queue.get(queue.size() - 1);
      queue.remove(queue.size() - 1);
      visited.add(g1.location.x + "" + g1.location.y);
      g1.location.x = p.x;
      g1.location.y = p.y;
    }
  
 

}


void widthFirst(Ghost agent){
    PVector newPos = queue.get(0);
    queue.remove(0);
    float posX = g1.location.x = newPos.x;
    float posY = g1.location.y = newPos.y;
    visited.add(posX + "" + posY);
   

    if (posY > 0 && !visited.contains(posX + "" + (posY-5))){
      queue.add(new PVector(posX, posY-5));
          visited.add(posX + "" + (posY-5));
    }
  
    if(posX > 0 && !visited.contains((posX-5) + "" + posY)){
      queue.add(new PVector(posX-5, posY));
          visited.add((posX-5) + "" + posY);

    }
    
    if(posY < height && !visited.contains(posX + "" + (posY+5))){
      queue.add(new PVector(posX, posY+5));
      visited.add(posX + "" + (posY+5));
    }
    
    if(posX < width && !visited.contains((posX+5) + "" + posY)){
      queue.add(new PVector(posX+5, posY));
      visited.add((posX+5) + "" + posY);
    } 
    
        System.out.println(posX + "," + posY);
  

}


void draw() {
  noStroke(); 
  background(0);
  
  fill(175);
  ellipse(goal.x,goal.y, 5,5);
  
  
  //Arrived
  if (!(g1.location.x == goal.x && g1.location.y == goal.y) && !queue.isEmpty()){
    depthFirst(g1);
    //widthFirst(g1);
  }
  


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

  // Our standard .Euler integration. motion model
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

  // Newton.s second law; we could divide by mass if we wanted.
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