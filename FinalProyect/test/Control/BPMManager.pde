import java.io.File;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.lang.Thread;
/**
 * Manages a neural network able to recognize patterns based on songs tempo/frecuency/strenght
 */
public class BPNManager
{

    private BPN redNeuronal;
    private int iteracionConverge;
    

    public BPNManager(BPN redN){
        redNeuronal = redN;
    }
    
    public void setIteracionConverge(int it){
        iteracionConverge = it;    
    }
    
    public void generarArchivoDePesos(File archivo){
        ArrayList<String> pesos = new ArrayList<String>();
        
        for (int k = 1; k < redNeuronal.getNumeroCapasOcultas() + 2; ++k){
            pesos.add("Pesos capa" + k);
             for (int i = 0; i < redNeuronal.getCapa(k).getTamano(); ++i){
                String pesosTemp = "";
                 for (int j = 0; j < redNeuronal.getCapa(k).getNumeroEntradas(); ++j){
                    pesosTemp+=redNeuronal.getCapa(k).getPesos()[i][j] + " ";                    
                }      
                pesos.add(pesosTemp);
                //System.out.println("Generó pesos en la fila " + i + " de la capa " + k);
             }                
        }
       // manejadorArchivos.escribirEnArchivo(archivo, pesos);        TODO
    }
    
    
    public void cargarArchivoDePesos(File archivo){
        ArrayList<String> st = null; //manejadorArchivos.leerDesdeArchivo(archivo);   TODO                     
        boolean archivoValido = true;
        int c = 0;
        try{
        for (int k = 1; k < redNeuronal.getNumeroCapasOcultas() + 2; ++k){
            ++c;            
             for (int i = 0; i < redNeuronal.getCapa(k).getTamano(); ++i){
                StringTokenizer stTemp = new StringTokenizer(st.get(c), " ");             
                for (int j = 0; j < redNeuronal.getCapa(k).getNumeroEntradas(); ++j){
                    redNeuronal.getCapa(k).getPesos()[i][j] = Double.parseDouble(stTemp.nextToken());
                }
                ++c;
             }                
        }
        }catch(Exception e){
            archivoValido = false; 
            //interfaz.decirMensaje("Archivo de pesos inválido");
        }                
    }
    
    
    public void setRedNeuronal(BPN rn){
        this.redNeuronal = rn;
    }
    
    
    public byte[] getMovesPattern (byte[] input){
        double [] inputVector = new double[input.length];
        for (int i = 0; i < input.length; ++i){
                inputVector[i] = input[i] == '1' ? 0.9 : 0.1; 
        }
        
        redNeuronal.setEntradas(inputVector);
        redNeuronal.propagarHaciaAdelante();
        double [] salidas = redNeuronal.getSalidas();
        
        double limiteInferior = 0.9-redNeuronal.getTolerancia();
        double limiteSuperior = 0.9+redNeuronal.getTolerancia();
        
        byte [] output = new byte[salidas.length];
        
        byte [] values = {'u','d'};
        
        for (int i = 0; i < redNeuronal.getCantidadUnidadesSalida(); ++i){
          if (salidas[i] > 0.5){
                 output[i] = values[i%2];
            }else{
              output[i] = 'x';
            }
            
        }
        
        return output;
    }
    
        
  /*  public void entrenarRedNeuronal(File archivoC){     
        final File archivoCasos = archivoC;
        interfaz.agregarAGrafico(0, true);
        
        try{
       (new Thread(){
        public void run(){
        interfaz.activarEtiquetaEspere(true); 
        interfaz.activarDesactivarComponentesParaEntrenar(false);
        boolean huboError = true;  
        int contadorIteraciones = 0;
        double promedioErrorIteraciones = 0;
        double promedioErrorIteracion = 0;       
        //File archivoSalida = interfaz.pedirArchivo(true);
        while (huboError){
            ++contadorIteraciones;
            ArrayList<String> st = manejadorArchivos.leerDesdeArchivo(archivoCasos);
                  //  System.out.println("1");
            int cantidadCasos = Integer.parseInt(st.get(0));
            huboError = false;            
            int cl = 1;
            int cantidadCasosSinAprender = 0;
            promedioErrorIteracion = 0;
            for (int k = 0; k < cantidadCasos; ++k){                    
                ++cl;                
                  //      System.out.println("2");
                double [] entrada = new double[redNeuronal.getCantidadUnidadesEntrada()];
                int c = 0;
                //System.out.println("3");
                for (int i = 0; i < CANTIDAD_FILAS; ++i){
                            
                    StringTokenizer stTemp = new StringTokenizer(st.get(cl), " ");
                    for (int j = 0; j < CANTIDAD_COLUMNAS; ++j){
                        String s = stTemp.nextToken();
                  //              System.out.println("4... iteracio " + k +" valor " + s);
                                
                        entrada[c] = Double.parseDouble(s);
                        ++c;                                        
                    }
                    ++cl;
                }
                
                    //    System.out.println("5");
                redNeuronal.setEntradas(entrada);
                      //  System.out.println("6");
                double [] salidasEsperadas = new double[redNeuronal.getCantidadUnidadesSalida()];
                        //System.out.println("7");
                StringTokenizer stTemp = new StringTokenizer(st.get(cl), " ");
                          //                  System.out.println("8");
                                            ++cl;
                for (int i = 0; i < redNeuronal.getCantidadUnidadesSalida(); ++i){

                    salidasEsperadas[i] = Double.parseDouble(stTemp.nextToken());
                }
                            //                System.out.println("9");
                redNeuronal.setSalidasEsperadas(salidasEsperadas);
                              //              System.out.println("10");
                redNeuronal.propagarHaciaAdelante();
                                //            System.out.println("11");
                redNeuronal.computarErrorSalida();
                                  //          System.out.println("12");
                promedioErrorIteracion+= redNeuronal.getPromedioErrores();
                if (redNeuronal.getError()){
                    huboError = true;
                                    //            System.out.println("13");
                    redNeuronal.propagarHaciaAtras();
                                      //          System.out.println("14");
                    redNeuronal.ajustarPesos();

                    ++cantidadCasosSinAprender;
                }                    
            }
                                        //       System.out.println("15");            
            promedioErrorIteracion = promedioErrorIteracion/cantidadCasos;
            promedioErrorIteraciones+=promedioErrorIteracion;
            if (contadorIteraciones%1 == 0){
                promedioErrorIteraciones = promedioErrorIteraciones/100;
                String mensajeError = "Iteración "+contadorIteraciones+ ". Promedio error: " + promedioErrorIteraciones + ". Cantidad casos sin aprender: "+ cantidadCasosSinAprender;
                                   //                      System.out.println("16");
               // interfaz.setTextoErrores(mensajeError);
                interfaz.graficando = true;
                interfaz.agregarAGrafico(promedioErrorIteraciones, false);
                while (interfaz.graficando){}                
                try{
                Thread.currentThread().sleep(2000);
                }catch(Exception e){}
                promedioErrorIteraciones = 0;  
                //System.out.println(mensajeError);
                //this.generarArchivoDePesos(archivoSalida);
            }
      }
      setIteracionConverge(contadorIteraciones);
      interfaz.decirMensaje("La red convergió en la iteración "+iteracionConverge+"\nPresione OK para guardar los pesos ");
      File archivo = interfaz.pedirArchivo(true);
      if (archivo != null){
        generarArchivoDePesos(archivo);
        interfaz.decirMensaje("Archivo de pesos generado correctamente");
      }
      interfaz.activarEtiquetaEspere(false); 
        interfaz.activarDesactivarComponentesParaEntrenar(true);
      }
      
      }).start();
      
      
      
                                    
      }catch(Exception e){
                                            //             System.out.println("17");
        interfaz.decirMensaje("Archivo de casos inválido");
      }
      
        
    }
    */
    
/*    public void comprobarLetra(){
        boolean [][] matriz = interfaz.getColor();
        double [] vectorEntrada = new double[redNeuronal.getCantidadUnidadesEntrada()];
        int c = 0;
        for (int i = 0; i < CANTIDAD_FILAS; ++i){
            for (int j = 0; j < CANTIDAD_COLUMNAS; ++j){
                vectorEntrada[c] = matriz[i][j] ? 0.9 : 0.1;
                ++c;
            }            
        }
        
        redNeuronal.setEntradas(vectorEntrada);
        redNeuronal.propagarHaciaAdelante();
        double [] salidas = redNeuronal.getSalidas();
        
        double limiteInferior = 0.9-redNeuronal.getTolerancia();
        double limiteSuperior = 0.9+redNeuronal.getTolerancia();
        
        for (int i = 0; i < redNeuronal.getCantidadUnidadesSalida(); ++i){
            if (salidas[i] > limiteInferior && salidas[i] < limiteSuperior){
                interfaz.setColorEtiquetaVocal(true,i);
                interfaz.setTextoCampoValorVocal(salidas[i], i);
            }else{
                interfaz.setColorEtiquetaVocal(false,i);
                interfaz.setTextoCampoValorVocal(salidas[i], i);
            }
            
        }
                
    
    }
   */ 
    
    
    
    

}   
    