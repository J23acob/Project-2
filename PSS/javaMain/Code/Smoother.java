import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Smoother {

    // Reads X,Y CSV data into a List<double[]>.
    // Adjust or remove the "header check" if your CSV has no header.
    public static List<double[]> readXYFromCSV(String inputCsv) {
        List<double[]> xyData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputCsv))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // Check for a header row and skip if it looks like one.
                if (isHeader) {
                    if (line.toLowerCase().contains("x") && line.toLowerCase().contains("y")) {
                        // Skip this line if it's the header
                        isHeader = false;
                        continue;
                    }
                    // If it’s numeric data, we still parse it below but only skip once
                    isHeader = false;
                }

                // Split on comma
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue; // skip malformed lines
                }

                try {
                    double x = Double.parseDouble(parts[0].trim());
                    double y = Double.parseDouble(parts[1].trim());
                    xyData.add(new double[] { x, y });
                } catch (NumberFormatException e) {
                    // Skip invalid lines
                    System.err.println("Skipping invalid numeric line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xyData;
    }

    // Writes X,Y data to a CSV file
    public static void writeXYToCSV(List<double[]> xyData, String outputCsv) {
        try (FileWriter fw = new FileWriter(outputCsv);
             PrintWriter pw = new PrintWriter(fw)) {

            // Optional: print header
            pw.println("X,Y");

            for (double[] pair : xyData) {
                pw.println(pair[0] + "," + pair[1]);
            }

            System.out.println("Smoothed data written to: " + outputCsv);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Smooths the Y-values using a "moving average" window approach.
    // For each index i, we look up to 'window' points on both sides
    // and average their Y-values. This helps reduce noise.
    public static void smoothData(List<double[]> xyData, int window) {

        double[] smoothedY = new double[xyData.size()];

        // Loop through each point
        for (int i = 0; i < xyData.size(); i++) {
            // Determine the bounds for our window
            // Example, i - window through i + window
            int start = Math.max(0, i - window);
            int end = Math.min(xyData.size() - 1, i + window);

            double sum = 0.0;
            int count = 0;

            // Sum up all Y-values in [start, end]
            for (int j = start; j <= end; j++) {
                sum += xyData.get(j)[1]; // only the y-value (index 1)
                count++;
            }

            // Compute the average
            double avg = sum / count;
            smoothedY[i] = avg;
        }

        // Now copy the smoothedY back into xyData
        for (int i = 0; i < xyData.size(); i++) {
            double x = xyData.get(i)[0];
            // Replace Y with the smoothed version
            xyData.set(i, new double[] { x, smoothedY[i] });
        }
    }

    // Main entry point
    public static void main(String[] args) {
        // 1) Read "salted_data.csv"
        // 2) Smooth using a window of +/- 5
        // 3) Write out "smoothed_data.csv"

        String inputCsv = "plot_data.csv";      // or "original_data.csv" or any file
        String outputCsv = "smoothed_data.csv";
        int windowValue = 5;  // number of points to left/right for averaging

        // 1. Read data
        List<double[]> xyData = readXYFromCSV(inputCsv);
        System.out.println("Loaded " + xyData.size() + " data points.");

        // 2. Smooth data
        System.out.println("Smoothing data with window +/- " + windowValue + " ...");
        smoothData(xyData, windowValue);

        // 3. Write smoothed data
        writeXYToCSV(xyData, outputCsv);

        System.out.println("Done.");
    }
}
