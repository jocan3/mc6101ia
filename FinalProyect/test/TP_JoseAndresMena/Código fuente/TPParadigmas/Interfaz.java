import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JOptionPane;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.ScrollPaneLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.lang.Thread;
import java.util.ArrayList;

/**
 * Interfaz tarea programada paradigmas
 */

public class Interfaz extends JFrame
{
    private Boton[][] matriz;
    private boolean[][] color;
    private JPanel panelEntrenar;
    private JPanel panelUsar;
    private JPanel panelGridUsar;
    private JPanel panelGraficar;
    private JMenuBar barraMenu;
    private JMenu [] menues;
    private Graficador graficador;
    private ArrayList<String> errores;
    
    private Controlador controlador;
    private Dibujador dibujador;
    private String comandoBotones;
    private int CANTIDAD_FILAS;
    private int CANTIDAD_COLUMNAS;
    private String nombreImagenFondo;
    private JFileChooser fileChooser;
    private File archivoCasos;
    
    private JLabel tituloEntrenar;
    private JLabel etiquetaAlpha;
    private JLabel etiquetaNumeroUnidadesCapaOculta;
    private JLabel etiquetaTolerancia;
    private JLabel etiquetaArchivoCasos;
    private JTextField campoAlpha;
    private JTextField campoNumeroUnidadesCapaOculta;
    private JTextField campoTolerancia;
    private JTextField campoArchivoCasos;
    private JButton botonCargarArchivoCasos;
    private JTextArea textoErrores;
    private JButton botonEntrenar;
    private JLabel etiquetaEspere;
    
    private JLabel tituloUsar;
    private JButton botonCargarCaso;
    private JButton botonLimpiarMatriz;
    private JButton botonCargarArchivoPesos;
    private JButton botonComprobar;
    private JLabel etiquetaVocalReconocida;        
    private JLabel [] vocales;
    private JTextField [] valorVocales;
    
    public boolean graficando;
    
    
    public void agregarAGrafico(double v, boolean r){
        final boolean reset = r;
        final double valor = v;
        //(new Thread(){
        //public void run(){
        if (graficador != null){
            panelGraficar.remove(graficador);
        }
        if (reset){
            errores.clear();
        }else{
            //NumberFormat nf = new DecimalFormat("#0.00");
            //errores.add(nf.format(valor));       
            errores.add(""+valor);       
        }        
        graficador = new Graficador(errores);       
        graficador.setSize(150+150,25+275);
        graficador.setLocation(0,0);
        panelGraficar.add(graficador);
        graficador.setVisible(true);
        panelGraficar.setVisible(false);
        panelGraficar.setVisible(true);
        
        
        try{
                //Thread.currentThread().sleep(3000);
                }catch(Exception e){}
        graficando = false;
        
        //}
        
        
        //}).start();
    }
    
    
    
     public Interfaz(Controlador c, int ancho, int alto, String[][] menu, String nombreArchivoImagen, int cantidadFilas, int cantidadColumnas, String textoComandoBotones, Dibujador d){
           dibujador = d;
           controlador = c;
           CANTIDAD_FILAS = cantidadFilas;
           CANTIDAD_COLUMNAS = cantidadColumnas;
           nombreImagenFondo = nombreArchivoImagen;
           comandoBotones = textoComandoBotones;
           fileChooser = new JFileChooser();
           errores = new ArrayList<String>();
         
           this.setSize(ancho, alto);
           this.setLocationRelativeTo(null);
           this.setLayout(new BorderLayout());
           this.setDefaultCloseOperation(EXIT_ON_CLOSE);
           
           barraMenu = new JMenuBar();
           this.menues = new JMenu[menu.length];
           for (int indiceMenu=0;indiceMenu<menu.length; indiceMenu++) {
              menues[indiceMenu] = new JMenu(menu[indiceMenu][0]);
              barraMenu.add(menues[indiceMenu]);        
              for (int indiceItem=1; indiceItem<menu[indiceMenu].length; indiceItem++) {
                String etiquetaItem = menu[indiceMenu][indiceItem];
                if (etiquetaItem == null) {
                  menues[indiceMenu].addSeparator();
                }else{
                  JMenuItem item = new JMenuItem(etiquetaItem);
                  item.addActionListener(c);
                  menues[indiceMenu].add(item);
                }
              }
            }
            this.setJMenuBar(barraMenu);                    
     }
     
     
     public void agregarPanelEntrenar(){
        panelEntrenar = new JPanel();   
        panelEntrenar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        panelEntrenar.setLayout(null);
                                      
       this.add(panelEntrenar, BorderLayout.CENTER);
        
        int xi=10;
        int yi=100;
        int h=25;
        int w=150;
        
        tituloEntrenar = new JLabel("ENTRENAR LA RED NEURONAL");
        tituloEntrenar.setFont(new java.awt.Font("Times new roman", 1, 25));
        tituloEntrenar.setForeground(java.awt.Color.BLACK);
        tituloEntrenar.setBounds(xi,yi-95,w+300,h);                
        panelEntrenar.add(tituloEntrenar);        
        
        etiquetaAlpha = new JLabel("Alpha:");
        campoAlpha = new JTextField();
        etiquetaAlpha.setFont(new java.awt.Font("Dialog", 1, 15));
        etiquetaAlpha.setForeground(java.awt.Color.BLACK);
        etiquetaAlpha.setBounds(xi,yi,w,h);        
        campoAlpha.setBounds(xi + 130,yi,w-75,h);
        panelEntrenar.add(etiquetaAlpha);
        panelEntrenar.add(campoAlpha);
        
        etiquetaTolerancia = new JLabel("Tolerancia:");
        campoTolerancia = new JTextField();
        etiquetaTolerancia.setFont(new java.awt.Font("Dialog", 1, 15));
        etiquetaTolerancia.setForeground(java.awt.Color.BLACK);
        etiquetaTolerancia.setBounds(xi,yi+70,w,h);        
        campoTolerancia.setBounds(xi + 130,yi + 70,w-75,h);
        panelEntrenar.add(etiquetaTolerancia);
        panelEntrenar.add(campoTolerancia);
        
        etiquetaNumeroUnidadesCapaOculta = new JLabel("<html>Unidades de<br />la capa oculta:</html>");
        campoNumeroUnidadesCapaOculta = new JTextField();
        etiquetaNumeroUnidadesCapaOculta.setFont(new java.awt.Font("Dialog", 1, 15));
        etiquetaNumeroUnidadesCapaOculta.setForeground(java.awt.Color.BLACK);
        etiquetaNumeroUnidadesCapaOculta.setBounds(xi,yi + 140,w,h+20);        
        campoNumeroUnidadesCapaOculta.setBounds(xi + 130,yi + 140,w-75,h);
        panelEntrenar.add(etiquetaNumeroUnidadesCapaOculta);
        panelEntrenar.add(campoNumeroUnidadesCapaOculta);
        
        etiquetaArchivoCasos = new JLabel("<html>Archivo<br />de casos:</html>");
        campoArchivoCasos = new JTextField();
        etiquetaArchivoCasos.setFont(new java.awt.Font("Dialog", 1, 15));
        etiquetaArchivoCasos.setForeground(java.awt.Color.BLACK);
        etiquetaArchivoCasos.setBounds(xi,yi+210,w,h+20);           
        campoArchivoCasos.setBounds(xi + 88,yi + 210,w,h);
        campoArchivoCasos.setEnabled(false);
        campoArchivoCasos.setForeground(java.awt.Color.BLACK);
        panelEntrenar.add(etiquetaArchivoCasos);
        panelEntrenar.add(campoArchivoCasos);
        botonCargarArchivoCasos = new JButton("Cargar archivo");
        botonCargarArchivoCasos.setBounds(xi+240,yi+210,w-30,h);
        botonCargarArchivoCasos.addActionListener(controlador);
        panelEntrenar.add(botonCargarArchivoCasos);
        
        botonEntrenar = new JButton("Entrenar");
        botonEntrenar.addActionListener(controlador);              
        botonEntrenar.setBackground(java.awt.Color.BLACK);
        botonEntrenar.setForeground(java.awt.Color.WHITE);        
        botonEntrenar.setBounds(xi + 145,yi+280,w,h);
        panelEntrenar.add(botonEntrenar);  
       
        panelGraficar = new JPanel();
        panelGraficar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        panelGraficar.setLayout(null);
        panelGraficar.setBorder(BorderFactory.createLineBorder(Color.black));
        panelGraficar.setBounds(xi + 375,yi,w+150,h+275);
        panelEntrenar.add(panelGraficar);  
        
        graficador = null;
        this.agregarAGrafico(0,true);        
        
        panelGraficar.setVisible(true);
        
        JLabel iteraciones = new JLabel("Iteraciones");
        iteraciones.setFont(new java.awt.Font("Dialog", 1, 12));
        iteraciones.setForeground(java.awt.Color.BLACK);
        iteraciones.setBounds(xi+375+(h+275)/2 - 19,yi+(w+150),w,h+20);
        panelEntrenar.add(iteraciones);
        
        etiquetaEspere = new JLabel("Espere mientras se entrena la red...");
      
        etiquetaEspere.setFont(new java.awt.Font("Dialog", 1, 16));
        etiquetaEspere.setForeground(java.awt.Color.BLUE);
        etiquetaEspere.setBounds(xi+375,yi-50,w+150,h);        
        panelEntrenar.add(etiquetaEspere);
        etiquetaEspere.setVisible(false);
        
        
        /*
        textoErrores = new JTextArea();
        textoErrores.setBounds(0,0,w+150,h+275);
        panelGraficar.add(textoErrores);*/
        
        panelEntrenar.setVisible(true);
        
     }
     
     public void agregarPanelUsar(){
        panelUsar = new JPanel();   
        panelUsar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        panelUsar.setLayout(null);
                                      
        this.add(panelUsar, BorderLayout.CENTER);
        
        int xi=200;
        int yi=75;
        int h=25;
        int w=150;
        
        tituloUsar = new JLabel("PROBAR LA RED NEURONAL");
        tituloUsar.setFont(new java.awt.Font("Times new roman", 1, 25));
        tituloUsar.setForeground(java.awt.Color.BLACK);
        tituloUsar.setBounds(xi-190,yi-70,w+300,h);                
        panelUsar.add(tituloUsar);        
                                
        botonCargarCaso = new JButton("Cargar caso");
        botonCargarCaso.setBounds(xi+10,yi-5,w,h);                
        botonCargarCaso.addActionListener(controlador);              
        botonCargarCaso.setBackground(java.awt.Color.BLACK);
        botonCargarCaso.setForeground(java.awt.Color.WHITE);        
        panelUsar.add(botonCargarCaso);
        
        botonLimpiarMatriz = new JButton("Limpiar matriz");
        botonLimpiarMatriz.setBounds(xi-140,yi-5,w,h);                
        botonLimpiarMatriz.addActionListener(controlador);              
        botonLimpiarMatriz.setBackground(java.awt.Color.BLACK);
        botonLimpiarMatriz.setForeground(java.awt.Color.WHITE);        
        panelUsar.add(botonLimpiarMatriz);
        
        botonCargarArchivoPesos = new JButton("Cargar pesos");
        botonCargarArchivoPesos.setBounds(xi+255,yi-5,w,h);                
        botonCargarArchivoPesos.addActionListener(controlador);              
        botonCargarArchivoPesos.setBackground(java.awt.Color.BLACK);
        botonCargarArchivoPesos.setForeground(java.awt.Color.WHITE);        
        panelUsar.add(botonCargarArchivoPesos);
        
        panelGridUsar = new JPanel();
        panelGridUsar.setLayout(new GridLayout(CANTIDAD_FILAS, CANTIDAD_COLUMNAS));
        panelGridUsar.setBounds(xi-140,yi+35,w+150,h+275);
        panelUsar.add(panelGridUsar);                
        
        matriz = new Boton[CANTIDAD_FILAS][CANTIDAD_COLUMNAS];
        color = new boolean[CANTIDAD_FILAS][CANTIDAD_COLUMNAS];
        Boton b;
        for (int i = 0; i < CANTIDAD_FILAS; ++i){
            for (int j = 0; j < CANTIDAD_COLUMNAS; ++j){
                b = new Boton(i,j);
                b.setActionCommand("Boton");
                b.addActionListener(controlador);
                b.addMouseListener(dibujador);
                b.setBackground(java.awt.Color.white);
                //b.setOpaque(false);
                //b.setContentAreaFilled(true);
                panelGridUsar.add(b);                
                matriz[i][j] = b;
                color[i][j] = false;
                //System.out.println(matriz[i][j].getActionCommand());
            }
        }
        //panelUsar.add(matriz[0][0]);
        
        botonComprobar = new JButton("Comprobar letra");       
        botonComprobar.setBounds(xi+255,yi+35,w,h);                
        botonComprobar.addActionListener(controlador);              
        botonComprobar.setBackground(java.awt.Color.BLACK);
        botonComprobar.setForeground(java.awt.Color.WHITE);        
        panelUsar.add(botonComprobar);
        
        
        
        vocales = new JLabel[10];
        valorVocales = new JTextField[10];
        
        vocales[0] = new JLabel("A");
        vocales[1] = new JLabel("E");
        vocales[2] = new JLabel("I");
        vocales[3] = new JLabel("O");
        vocales[4] = new JLabel("U");
        vocales[5] = new JLabel("a");
        vocales[6] = new JLabel("e");
        vocales[7] = new JLabel("i");
        vocales[8] = new JLabel("o");
        vocales[9] = new JLabel("u");
        
        int yl = 75;
        for (int i = 0; i < 10; ++i){
            vocales[i].setFont(new java.awt.Font("Dialog", 1, 15));
            vocales[i].setForeground(java.awt.Color.BLACK);
            vocales[i].setBounds(xi+400,yi+yl,w,h);                
            panelUsar.add(vocales[i]);
            valorVocales[i] = new JTextField();
            valorVocales[i].setBounds(xi+250,yi+yl,w-20,h);
            valorVocales[i].setEnabled(false);
            valorVocales[i].setForeground(java.awt.Color.BLACK);
            panelUsar.add(valorVocales[i]);       
            yl += 25;
        }                
     }
     
     
     
     public void quitarPaneles(){      
        if (panelEntrenar != null){
             this.remove(panelEntrenar); 
             panelEntrenar = null;
        }
        if (panelUsar != null){
             this.remove(panelUsar); 
             panelUsar = null;
        }
        
     }
     
      public void quitarPanelUsar(){
        if (panelUsar != null){
            this.remove(panelUsar);   
            panelUsar = null;
        }
     }
     
     public void activarEtiquetaEspere(boolean activar){
        etiquetaEspere.setVisible(activar);   
     }
     
     public void setArchivoCasos(File archivo){
        archivoCasos = archivo;   
     }
     
     public Object[] getDatosEntrenamiento(){
        Object[] datosEntrenamiento = null;
        if ((!campoAlpha.getText().equals(""))&&(!campoTolerancia.getText().equals(""))&&(!campoNumeroUnidadesCapaOculta.getText().equals(""))&&(!campoArchivoCasos.getText().equals(""))){
            datosEntrenamiento = new Object[4];
            datosEntrenamiento[0] = campoAlpha.getText();
            datosEntrenamiento[1] = campoTolerancia.getText();
            datosEntrenamiento[2] = campoNumeroUnidadesCapaOculta.getText();                        
            datosEntrenamiento[3] = archivoCasos;                        
        }                
        return datosEntrenamiento;
    }
     
     
     
     public void setCampoArchivoCasos(String texto){
         campoArchivoCasos.setText(texto);
     }
     
        
     public void decirMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);   
     }
        
     public void activar(boolean verdaderoFalso){
          this.setVisible(verdaderoFalso);
     }
     
     public void cambiarColorBoton(Object o){
        Boton b = (Boton)o;
        int i = (int)b.getFila();
        int j = (int)b.getColumna();
        //this.decirMensaje("boton "+ i +"," + j);
        
        if (color[i][j]){
            matriz[i][j].setBackground(java.awt.Color.white);
            color[i][j] = false;
        }else{
            matriz[i][j].setBackground(java.awt.Color.black);
            color[i][j] = true;
        }
        
        matriz[i][j].setEnabled(false);
        matriz[i][j].setEnabled(true);
         
     }
     
     public File pedirArchivo(boolean guardarComo){
        int returnVal = 0;
        File archivo = null;
        if(guardarComo){
            returnVal = fileChooser.showSaveDialog(this);
        }else{
            returnVal = fileChooser.showOpenDialog(this);
        }
        if(returnVal == JFileChooser.APPROVE_OPTION){
            archivo = fileChooser.getSelectedFile();
        }
        return archivo;
    }
    
    
    public boolean [][] getColor(){
        return color;
    }
    
    
    public void setColorEtiquetaVocal(boolean color, int indice){
        if (color){
            vocales[indice].setForeground(java.awt.Color.RED);
        }else{
            vocales[indice].setForeground(java.awt.Color.BLACK);
        }        
    } 
    
    public void setTextoCampoValorVocal(double v, int i){
        final double valor = v;
        final int indice = i;
        (new Thread(){
        public void run(){
        NumberFormat nf = new DecimalFormat("#0.00000000000000");
        valorVocales[indice].setText(nf.format(valor));       
        }
        }).start();
    }
    
    public void cargarCaso(boolean [][] caso){
        for (int i = 0; i < CANTIDAD_FILAS; ++i){
            for (int j = 0; j < CANTIDAD_FILAS; ++j){
                if (!caso[i][j]){
                    color[i][j] = false;
                    matriz[i][j].setBackground(java.awt.Color.white);                
                }else{
                    color[i][j] = true;
                    matriz[i][j].setBackground(java.awt.Color.black);                
                }
            }
        } 
    }
    
    
    public void activarDesactivarComponentesParaEntrenar(boolean valor){
        campoAlpha.setEnabled(valor);
        campoTolerancia.setEnabled(valor);
        campoNumeroUnidadesCapaOculta.setEnabled(valor);
        botonCargarArchivoCasos.setEnabled(valor);
        botonEntrenar.setEnabled(valor);
        barraMenu.setEnabled(valor);
    }
    
    
    public void setTextoErrores(String texto){
        textoErrores.setText(textoErrores.getText()+ texto+"\n");
    }
    
    public void limpiarMatriz(){
        for (int i = 0; i < CANTIDAD_FILAS; ++i){
            for (int j = 0; j < CANTIDAD_FILAS; ++j){
                color[i][j] = false;
                matriz[i][j].setBackground(java.awt.Color.white);                
            }
        }        
    }
    
     

}

