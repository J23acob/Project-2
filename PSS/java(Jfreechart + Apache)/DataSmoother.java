/*
   DataSmoother.java
   • Reads a CSV of (x, y)
   • Replaces each y with the average of neighbours inside ±window
   • Exports smoothed points to smoothed_data.csv
   • Shows original vs. smoothed curves via JFreeChart
*/

   import java.io.BufferedReader;
   import java.io.FileReader;
   import java.io.FileWriter;
   import java.io.PrintWriter;
   import java.io.IOException;
   import java.util.ArrayList;
   import java.util.List;
   
   /* JFreeChart & Swing */
   import org.jfree.chart.ChartFactory;
   import org.jfree.chart.ChartPanel;
   import org.jfree.chart.JFreeChart;
   import org.jfree.chart.plot.PlotOrientation;
   import org.jfree.data.xy.XYSeries;
   import org.jfree.data.xy.XYSeriesCollection;
   
   import javax.swing.JFrame;
   
   public class DataSmoother {
   
       /** Reads "X,Y" CSV into List<double[]>; skips header row if present. */
       public static List<double[]> readCSV(String file) {
           List<double[]> pts = new ArrayList<>();
           try (BufferedReader br = new BufferedReader(new FileReader(file))) {
               String line;
               boolean first = true;
               while ((line = br.readLine()) != null) {
                   if (first && line.toLowerCase().contains("x")) { // header
                       first = false;
                       continue;
                   }
                   String[] parts = line.split(",");
                   if (parts.length < 2) continue;
                   pts.add(new double[] {
                       Double.parseDouble(parts[0].trim()),
                       Double.parseDouble(parts[1].trim())
                   });
                   first = false;
               }
           } catch (IOException ex) {
               ex.printStackTrace();
           }
           return pts;
       }
   
       /** Writes list of {x,y} to CSV. */
       public static void writeCSV(List<double[]> pts, String file) {
           try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
               pw.println("X,Y");
               for (double[] p : pts) pw.println(p[0] + "," + p[1]);
               System.out.printf("CSV written: %s  (%d rows)%n", file, pts.size());
           } catch (IOException ex) {
               ex.printStackTrace();
           }
       }
   
       /** Returns a new list whose y-values are smoothed with window ±k. */
       public static List<double[]> smooth(List<double[]> src, int window) {
           int n = src.size();
           List<double[]> out = new ArrayList<>(n);
   
           for (int i = 0; i < n; i++) {
               int left  = Math.max(0, i - window);
               int right = Math.min(n - 1, i + window);
   
               double sum = 0;
               for (int j = left; j <= right; j++) sum += src.get(j)[1];
               double avg = sum / (right - left + 1);
   
               out.add(new double[] { src.get(i)[0], avg });
           }
           return out;
       }
   
       /** Displays original & smoothed series in the same chart. */
       public static void showChart(List<double[]> original,
                                    List<double[]> smoothed,
                                    int window) {
   
           XYSeries rawSeries = new XYSeries("original");
           for (double[] p : original) rawSeries.add(p[0], p[1]);
   
           XYSeries smoothSeries = new XYSeries("smoothed (±" + window + ")");
           for (double[] p : smoothed) smoothSeries.add(p[0], p[1]);
   
           XYSeriesCollection dataset = new XYSeriesCollection();
           dataset.addSeries(rawSeries);
           dataset.addSeries(smoothSeries);
   
           JFreeChart chart = ChartFactory.createXYLineChart(
                   "Moving-average smoothing (window ±" + window + ")",
                   "x",
                   "y",
                   dataset,
                   PlotOrientation.VERTICAL,
                   true,   // legend
                   false,
                   false);
   
           JFrame f = new JFrame("Data Smoother");
           f.setContentPane(new ChartPanel(chart));
           f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           f.pack();
           f.setLocationRelativeTo(null);
           f.setVisible(true);
       }
   
       /* main */
   
       public static void main(String[] args) {
   
           String inputCsv  = "plot_data.csv";      // produced by DataPlotter
           String outputCsv = "smoothed_data.csv";
           int    window    = 5;                    // ±5 points
   
           // allow overrides from command-line
           if (args.length >= 1) window = Integer.parseInt(args[0]);
           if (args.length >= 2) inputCsv = args[1];
           if (args.length >= 3) outputCsv = args[2];
   
           List<double[]> original = readCSV(inputCsv);
           if (original.isEmpty()) {
               System.err.println("No data loaded from " + inputCsv);
               return;
           }
   
           List<double[]> smoothed = smooth(original, window);
           writeCSV(smoothed, outputCsv);
   
           showChart(original, smoothed, window);
       }
   }
   