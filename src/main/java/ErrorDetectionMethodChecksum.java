import static java.lang.System.exit;

public class ErrorDetectionMethodChecksum implements ErrorDetectionMethod {

    private int checksumLength;

    public int getChecksumLength() {
        return checksumLength;
    }
    @Override

    public void configure() {

        boolean errorFlag = false;
        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
            }
            try {
                System.out.print(PrintColor.printInWhite("Enter the checksum length (in bytes) [1-10]: "));
                checksumLength = MledSimulator.getInstance().getScanner().nextInt();
                MledSimulator.getInstance().getScanner().nextLine();
                errorFlag = !Validator.isIntValid(checksumLength, 1, 10);
            } catch (Exception e) {
                errorFlag = true;
            }
        } while (errorFlag);
    }
    public String calculate(byte[] input) {
        int sum = 0;
        for (int i = 0; i < input.length; i += checksumLength) {
            int segmentEnd = Math.min(i + checksumLength, input.length);
            sum += byteArrayToInt(input, i, segmentEnd, checksumLength);
        }
        int checksum = ~sum;
        return String.valueOf(checksum);
    }
    public boolean verify(byte[] input, String valueToCompare) {
        String calculatedChecksum = calculate(input);
        return calculatedChecksum.equals(valueToCompare);
    }

    private static int byteArrayToInt(byte[] bytes, int start, int end, int length) {
        int value = 0;
        for (int i = start; i < start + length; i++) {
            value = (value << 8) + ((i < end) ? (bytes[i] & 0xff) : 0);
        }
        return value;
    }

    //override toString method to print the checksum length
    @Override
    public String toString() {
        return "Checksum length: " + checksumLength;
    }
}
