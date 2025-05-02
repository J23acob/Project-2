/*
   DataSalter.java
   • Reads a CSV of (x, y) pairs
   • Adds uniformly distributed noise ±range to each y
   • Computes a SimpleRegression on the noisy data (Commons Math)
   • Exports salted_data.csv
   • Shows original, salted, and regression‑line curves via JFreeChart
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* JFreeChart & Swing */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;

/* Apache Commons Math */
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class DataSalter {

    /** Reads "X,Y" CSV into List<double[]>; skips header if present. */
    public static List<double[]> readCSV(String file) {
        List<double[]> pts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first && line.toLowerCase().contains("x")) { first = false; continue; }
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                pts.add(new double[] {
                    Double.parseDouble(parts[0].trim()),
                    Double.parseDouble(parts[1].trim())
                });
                first = false;
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        return pts;
    }

    /** Writes list of {x,y} to CSV with header. */
    public static void writeCSV(List<double[]> pts, String file) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("X,Y");
            for (double[] p : pts) pw.println(p[0] + "," + p[1]);
            System.out.printf("CSV written: %s  (%d rows)%n", file, pts.size());
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    /** Returns a new list with y-values salted by uniform noise ±range. */
    public static List<double[]> salt(List<double[]> src, double range, Long seed) {
        Random rng = (seed == null) ? new Random() : new Random(seed);
        List<double[]> out = new ArrayList<>(src.size());
        for (double[] p : src) {
            double noise = (rng.nextDouble() * 2 * range) - range; // (-range, +range)
            out.add(new double[] { p[0], p[1] + noise });
        }
        return out;
    }

    /** Displays original, salted, and regression line together. */
    public static void showChart(List<double[]> original,
                                 List<double[]> salted,
                                 XYSeries regSeries,
                                 double range) {

        XYSeries rawSeries = new XYSeries("original");
        for (double[] p : original) rawSeries.add(p[0], p[1]);

        XYSeries saltSeries = new XYSeries("salted (±" + range + ")");
        for (double[] p : salted) saltSeries.add(p[0], p[1]);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(rawSeries);
        dataset.addSeries(saltSeries);
        dataset.addSeries(regSeries);  // regression line

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Salted data ±" + range + " and linear fit",
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                true, false, false);

        JFrame f = new JFrame("Data Salter + Regression");
        f.setContentPane(new ChartPanel(chart));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {

        String inputCsv  = "plot_data.csv";   // clean data from DataPlotter
        String outputCsv = "salted_data.csv";
        double range     = 2.0;               // noise amplitude ±range
        Long   seed      = null;              // RNG seed (null = random)

        /* CLI overrides */
        if (args.length >= 1) range = Double.parseDouble(args[0]);
        if (args.length >= 2) inputCsv = args[1];
        if (args.length >= 3) outputCsv = args[2];
        if (args.length >= 4) seed = Long.parseLong(args[3]);

        /* Load, salt, save */
        List<double[]> original = readCSV(inputCsv);
        if (original.isEmpty()) { System.err.println("No data in " + inputCsv); return; }

        List<double[]> salted = salt(original, range, seed);
        writeCSV(salted, outputCsv);

        /* Commons Math regression */
        SimpleRegression reg = new SimpleRegression();
        for (double[] p : salted) reg.addData(p[0], p[1]);
        double a = reg.getIntercept();
        double b = reg.getSlope();
        System.out.printf("Linear fit:  y = %.4f + %.4f·x (R² = %.4f)%n",
                          a, b, reg.getRSquare());

        /* Build a straight line series for plotting */
        double xMin = salted.get(0)[0];
        double xMax = salted.get(salted.size()-1)[0];
        XYSeries line = new XYSeries("regression line");
        line.add(xMin, a + b * xMin);
        line.add(xMax, a + b * xMax);

        showChart(original, salted, line, range);
    }
}
