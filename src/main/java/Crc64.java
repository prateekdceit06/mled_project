import static java.lang.System.exit;

public class Crc64 implements Crc {
    @Override
    public String getPolynomial(int choice) {
        String crcPolynomial = "";
        boolean errorFlag = false;
        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRed("Error: Invalid input. Please try again."));
            }
            try {
                if (!errorFlag) {
                    switch (choice) {
                        case 1:
                            crcPolynomial = Constants.CRC64Variant.CRC_64.getPolynomial();
                            break;
                        case 2:
                            crcPolynomial = Constants.CRC64Variant.CRC_64_ECMA_182.getPolynomial();
                            break;
                        case 3:
                            crcPolynomial = Constants.CRC64Variant.CRC_64_GO_ISO.getPolynomial();
                            break;
                        case 4:
                            crcPolynomial = Constants.CRC64Variant.CRC_64_WE.getPolynomial();
                            break;
                        case 5:
                            exit(1);
                        default:
                            errorFlag = true;
                            break;
                    }
                }
            } catch (Exception e) {
                errorFlag = true;
            }
        } while (errorFlag);
        return crcPolynomial;
    }
}

