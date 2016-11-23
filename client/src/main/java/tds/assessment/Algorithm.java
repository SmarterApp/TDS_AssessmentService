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

    /**
     * @return the name of the algorithm 
     */
    public String getAlgorithmName() {
        return algorithmName;
    }

    @Override
    public String toString() {
        return this.getAlgorithmName();
    }

    /**
     * Returns a {@link tds.assessment.Algorithm} enum for the algorithm name
     *
     * @param algorithmName
     * @return
     */
    public static Algorithm fromAlgorithm(String algorithmName) {
        if (algorithmName != null) {
            for (Algorithm algorithm : values()) {
                if (algorithmName.equalsIgnoreCase(algorithm.getAlgorithmName())) {
                    return algorithm;
                }
            }
        }
        // No value found for string
        throw new IllegalArgumentException();
    }
}
