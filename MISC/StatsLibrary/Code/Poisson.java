public class Poisson {
    /** Poisson PMF: P(X=k) = e^{-lam} * lam^k / k! **/
    public static double pmf(int k, double lam) {
        return Math.exp(-lam) * Math.pow(lam, k) / factorial(k);
    }

    /** Poisson CDF: P(X<=k) = sum_{i=0..k} PMF(i) **/
    public static double cdf(int k, double lam) {
        double sum = 0;
        for (int i = 0; i <= k; i++) {
            sum += pmf(i, lam);
        }
        return sum;
    }

    /** Simple integer factorial **/
    private static long factorial(int n) {
        long f = 1;
        for (int i = 2; i <= n; i++) f *= i;
        return f;
    }

    public static void main(String[] args) {
        double lam = 1.9;
        int k = 3;
        double pmf = pmf(k, lam);
        double probGe3 = 1 - cdf(k - 1, lam);
        System.out.printf("P(X=%d) = %.4f%n", k, pmf);
        System.out.printf("P(X>=%d) = %.4f%n", k, probGe3);
    }
}
