/*
* Class that calculates probabilities: P, P conditional and P Bayes
* 
*/


class PObject{

  public String [][] matrix;
  public int rows;
  public int columns;
  
  
  PObject(int x, int y){
    matrix = new String[x][y];
    rows = x;
    columns = y;
  }
  
  
  double P(int X, String x){
    return (double)getOccurrences(X,x)/rows;
  }
  
  
  int getOccurrences(int X, String x){
    int occurrences = 0; //<>//
    for (int i = 0; i < rows; ++i){
      if (matrix[i][X].equals(x)){
        ++occurrences;
      }
    }
    return occurrences;
  }
  
  
  int getOccurrences(int X1, String x1, int X2, String x2){
    int occurrences = 0;
    for (int i = 0; i < rows; ++i){
      if (matrix[i][X1].equals(x1)&&matrix[i][X2].equals(x2)) {
        ++occurrences;
      }
    }
    return occurrences;
  }
  
  
  double PIntersection(int X1, String x1, int X2, String x2){
    return (double)getOccurrences(X1,x1,X2,x2)/rows;
  }
  
  
  double PConditional(int X1, String x1, int X2, String x2){
    return (PIntersection(X1,x1,X2,x2))/P(X2,x2);  
  } 
  

  double PBayes(int X1, String x1, int X2, String x2){
    return (double)(PConditional(X2,x2,X1,x1)*P(X1,x1))/P(X2,x2); //<>//
  }
  
}