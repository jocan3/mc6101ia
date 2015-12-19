package processing.test.ketaiaudio;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ketai.sensors.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class KetaiAudio extends PApplet {




KetaiAudioInput mic;
short[] data;

int time = 0;
int wait = 3000;
int counter = 0;
int counterMeh = 0;
int counterSnapshot = 0;
int sumSnapshot = 0;
int maxSumSnapshot = 0;

String message = "";


public void setup()
{
  orientation(LANDSCAPE);
  mic = new KetaiAudioInput(this);
  fill(255,0,0);
  textSize(48);
  time = millis();
  counter = 0;
}


public void draw()
{
  background(128);
  if (data != null)
  { 
    try{
    float [] pdata = new float[data.length];
    Complex [] cdata = new Complex[1024];
     
     float [] mappedData = new float[data.length];
      
      sumSnapshot = 0;
    for (int i = 0; i < data.length; i++)
    {
       pdata[i] = data[i];
       if(i < cdata.length) cdata[i] = new Complex(data[i],0);
      
      
      // sumSnapshot += data[i];
      if(i != data.length-1){
        line(i, map(data[i], -32768, 32767,height,0), i+1, map(data[i+1], -32768, 32767,height,0));
          mappedData[i] = map(data[i], -32768, 32767,height,0);
          mappedData[i+1] = map(data[i+1], -32768, 32767,height,0);
       }
  
    }
 
 
    int [] presult = findPeaksAndValleys(mappedData);
    
    message = "Num peaks: " + presult.length;
    
    for (int i = 0; i < presult.length; i++){  
      sumSnapshot += Math.abs(mappedData[presult[i]]);// < 0) ? mappedData[presult[i]]*-1:mappedData[presult[i]]; 
      //ellipse(presult[i],map(data[presult[i]], -32768, 32767,2*(height/3),(height/3)),10,10);
    }
    
    if (sumSnapshot > maxSumSnapshot) maxSumSnapshot = sumSnapshot;  
    /*
    FFT f = new FFT();
    Complex [] cresult = f.fft(cdata);
    
    for (int i = 0; i < cresult.length/2; i++)
    {  
      if(i != cresult.length-1)
        line(i*2, map((float)cresult[i].re, -32768, 32768,height,2*(height/3)), (i+1)*2, map((float)cresult[i+1].re, -32768, 32767,height,2*(height/3)));
    }*/
    
    
    
    if (millis()-time < wait){
      counter += data.length;
    }else{
      time = millis();
      counterMeh = counter;
      counterSnapshot = data.length;
      counter = 0;
    }
    }catch (Exception e){
      message = "Error: "+e;;      
    }
    
  }
  
  if(mic.isActive()){
    text("READING MIC", width/2, height/2);
    text("Max sum snapshot: " + maxSumSnapshot, (width/2), (height/2)+(height/6));
    text("sum snapshot Length: " + sumSnapshot, (width/2), (height/2)+2*(height/6));
    text("Log: " + message, (width/4), (height/2)+3*(height/6));
  }
  else
    text("NOT READING MIC", width/2, height/2);
  
}

public int [] findPeaksAndValleys(float [] array){
  int [] indexes = new int[array.length];
  
  int numPeaks = 0;
  
  for (int i = 0; i < array.length; ++i){
    if ((i < array.length-1)&&(i > 0)){
      if ((array[i-1] < array[i] && array[i] > array[i+1]) || (array[i-1] > array[i] && array[i] < array[i+1])){
        indexes[numPeaks] = i;
        ++numPeaks;
      } 
    }
  }
  int [] result = new int[numPeaks];
  for (int i = 0; i < result.length; ++i) result[i] = indexes[i];
  return result;
}


public int [] findPeaksAndValleys(short [] array){
  int [] indexes = new int[array.length];
  
  int numPeaks = 0;
  
  for (int i = 0; i < array.length; ++i){
    if ((i < array.length-1)&&(i > 0)){
      if ((array[i-1] < array[i] && array[i] > array[i+1]) || (array[i-1] > array[i] && array[i] < array[i+1])){
        indexes[numPeaks] = i;
        ++numPeaks;
      } 
    }
  }
  int [] result = new int[numPeaks];
  for (int i = 0; i < result.length; ++i) result[i] = indexes[i];
  return result;
}

public void onAudioEvent(short[] _data)
{
  data= _data;
}

public void mousePressed()
{
  maxSumSnapshot = 0;
  
  if (mic.isActive())
    mic.stop(); 
  else
    mic.start();
}
/******************************************************************************
 *  Compilation:  javac Complex.java
 *  Execution:    java Complex
 *
 *  Data type for complex numbers.
 *
 *  The data type is "immutable" so once you create and initialize
 *  a Complex object, you cannot change it. The "final" keyword
 *  when declaring re and im enforces this rule, making it a
 *  compile-time error to change the .re or .im fields after
 *  they've been initialized.
 *
 *  % java Complex
 *  a            = 5.0 + 6.0i
 *  b            = -3.0 + 4.0i
 *  Re(a)        = 5.0
 *  Im(a)        = 6.0
 *  b + a        = 2.0 + 10.0i
 *  a - b        = 8.0 + 2.0i
 *  a * b        = -39.0 + 2.0i
 *  b * a        = -39.0 + 2.0i
 *  a / b        = 0.36 - 1.52i
 *  (a / b) * b  = 5.0 + 6.0i
 *  conj(a)      = 5.0 - 6.0i
 *  |a|          = 7.810249675906654
 *  tan(a)       = -6.685231390246571E-6 + 1.0000103108981198i
 *
 ******************************************************************************/

public class Complex {
    public final double re;   // the real part
    public final double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    // return a string representation of the invoking Complex object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    // return abs/modulus/magnitude and angle/phase/argument
    public double abs()   { return Math.hypot(re, im); }  // Math.sqrt(re*re + im*im)
    public double phase() { return Math.atan2(im, re); }  // between -pi and pi

    // return a new Complex object whose value is (this + b)
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this - b)
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this * b)
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    public Complex times(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // return a new Complex object whose value is the conjugate of this
    public Complex conjugate() {  return new Complex(re, -im); }

    // return a new Complex object whose value is the reciprocal of this
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    // return the real or imaginary part
    public double re() { return re; }
    public double im() { return im; }

    // return a / b
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    // return a new Complex object whose value is the complex exponential of this
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // return a new Complex object whose value is the complex sine of this
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex cosine of this
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex tangent of this
    public Complex tan() {
        return sin().divides(cos());
    }
    


    // a static version of plus
    public Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }





}

/******************************************************************************
 *  Compilation:  javac FFT.java
 *  Execution:    java FFT N
 *  Dependencies: Complex.java
 *
 *  Compute the FFT and inverse FFT of a length N complex sequence.
 *  Bare bones implementation that runs in O(N log N) time. Our goal
 *  is to optimize the clarity of the code, rather than performance.
 *
 *  Limitations
 *  -----------
 *   -  assumes N is a power of 2
 *
 *   -  not the most memory efficient algorithm (because it uses
 *      an object type for representing complex numbers and because
 *      it re-allocates memory for the subarray, instead of doing
 *      in-place or reusing a single temporary array)
 *  
 ******************************************************************************/

public class FFT {

    // compute the FFT of x[], assuming its length is a power of 2
    public Complex[] fft(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }

        // fft of even terms
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }


    // compute the inverse FFT of x[], assuming its length is a power of 2
    public Complex[] ifft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].times(1.0f / N);
        }

        return y;

    }

    // compute the circular convolution of x and y
    public Complex[] cconvolve(Complex[] x, Complex[] y) {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.length != y.length) { throw new RuntimeException("Dimensions don't agree"); }

        int N = x.length;

        // compute FFT of each sequence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT
        return ifft(c);
    }


    // compute the linear convolution of x and y
    public Complex[] convolve(Complex[] x, Complex[] y) {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2*x.length];
        for (int i = 0;        i <   x.length; i++) a[i] = x[i];
        for (int i = x.length; i < 2*x.length; i++) a[i] = ZERO;

        Complex[] b = new Complex[2*y.length];
        for (int i = 0;        i <   y.length; i++) b[i] = y[i];
        for (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;

        return cconvolve(a, b);
    }

    // display an array of Complex numbers to standard output
    public void show(Complex[] x, String title) {
      OutputStream os = createOutput("log.txt");
      PrintWriter writer = null;
      try{
      writer = new PrintWriter("log.txt", "UTF-8");    
      }catch(Exception e){}
        System.out.println(title);
        System.out.println("-------------------");
        
        for (int i = 0; i < x.length; i++) {
          writer.println(i + " " + x[i].re);
          System.out.println(x[i].re);
        }
        System.out.println();
        writer.flush();
        writer.close();
    }
  

}
/**
 * A standard peak detector in time series.
 * <p>
 * The goal of this class is to identify peaks in a 1D time series (float[]).
 * It simply implements G.K. Palshikar's <i>Simple Algorithms for Peak Detection 
 * in Time-Series</i> ( Proc. 1st Int. Conf. Advanced Data Analysis, Business 
 * Analytics and Intelligence (ICADABAI2009), Ahmedabad, 6-7 June 2009),
 * We retained the first "spikiness" function he proposed, based on computing
 * the max signed distance to left and right neighbors.
 * <p>
 * <pre> 
 *              http://sites.google.com/site/girishpalshikar/Home/mypublications/
 *              SimpleAlgorithmsforPeakDetectioninTimeSeriesACADABAI_2009.pdf
 * </pre>
 * @author Jean-Yves Tinevez <jeanyves.tinevez@gmail.com> May 10, 2011
 */
public class PeakDetector {

        private float[] T;

        /**
         * Create a peak detector for the given time series.
         */
        public PeakDetector(final float[] timeSeries) {
                this.T = timeSeries; 
        }
        
        /**
         * Return the peak locations as array index for the time series set at creation.
         * @param windowSize  the window size to look for peaks. a neighborhood of +/- windowSize
         * will be inspected to search for peaks. Typical values start at 3.
         * @param stringency  threshold for peak values. Peak with values lower than <code>
         * mean + stringency * std</code> will be rejected. <code>Mean</code> and <code>std</code> are calculated on the 
         * spikiness function. Typical values range from 1 to 3.
         * @return an int array, with one element by retained peak, containing the index of 
         * the peak in the time series array.
         */
        public int[] process(final int windowSize, final float stringency) {
                
                // Compute peak function values
                float[] S = new float[T.length];
                float maxLeft, maxRight;
                for (int i = windowSize; i < S.length - windowSize; i++) {
                        
                        maxLeft = T[i] - T[i-1];
                        maxRight = T[i] - T[i+1];
                        for (int j = 2; j <= windowSize; j++) {
                                if (T[i]-T[i-j] > maxLeft)
                                        maxLeft = T[i]-T[i-j];
                                if (T[i]-T[i+j] > maxRight)
                                        maxRight = T[i]-T[i+j];
                        }
                        S[i] = 0.5f * (maxRight + maxLeft);
                        
                }
                
                // Compute mean and std of peak function
                float mean = 0;
                int n = 0;
            float M2 = 0;
            float delta;
            for (int i = 0; i < S.length; i++) {
                n = n + 1;
                delta = S[i] - mean;
                mean = mean + delta/n;
                M2 = M2 + delta*(S[i] - mean) ;
            }

            float variance = M2/(n - 1);
            float std = (float) Math.sqrt(variance);
            
            // Collect only large peaks
            ArrayList<Integer> peakLocations = new ArrayList<Integer>();
            for (int i = 0; i < S.length; i++) {
                if (S[i] > 0 && (S[i]-mean) > stringency * std) {
                        peakLocations.add(i);
                }
                }
            
            // Remove peaks too close
            ArrayList<Integer> toPrune = new ArrayList<Integer>();
            int peak1, peak2, weakerPeak;
            for (int i = 0; i < peakLocations.size()-1; i++) {
                        peak1 = peakLocations.get(i);
                        peak2 = peakLocations.get(i+1);
                        
                        if (peak2 - peak1 < windowSize) {
                                // Too close, prune the smallest one
                                if (T[peak2] > T[peak1])
                                        weakerPeak = peak1;
                                else 
                                        weakerPeak = peak2;
                                toPrune.add(weakerPeak);
                        }
                }
            peakLocations.removeAll(toPrune);
            
            // Convert to int[]
            int[] peakArray = new int[peakLocations.size()];
            for (int i = 0; i < peakArray.length; i++) {
                        peakArray[i] = peakLocations.get(i);
                }
            return peakArray;
        }
        
        
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "KetaiAudio" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
