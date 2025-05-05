public class UniformProbability {
    // P(x1<=X<=x2) = (x2 - x1) / (b - a)
    public static double probability(double a, double b, double x1, double x2) {
        return (x2 - x1) / (b - a);
    }
    public static void main(String[] args) {
        System.out.printf("P(1≤X≤5) = %.4f%n", probability(-15, 16, 1, 5));
    }
}
