package tds.assessment;

/**
 * Enumeration representing assessment algorithm types
 */
public enum Algorithm {
    ADAPTIVE_2("adaptive2"),
    VIRTUAL("virtual"),
    FIXED_FORM("fixedform");

    private String type;

    Algorithm(String type) {
        this.type = type;
    }

    /**
     * @return the name of the algorithm
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.getType();
    }

    /**
     * Returns a {@link tds.assessment.Algorithm} enum for the algorithm name
     *
     * @param algorithmType Th
     * @return
     */
    public static Algorithm fromType(String algorithmType) {
        if (algorithmType == null) throw new IllegalArgumentException("The algorithm type cannot be null");

        for (Algorithm algorithm : values()) {
            if (algorithmType.equalsIgnoreCase(algorithm.getType())) {
                return algorithm;
            }
        }
        // No Algorithm found for algorithm type
        throw new IllegalArgumentException(String.format("No Algorithm found with the name {}", algorithmType));
    }
}
