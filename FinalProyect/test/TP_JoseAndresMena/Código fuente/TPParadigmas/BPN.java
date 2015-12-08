import java.util.Random;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 * Implementa una Red Neuronal de Propagaci�n hacia Atras (Back Propagation Network), sus estructuras y los m�todos para su funcionamiento general
 * @author Andres Mena
 * @version 2012
 
 */
public class BPN
{
    private Capa unidadesEntrada;
    private Capa unidadesSalida;
    private Capa[] capas;
    private double alfa;
    private double eta;
    private int numeroCapasOcultas;
    private int cantidadUnidadesEntrada;
    private int cantidadUnidadesSalida;
    private int cantidadUnidadesCapaOculta;
    private double [] salidasEsperadas;
    private double tolerancia;
    private boolean error;
    private double [] vectorSalidas;
    private double promedioErrores;
    
    public BPN(double alfa, double eta, int numeroCapasOcultas, int cantidadUnidadesEntrada, int cantidadUnidadesSalida, int cantidadUnidadesCapaOculta, double tol){
        vectorSalidas=new double [cantidadUnidadesSalida];
        
        this.error=false;
        this.tolerancia=tol;
        this.alfa=alfa;
        this.eta=eta;
        this.numeroCapasOcultas=numeroCapasOcultas;
        this.cantidadUnidadesEntrada=cantidadUnidadesEntrada+1;
        this.cantidadUnidadesSalida=cantidadUnidadesSalida;
        this.cantidadUnidadesCapaOculta=cantidadUnidadesCapaOculta+1;
        this.promedioErrores = 0;
        
        capas=new Capa[numeroCapasOcultas+2];
        capas[0]= unidadesEntrada = new Capa(this.cantidadUnidadesEntrada,1);
               
        for(int i=1;i<numeroCapasOcultas+1;++i){
            capas[i]=new Capa(this.cantidadUnidadesCapaOculta,capas[i-1].getTamano());
        }
        
        capas[numeroCapasOcultas+1]= unidadesSalida= new Capa(this.cantidadUnidadesSalida,this.cantidadUnidadesCapaOculta);
        salidasEsperadas = new double [this.cantidadUnidadesSalida];
        
        //Se ajustan pesos aleatoriamente cada vez que se crea una nueva red
        Calendar c = new GregorianCalendar();
        Random aleatorio = new Random(c.get(Calendar.SECOND));
        int signo= aleatorio.nextInt(2);
        
        double peso = 0;
        //empieza de 1 porque la primera capa no tiene pesos
        for(int k=1;k<numeroCapasOcultas+2;++k){
            for(int i=0; i<capas[k].getTamano();++i){
                for(int j=0; j<capas[k-1].getTamano();++j){
                    signo= aleatorio.nextInt(2);
                    peso=aleatorio.nextDouble();                    
                    peso = 0.3*peso;
                    if(signo==1){
                    peso=-1*peso;
                    }
                    capas[k].getDelta()[i][j]=capas[k].getPesos()[i][j]=peso;
                }
            }
        }
    }

    public void setAlfa(double valor){
            alfa=valor;
    }
    
    public void setEta(double valor){
        eta=valor;
    }
    
    public void setNumeroCapasOcultas(int valor){
        numeroCapasOcultas=valor;
    }
    
    public boolean getError(){
        return error;
    }
    
    public double getTolerancia(){
        return tolerancia;
    }
    
    public double getPromedioErrores(){
        return promedioErrores;
    }
    
    
    public int getCantidadUnidadesEntrada(){
        return cantidadUnidadesEntrada-1;
    }
    
    public int getCantidadUnidadesSalida(){
        return cantidadUnidadesSalida;
    }
    
    public Capa getUnidadesEntrada(){
        return unidadesEntrada;
    }
    
    public Capa getUnidadesSalida(){
        return unidadesSalida;
    }
    
    public Capa getCapa(int indice){
        return capas[indice];
    } 
    
    public double getAlfa(){
        return alfa;
    }
    
    public double getEta(){
        return eta;
    }
    
    public int getNumeroCapasOcultas(){
        return numeroCapasOcultas;
    }

    public void setSalidasEsperadas(double[] valor){
        for(int i=0;i<cantidadUnidadesSalida;++i){
            salidasEsperadas[i]=valor[i];
        }
    }
    
   public void setEntradas(double[] entradas){
        unidadesEntrada.setSalidas(0,1);
        for(int i=1;i<cantidadUnidadesEntrada;++i){
            unidadesEntrada.setSalidas(i,entradas[i-1]);
        }    
    }
    
    public void propagarCapa(Capa inferior, Capa superior){
        double [] entradas;
        double [] actual;
        double [][] conexiones;
        double suma;
        
        entradas=inferior.getSalidas();
        actual=superior.getSalidas();
        conexiones=superior.getPesos();
        
        for(int i=0; i < superior.getTamano();++i){
            suma=0;
            for(int j=0; j<inferior.getTamano();++j){
                suma=suma+ entradas[j] * conexiones[i][j];
            }
            actual[i]=1.0/(1.0 + Math.exp(-suma));

        
        }
        
        
        
        
    }
    
    public void propagarHaciaAdelante(){
        Capa superior;
        Capa inferior;
        
        for(int i=0;i<capas.length-1;++i){
            capas[i].setSalidas(0,1);
            inferior=capas[i];
            superior=capas[i+1];
            propagarCapa(inferior,superior);            
        }
    }
    
   public double[] getSalidas(){
        for(int i=0;i<cantidadUnidadesSalida;++i){
            vectorSalidas[i]=unidadesSalida.getSalidas(i);
        }
        return vectorSalidas;
   }
    
   public void computarErrorSalida(){
        double [] errores = unidadesSalida.getErrores();
        double [] salidas = unidadesSalida.getSalidas();
        double limiteSuperior=0.0;
        double limiteInferior=0.0;
        error=false;
        promedioErrores = 0;
        for(int i =0; i< cantidadUnidadesSalida; ++i){
            limiteSuperior=salidasEsperadas[i]+tolerancia;
            limiteInferior=salidasEsperadas[i]-tolerancia;
            
            if(salidas[i]>limiteSuperior || salidas[i]<limiteInferior){
                error=true;
            }
            errores[i]= salidas[i]*(1-salidas[i])*(salidasEsperadas[i]-salidas[i]);
            

            double errorAbs = salidasEsperadas[i]-salidas[i];

            errorAbs = (errorAbs >= 0) ? errorAbs : (-1*errorAbs);
            promedioErrores+= errorAbs;
            
        }
        
        promedioErrores = promedioErrores/cantidadUnidadesSalida;
    }
    
    public void propagacionHaciaAtrasError(Capa inferior, Capa superior){
        double [] emisor = superior.getErrores(); 
        double [] receptor = inferior.getErrores();
        double [][] conexiones=superior.getPesos();
        double unidad;
        for(int i=0; i<inferior.getTamano();++i){
            receptor[i]=0;
            for(int j=0; j<superior.getTamano();++j){

                
                receptor[i] = receptor[i]+ emisor[j]*conexiones[j][i];
            }

            unidad = inferior.getSalidas(i);
            receptor[i]= receptor[i]* unidad * (1-unidad);

            
        }
    }
    
    
    public void propagarHaciaAtras(){
        Capa superior;
        Capa inferior;
        
        for(int i=capas.length-1;i>1;--i){
            inferior=capas[i-1];
            superior=capas[i];
            propagacionHaciaAtrasError(inferior,superior);
        }
    }
    
    public void ajustarPesos(){
        Capa actual;
        double [] entradas;
        double [] unidades;
        double [][] pesos;
        double [][] delta;
        double [] error;
        
        for(int i= 1; i<capas.length;++i){

            actual = capas[i];
            unidades = capas[i].getSalidas();
            entradas = capas[i-1].getSalidas();
            pesos=actual.getPesos();
            delta=actual.getDelta();
            error=capas[i].getErrores();
            for(int j=0;j<unidades.length;++j){

                for(int k=0; k<capas[i-1].getTamano();++k){

                    double pesosAntes = pesos[j][k]; 
                    pesos[j][k]=pesos[j][k]+(entradas[k]*eta*error[j]);// + (alfa*delta[j][k]);
                    delta[j][k]= pesos[j][k]-pesosAntes;
                }
            }
        }
    }

    /**
     * Subclase que implementa una capa de una red neuronal de propagaci�n hacia atr�s
     * @author Andres Mena
     * @author Hermes Mora
     * @version 2012
     
     */
      public class Capa{
          private double [] salidas;
          private double [] errores;
          private double [][] pesos;
          private double [][] last_delta;//no sabemos
          private int tamano;
          private int numeroEntradas;
          
          public Capa(int t, int numeroE){
                numeroEntradas=numeroE;
                tamano=t;
                salidas=new double[t];
                errores=new double[t];
                pesos=new double [t][numeroE];
                last_delta = new double [t][numeroE];
            }
          
          public int getTamano(){
            return tamano;  
          }
            
         public int getNumeroEntradas(){
            return numeroEntradas;  
         }
        
          public void setSalidas(int indice, double valor){
              salidas [indice]=valor;
          }
    
          public double getSalidas(int indice){
            return salidas[indice];
          }
          
           public double [] getSalidas(){
            return salidas;
          }
          
           public double [][] getPesos(){
            return pesos;
          }
          
           public double [][] getDelta(){
            return last_delta;
          }
          
            public double [] getErrores(){
            return errores;
          }
        
        }
}
