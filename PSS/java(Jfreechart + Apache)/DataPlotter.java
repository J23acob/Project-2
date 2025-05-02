/*
   DataPlotter.java
   • Generates (x, y) data for a nonlinear function (y = x² by default)
   • Exports the points to plot_data.csv
   • Displays the curve plus a mean‑line via JFreeChart
   • Uses Apache Commons Math to compute the mean and stdev
*/

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* JFreeChart & Swing */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;

/* Apache Commons Math */
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class DataPlotter {

    /* Changeable function  */
    private static double f(double x) {
        return x * x;                // parabola y = x²
    }

    /** Generates a List<double[]> where each element is {x, y}. */
    public static List<double[]> generateData(double start,
                                              double end,
                                              double step) {
        List<double[]> pts = new ArrayList<>();
        for (double x = start; x <= end; x += step) {
            pts.add(new double[] { x, f(x) });
        }
        return pts;
    }

    /** Writes the list to CSV with header X,Y. */
    public static void writeToCSV(List<double[]> pts, String file) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("X,Y");
            for (double[] p : pts) pw.println(p[0] + "," + p[1]);
            System.out.printf("CSV written: %s  (%d rows)%n",
                               file, pts.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Uses Commons Math to print mean and stdev, and returns the mean. */
    private static double printStats(List<double[]> pts) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double[] p : pts) stats.addValue(p[1]);
        double mean  = stats.getMean();
        double stdev = stats.getStandardDeviation();
        System.out.printf("y‑mean = %.4f,  y‑stdev = %.4f%n", mean, stdev);
        return mean;
    }

    /** Builds the dataset with both the curve and the mean line. */
    private static XYSeriesCollection buildDataset(List<double[]> pts,
                                                   double mean) {
        XYSeries curve = new XYSeries("y = x^2");
        for (double[] p : pts) curve.add(p[0], p[1]);

        XYSeries meanLine = new XYSeries("mean(y)");
        // horizontal line across the same x‑range
        double xStart = pts.get(0)[0];
        double xEnd   = pts.get(pts.size() - 1)[0];
        meanLine.add(xStart, mean);
        meanLine.add(xEnd,   mean);

        XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(curve);
        data.addSeries(meanLine);
        return data;
    }

    /** Displays the chart. */
    public static void showChart(XYSeriesCollection dataset, String title) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "x",
                "y",
                dataset);

        JFrame frame = new JFrame("JFreeChart – " + title);
        frame.setContentPane(new ChartPanel(chart));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        double start = -10, end = 10, step = 0.5;

        List<double[]> points = generateData(start, end, step);
        double mean = printStats(points);                 // Commons Math work
        writeToCSV(points, "plot_data.csv");

        XYSeriesCollection dataset = buildDataset(points, mean);
        showChart(dataset, "y = x^2 with mean line");
    }
}
