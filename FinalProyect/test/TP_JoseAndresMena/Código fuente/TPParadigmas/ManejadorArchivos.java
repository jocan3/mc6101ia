import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * Clase encargada de la Entrada/Salida desde y hacia archios de texto
 * @author Andres Mena
 * @version 12 de Mayo, 2012
 */
public class ManejadorArchivos
{
    
    
    public ManejadorArchivos()
    {
        
    }

    
    public void escribirEnArchivo(File archivo, ArrayList<String> st){
        try{
            PrintWriter fos = new PrintWriter(archivo);
            for (int i = 0; i<st.size(); ++i){
                fos.println(st.get(i));
            }            
            fos.close();
        }catch(Exception e){}
    }
    
    
    public void escribirEnArchivo(File archivo, String texto){
        try{
            PrintWriter fos = new PrintWriter(archivo);
            StringTokenizer st = new StringTokenizer(texto, "\n");
            while(st.hasMoreTokens()){
                fos.println(st.nextToken());
            }
            fos.close();
        }catch(Exception e){}
    }
    
    
   /* public StringTokenizer leerDesdeArchivo(File archivo){
        StringTokenizer st = null;
        String texto = "";
        try{
            BufferedReader fis = new BufferedReader(new FileReader(archivo));
            String line = fis.readLine();
            while (line != null){
                texto+=line + "\n";
                line = fis.readLine();
            }
            fis.close();
            st = new StringTokenizer(texto,"\n");         
            
        }catch(Exception e){
         
        }       
         return st;
    }*/
    
    public ArrayList<String> leerDesdeArchivo(File archivo){
        ArrayList<String> st = new ArrayList<String>();
        String texto = "";
        try{
            BufferedReader fis = new BufferedReader(new FileReader(archivo));
            String line = fis.readLine();
            while (line != null){
                st.add(line);
                line = fis.readLine();
            }
            fis.close();
            
        }catch(Exception e){
         
        }       
         return st;
    }
    
    

    
}
