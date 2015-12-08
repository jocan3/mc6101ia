import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.ArrayList;

 
public class Graficador extends JPanel {
    double[] data;
    final int PAD = 20;
 
    public Graficador(ArrayList<String> numeros){
        data = new double[numeros.size()];
        for (int i = 0; i < numeros.size();++i){
            data[i] = Double.parseDouble(numeros.get(i));
        }
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        // Draw labels.
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();
        // Ordinate label.
        String s = "Error";
        float sy1 = 5+lm.getDescent()*2;
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx1 = (PAD - sw)/2+5;
        g2.drawString(s, sx1, sy1);
        
        
        // Abcissa label.
        s = "Iteraciones";
        float sy2 = h - PAD + (PAD - sh)/2 + lm.getAscent();
        sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx2 = (w - sw)/2;
        //g2.drawString(s, sx2, sy2+10);
        // Draw lines.
        double xInc = (double)(w - 2*PAD)/(data.length-1);
        double scale = (double)(h - 2*PAD)/getMax();
        g2.setPaint(Color.green.darker());
        for(int i = 0; i < data.length-1; i++) {
            double x1 = PAD + i*xInc;
            double y1 = h - PAD - scale*data[i];
            double x2 = PAD + (i+1)*xInc;
            double y2 = h - PAD - scale*data[i+1];
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
            if (i%3 == 0){
                String num = (i+1)+"";
                g2.drawString(num, (float)x1, sy2);
               
                
            }
            if (i%11 == 0){                    
                    String num = data[i+1] +"";
                    g2.drawString(num, sx1, (float)y2);
                    
            }
            
            
            
        }
        
        
        // Mark data points.
        g2.setPaint(Color.red);
        for(int i = 0; i < data.length; i++) {
            double x = PAD + i*xInc;
            double y = h - PAD - scale*data[i];
            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
        }
    }
 
    private double getMax() {
        double max = -Integer.MAX_VALUE;
        for(int i = 0; i < data.length; i++) {
            if(data[i] > max)
                max = data[i];
        }
        return max;
    }
    
    
    
}