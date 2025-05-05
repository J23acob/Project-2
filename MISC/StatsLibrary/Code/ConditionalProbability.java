public class ConditionalProbability {
    /**
     * Computes P(A|B) = P(A ∩ B) / P(B)
     * @param pAB - probability of both A and B
     * @param pB  - probability of B
     * @return conditional probability P(A|B)
     */
    public static double conditional(double pAB, double pB) {
        if (pB == 0) {
            throw new IllegalArgumentException("P(B) must be non-zero");
        }
        return pAB / pB;
    }

    public static void main(String[] args) {
        double pAB = 0.249; // P(A and B)
        double pB  = 0.453; // P(B)
        double result = conditional(pAB, pB);
        System.out.printf("P(A|B) = %.3f%n", result);
    }
}
