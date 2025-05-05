import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class HashMapExperiment {

    private static String randomString(Random r, int min, int max) {
        int len = r.nextInt(max - min + 1) + min;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append((char) ('a' + r.nextInt(26)));
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        int[] sizes = {10_000, 20_000, 40_000, 80_000, 160_000};
        Random rnd = new Random(42);

        try (FileWriter w = new FileWriter("results.csv")) {
            w.write("size,nanoTime,capacity\n");

            for (int n : sizes) {
                JacobHashMap<String> map = new JacobHashMap<>();
                long t0 = System.nanoTime();
                for (int i = 0; i < n; i++) map.add(randomString(rnd, 5, 15));
                long elapsed = System.nanoTime() - t0;

                w.write(n + "," + elapsed + "," + map.capacity() + "\n");
                System.out.printf("Inserted %,d strings (%.2f ms)%n",
                                   n, elapsed / 1_000_000.0);
            }
        }
        System.out.println("Wrote results.csv");
    }
}
