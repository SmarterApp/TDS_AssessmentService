package tds.assessment;

/**
 * Enumeration representing assessment algorithm types
 */
public enum Algorithm {
    ADAPTIVE_2("adaptive2"),
    VIRTUAL("virtual"),
    FIXED_FORM("fixedform");

    private String algorithmName;

    Algorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    @Override
    public String toString() {
        return this.getAlgorithmName();
    }

    public static Algorithm fromAlgorithm(String algorithmName) {
        for (Algorithm algorithm : values()) {
            if (algorithm.getAlgorithmName().equalsIgnoreCase(algorithmName)) {
                return algorithm;
            }
        }
        // No value found for string
        throw new IllegalArgumentException();
    }
}
