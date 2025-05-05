public class Chebyshev {
    // Chebyshev bound: P(|X - μ| < kσ) ≥ 1 - 1/k^2
    public static double bound(double k) {
        return 1 - 1.0/(k*k);
    }
    public static void main(String[] args) {
        for (double k : new double[]{1.5, 2, 3}) {
            System.out.printf("k=%.1f: P≥%.3f%n", k, bound(k));
        }
    }
}
