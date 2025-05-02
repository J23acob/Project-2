public class DataHandler {

    public static void main(String[] args) {
        // 1) Generate the original data
        //    Calls the PlotFunction class, which should produce "plot_data.csv"
        System.out.println("===== Generating original data (PlotFunction) =====");
        PlotFunction.main(new String[0]);

        // 2) Salt the data
        //    Calls the DataSalter class, which should read "plot_data.csv"
        //    and produce "salted_data.csv"
        System.out.println("===== Salting data (DataSalter) =====");
        DataSalter.main(new String[0]);   // or pass args if needed

        // 3) Smooth the salted data
        //    Calls the Smoother class, which should read "salted_data.csv"
        //    and produce "smoothed_data.csv"
        System.out.println("===== Smoothing data (Smoother) =====");
        Smoother.main(new String[0]);

        System.out.println("\nAll steps complete!");
        System.out.println("You should now have plot_data.csv, salted_data.csv, and smoothed_data.csv.");
    }
}
