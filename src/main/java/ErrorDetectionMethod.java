public interface ErrorDetectionMethod {
    void configure();
    String calculate(byte[] input);
    boolean verify(byte[] input, String valueToCompare);
}
