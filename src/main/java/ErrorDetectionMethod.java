public interface ErrorDetectionMethod {
    public void configure();
    public String calculate(byte[] input);
    public boolean verify(byte[] input, String valueToCompare);
}
