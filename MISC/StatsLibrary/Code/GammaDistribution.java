public class GammaDistribution {
    /** Computes factorial of n */
    private static long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    /**
     * Gamma PDF: f(x) = x^(k-1) * exp(-x/theta) / (theta^k * (k-1)!)
     * for x >= 0, integer shape k > 0
     */
    public static double pdf(int k, double theta, double x) {
        if (x < 0 || k <= 0 || theta <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        double numerator = Math.pow(x, k - 1) * Math.exp(-x / theta);
        double denominator = Math.pow(theta, k) * factorial(k - 1);
        return numerator / denominator;
    }

    /**
     * Gamma CDF for integer shape k:
     * P(X <= x) = 1 - exp(-x/theta) * sum_{j=0 to k-1} (x/theta)^j / j!
     */
    public static double cdf(int k, double theta, double x) {
        if (x < 0 || k <= 0 || theta <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        double sum = 0;
        double z = x / theta;
        for (int j = 0; j < k; j++) {
            sum += Math.pow(z, j) / factorial(j);
        }
        return 1 - Math.exp(-z) * sum;
    }

    public static void main(String[] args) {
        int k = 3;                // shape
        double theta = 15.9;      // scale
        double x = 30.0;          // evaluation point
        double ppdf = pdf(k, theta, x);
        double pcdf = cdf(k, theta, x);
        System.out.printf("Gamma PDF at x=%.1f: %.5f%n", x, ppdf);
        System.out.printf("Gamma CDF at x=%.1f: %.3f%n", x, pcdf);
    }
}
