import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataSalter {

    // This method will read a CSV of "X,Y" data (with or without a header)
    // and store it as a List of double[].
    public static List<double[]> readXYFromCSV(String inputCsv) {
        List<double[]> xyData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputCsv))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // If the first line is a header, skip it and set flag to false.
                if (isHeader) {
                    // Try to detect if it's actually a header vs numeric data
                    if (line.toLowerCase().contains("x") && line.toLowerCase().contains("y")) {
                        // Skipping header
                        isHeader = false;
                        continue;
                    }
                    // If it's numeric data, we'll still process, but we only skip once
                    isHeader = false;
                }

                // Split on comma
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue; // skip any malformed line
                }

                try {
                    double x = Double.parseDouble(parts[0].trim());
                    double y = Double.parseDouble(parts[1].trim());
                    xyData.add(new double[]{x, y});
                } catch (NumberFormatException e) {
                    // If parsing fails, skip this line or handle error
                    System.err.println("Skipping invalid numeric line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xyData;
    }

    // This method salts the Y-values in-place.
    // range is the maximum absolute salt. For example, range = 2.0 => random is in [-2, 2].
    // randomSalt can be positive or negative each time.
    public static void saltData(List<double[]> xyData, double range) {
        Random random = new Random(); // new Random(seed) for reproducibility

        for (double[] pair : xyData) {
            double x = pair[0];
            double y = pair[1];

            // Generate random double in [0, range), then shift it to [-range, range].
            double salt = (random.nextDouble() * 2 * range) - range;

            // Add or subtract from Y only (X stays the same)
            double saltedY = y + salt;

            // Update the pair's Y value
            pair[1] = saltedY;
        }
    }

    // Write out the salted data to a new CSV file
    public static void writeXYToCSV(List<double[]> xyData, String outputCsv) {
        try (FileWriter fw = new FileWriter(outputCsv);
             PrintWriter pw = new PrintWriter(fw)) {

            // header
            pw.println("X,Y");

            for (double[] pair : xyData) {
                pw.println(pair[0] + "," + pair[1]);
            }

            System.out.println("Salted data has been written to " + outputCsv);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main method to tie it all together
    public static void main(String[] args) {

        // Example usage:
        // 1) Read "original_data.csv" from the project folder.
        // 2) Salt it with a random range of 2.0 (each Y changes by up to ±2).
        // 3) Write out "salted_data.csv"

        String inputCsv = "plot_data.csv";  // input
        String outputCsv = "salted_data.csv";   // output
        double range = 2.0;                     // Y will be salted by a random amount in [-2, 2]

        // Step 1: Read data
        List<double[]> xyData = readXYFromCSV(inputCsv);

        System.out.println("Original data size: " + xyData.size());
        System.out.println("Salting data...");

        // Step 2: Salt data
        saltData(xyData, range);

        // Step 3: Write salted data to a CSV
        writeXYToCSV(xyData, outputCsv);

        System.out.println("Done.");
    }
}
