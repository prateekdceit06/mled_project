import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.System.exit;

public class ErrorDetectionMethodHash implements ErrorDetectionMethod {

    private String algorithm = "MD5";

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void configure() {
        Menu.hashAlgorithmMenu();
        boolean errorFlag = false;
        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
            }
            try {
                System.out.print(PrintColor.printInRed("Select the hash algorithm: "));
                int algorithmNum = MledSimulator.getInstance().getScanner().nextInt();
                MledSimulator.getInstance().getScanner().nextLine();
                errorFlag = !Validator.isIntValid(algorithmNum, 1, 4);
                if (errorFlag) {
                    continue;
                }
                switch (algorithmNum) {
                    case 1:
                        algorithm = "MD5";
                        break;
                    case 2:
                        algorithm = "SHA-1";
                        break;
                    case 3:
                        algorithm = "SHA-256";
                        break;
                    case 4:
                        exit(1);
                    default:
                        System.out.println(PrintColor.printInRedBack("Invalid input"));
                        errorFlag = true;
                }
            } catch (Exception e) {
                errorFlag = true;
            }
        } while (errorFlag);

    }

    @Override
    public String calculate(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(input);
            return DatatypeConverter.printHexBinary(digest).toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(byte[] input, String valueToCompare) {
        String calculatedHash = calculate(input);
        return calculatedHash.equalsIgnoreCase(valueToCompare);
    }

    //override toString method to print the hash algorithm
    @Override
    public String toString() {
        return "Hash algorithm: " + algorithm;
    }
}
