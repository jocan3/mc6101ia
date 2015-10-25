/*
* authors:
*      - José Andrés Mena
*      - Oscar Rodríguez
*/

PImage bg;
int a;
Ball b;

ArrayList<PVector> queue;
ArrayList<String> visited;

ArrayList<PVector> route;

color black = color(0,0,0);
color white = color(255,255,255);

PVector target = new PVector(390,130);



void setup() 
{
  b = new Ball();
  size(420,420);
  frameRate(20);
  
  
  
  visited = new ArrayList<String>();
  queue = new ArrayList<PVector>();
  queue.add(new PVector(10,10)); 
  
  route = new ArrayList<PVector>();
  route.add(new PVector(10,10));
  
  // The background image must be the same size as the parameters
  // into the size() method. In this program, the size of "milan_rubbish.jpg"
  // is 200 x 200 pixels.
  bg = loadImage("data/background.jpg");
  
  calculateTarget();
}



void draw() 
{
  background(bg);
  
  if (!(b.location.x == target.x && b.location.y == target.y) && !queue.isEmpty()){
    moveBall();
  }
  
  b.display();
  
  displayRoute();
  
  fill(255,69,0);
  ellipse(target.x,target.y,10,10);
}


void displayRoute(){
  for (int i = 0; i < route.size(); ++i){
    fill(255,69,0);
    ellipse(route.get(i).x,route.get(i).y,5,5);  
  }
}


void calculateTarget(){
  boolean found = false;
  
  while (!found){
    target = new PVector((int)random(width/20)*20 - 10,(int)random(height/20)*20 - 10);
    if (bg.get((int)target.x,(int)target.y)==white){
      found = true;
    }
  }

}

void moveBall(){
  
  if (b.location.x == 250 && b.location.y == 210){
    a = 9; //<>//
  } 
  
  
  PVector [] directions = new PVector[4];
  PVector up = new PVector(b.location.x, b.location.y - 20);
  PVector left = new PVector(b.location.x-20, b.location.y);
  PVector down = new PVector(b.location.x, b.location.y + 20);
  PVector right = new PVector(b.location.x + 20, b.location.y);
  
  directions[0] = up;
  directions[1] = left;
  directions[2] = down;
  directions[3] = right;
  

  
  float weightUp = 99999;
  float weightLeft = 99999;
  float weightDown = 99999;
  float weightRight = 99999;

  
  
  if (bg.get((int)up.x,(int)up.y)==white && up.y > 0 && !visited.contains(up.x + "" + up.y)){
    weightUp = Math.abs(target.x - up.x) + Math.abs(target.y - up.y);
  }
  
  if (bg.get((int)left.x,(int)left.y)==white && left.x > 0 && !visited.contains(left.x + "" + left.y)){
    weightLeft = Math.abs(target.x - left.x) + Math.abs(target.y - left.y);
  }

  if (bg.get((int)down.x,(int)down.y)==white && down.y < height && !visited.contains(down.x + "" + down.y)){
    weightDown = Math.abs(target.x - down.x) + Math.abs(target.y - down.y);
  }
  
  if (bg.get((int)right.x,(int)right.y)==white && right.x < width && !visited.contains(right.x + "" + right.y)){
    weightRight = Math.abs(target.x - right.x) + Math.abs(target.y - right.y);
  }
  
    float [] weights = new float[4]; 
  
    weights[0] = weightUp;
    weights[1] = weightLeft;
    weights[2] = weightDown;
    weights[3] = weightRight;

  
    if (weights[0] == weights[1] &&  weights[1] == weights[2] && weights[2] == weights[3] && weights[3] == 99999){
      PVector p = queue.get(queue.size() - 1);
      queue.remove(queue.size() - 1);
      route.remove(route.size() - 1);
      visited.add(b.location.x + "" + b.location.y);
      b.location.x = p.x;
      b.location.y = p.y;

    }else{
      int index = getMin(weights);
      queue.add(new PVector(b.location.x, b.location.y));
      route.add(new PVector(b.location.x, b.location.y));
      visited.add(b.location.x + "" + b.location.y);
      b.location.x = directions[index].x;
      b.location.y = directions[index].y;    
    }
    
//  int distance = Math.abs(x1-x0) + Math.abs(y1-y0);
  
}



int getMin(float [] list){
  int index = -1;
  float min = 99999;
  
  for (int i = 0; i < list.length; ++i){
    if (list[i] < min){
      index = i;
      min = list[i];
    }
  }
  return index;

}




class Ball{
  
  PVector location = new PVector(10,10);
  
  void display(){
    fill(220,20,60);
    ellipse(location.x,location.y,18,18);
  }


}