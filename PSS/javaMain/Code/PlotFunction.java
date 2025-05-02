import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlotFunction {

    // Define any function, y = x^2
    private static double myFunction(double x) {
        return x * x;  // Parabola
    }
    
    public static void main(String[] args) {
        // We'll store (x, y) pairs in a List of String arrays or a List of a custom class.
        List<double[]> dataPoints = new ArrayList<>();

        // Generate data points for x from -10 to 10 in steps
        double start = -10.0;
        double end = 10.0;
        double step = 0.5; // step size

        for (double x = start; x <= end; x += step) {
            double y = myFunction(x);
            dataPoints.add(new double[] {x, y});
        }

        // Export to a CSV file
        String fileName = "plot_data.csv";
        try (FileWriter fileWriter = new FileWriter(fileName);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Header
            printWriter.println("X,Y");

            // data points
            for (double[] point : dataPoints) {
                printWriter.println(point[0] + "," + point[1]);
            }

            System.out.println("Data successfully exported to " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
