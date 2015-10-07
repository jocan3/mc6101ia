import java.util.*;

PImage bgImg;
PImage OImg;
PImage XImg; 
Board b;
int turn = 0;
Boolean gameOver = false;
PVector move = new PVector(0,0);
ArrayList<String> log = new ArrayList<String>();


void setup(){
  size(300,300);
  bgImg = loadImage("board.png");
  OImg = loadImage("O.png");
  XImg = loadImage("X.png");
  b = new Board();
  Random rn =new Random();
  turn = rn.nextInt(2); 
}

void draw(){
  background(bgImg);
  b.display();

}

int mapToMatrix(float value){
  if (value < 100){
    return 0;
  }else if (value >= 100 && value < 200){
    return 1;
  }else{
    return 2;
  }

}

void mousePressed(){
  println("turn " + turn);
  if (!gameOver){
    int x = mapToMatrix(mouseX);
    int y = mapToMatrix(mouseY);
    
    if (turn == 0){
      DrawX();
      x = (int)move.x;
      y = (int)move.y;
      if (b.matrix[x][y] == ' '){
        b.matrix[x][y] = 'X';
        turn = 1;
      }
      
    }else{
      if (b.matrix[x][y] == ' '){
        b.matrix[x][y] = 'O';
        turn = 0;
      }
    }
    gameOver = b.getWinner() != ' ';
    String [] array = new String[log.size()];
    saveStrings("log.txt",log.toArray(array));
  }
  

}



class Board{
  
  public char [][] matrix;
  
  public Board(){
    matrix = new char[3][3];
    for (int i = 0; i < 3; ++i){
    for (int j = 0; j < 3; ++j){
          matrix[i][j] = ' ';
    }
  }
  }
  
  public Board(char[][] m){
    matrix = new char[3][3];
  for (int i = 0; i < 3; ++i){
    for (int j = 0; j < 3; ++j){
          matrix[i][j] = m[i][j];
    }
  }
  
  }
  
  
  void display(){
    for (int i = 0; i < 3; ++i){
    for (int j = 0; j < 3; ++j){
          if (matrix[i][j] == 'X'){
            image(XImg, i*100, j*100, 100, 100);
          }
          if (matrix[i][j] == 'O'){
            image(OImg, i*100, j*100, 100, 100);
          };
    }
  }
  
  }
  
  char getWinner(){
    char result = ' ';
    int Xcount = 0;
    int Ocount = 0;
    
    for (int i = 0; i < 3 && result == ' '; ++i){
    for (int j = 0; j < 3 && result == ' '; ++j){
      if (matrix[i][j] == 'X') Xcount++;
      if (matrix[i][j] == 'O') Ocount++;
    }
    if (Xcount == 3) result = 'X';
    if (Ocount == 3) result = 'O';
    Xcount = 0;
    Ocount = 0;
    }
    
    for (int i = 0; i < 3 && result == ' '; ++i){
    for (int j = 0; j < 3 && result == ' '; ++j){
      if (matrix[j][i] == 'X') Xcount++;
      if (matrix[j][i] == 'O') Ocount++;
    }
    if (Xcount == 3) result = 'X';
    if (Ocount == 3) result = 'O';
    Xcount = 0;
    Ocount = 0;
    }
    
    for (int i = 0; i < 3 && result == ' '; ++i){
      if (matrix[i][i] == 'X') Xcount++;
      if (matrix[i][i] == 'O') Ocount++;
    }
    if (Xcount == 3) result = 'X';
    if (Ocount == 3) result = 'O';
    Xcount = 0;
    Ocount = 0;
    
    for (int i = 0; i < 3 && result == ' '; ++i){
      if (matrix[i][2-i] == 'X') Xcount++;
      if (matrix[i][2-i] == 'O') Ocount++;
    }
    if (Xcount == 3) result = 'X';
    if (Ocount == 3) result = 'O';
    
    
    return result;
  }
  

}






 class Node{

  public ArrayList<Node> children;
  public char [][] matrix;
  public int minmax;
  public char symbol;
  PVector move;
  
  
  public Node(){
    children  = new ArrayList<Node>();
    matrix = new char[3][3];
  }
  
  public Node(char [][] m, char s, float x, float y){
    children  = new ArrayList<Node>();
    matrix = new char[3][3];
    symbol = s;
    for (int i = 0; i < 3; ++i){
      for (int j=0; j < 3; ++j){
        matrix[i][j] = m[i][j];
      }
    }
    
    move = new PVector(x,y);
    
    
    //minmax = calculateMinMax();
        
    if (CheckIfWinner() == ' '){
      getChildren();
    }
    
    //log.add("Node: symbol " +symbol+ " value " + matrix);
    
   }
   
   public Boolean isTerminal(){
     return children.isEmpty();
   }
   
   public int  getTotal(){
     //if terminal, return minmax, else if X get Minimum from children, if O, get maxsimun of children
     int result = 0;
     if (this.isTerminal()){
       char winner = ' ';
       Board bTemp = new Board(this.matrix);
       winner = bTemp.getWinner();
       if (winner == 'X') result = 1;
       if (winner == 'O') result = -1;
       if (winner == ' ') result = 0;                    
     }else{
       int [] values = new int[this.children.size()];
          for (int i = 0; i < values.length; ++i){
            values[i] = children.get(i).getTotal();
          }
       
       if (symbol == 'X'){
          int index = getMin(values);
          result = values[index];
       }else{
         int index = getMax(values);
          result = values[index];
       }
     
     }
     return result;
   }
   
   public char CheckIfWinner(){
      char winner = ' ';
      Board bTemp = new Board(this.matrix);
      winner = bTemp.getWinner();      
      return winner;
   }
   
   public int calculateMinMax(){
     char winner = ' ';
     Board bTemp = new Board(this.matrix);
     winner = bTemp.getWinner();
     if (symbol == 'X'){  
       if (winner == 'X') return 1;
       else return 0;
       //Call checkGame if (win == 1) minmax = 1, else minmax = 0
     }else{
       if (winner == 'O') return -1;
       else return 0;
       //Call checkGame if (win != 1) minmax = -1, else minmax = 0
     }
   }
  
  public void getChildren(){
    if (minmax == 0){
    char childrenSymbol = symbol == 'X' ? 'O' : 'X';
    for (int i = 0; i < 3; ++i){
      for (int j=0; j < 3; ++j){
        if (matrix[i][j] == ' '){
            matrix[i][j] = childrenSymbol;
            children.add(new Node(matrix, childrenSymbol, i, j));
            matrix[i][j] = ' ';
        }
      }
    }
    }
    
  } 
}

void DrawX(){
  char tmp = ' ';
  char [][] matrix = new char [3][3]; 
  
  for(int i = 0; i < 3; ++i){
    for(int j = 0; j < 3; ++j){
      matrix[i][j] = b.matrix[i][j];
    }
  }
  
  Node root = new Node(matrix, 'O', -1, -1); //Pull board values //<>// //<>//
  int [] totals = new int [root.children.size()];
   for(int i = 0; i < totals.length; ++i){
    totals[i] = root.children.get(i).getTotal();
  }
  
  int index = getMax(totals);
  
  move.x = root.children.get(index).move.x;
  move.y = root.children.get(index).move.y;
  
}

int getMax(int [] list){
  int index = -1;
  int max = -99999;
  
  for (int i = 0; i < list.length; ++i){
    if (list[i] > max){
      index = i;
      max = list[i];
    }
  }
  return index;

} 


int getMin(int [] list){
  int index = -1;
  int min = 99999;
  
  for (int i = 0; i < list.length; ++i){
    if (list[i] < min){
      index = i;
      min = list[i];
    }
  }
  return index;

} 