import static java.lang.System.exit;

public class Crc32 implements Crc{
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
                                crcPolynomial = Constants.CRC32Variant.CRC_32.getPolynomial();
                                break;
                            case 2:
                                crcPolynomial = Constants.CRC32Variant.CRC_32_BZIP2.getPolynomial();
                                break;
                            case 3:
                                crcPolynomial = Constants.CRC32Variant.CRC_32C.getPolynomial();
                                break;
                            case 4:
                                crcPolynomial = Constants.CRC32Variant.CRC_32D.getPolynomial();
                                break;
                            case 5:
                                crcPolynomial = Constants.CRC32Variant.CRC_32_MPEG_2.getPolynomial();
                                break;
                            case 6:
                                crcPolynomial = Constants.CRC32Variant.CRC_32_POSIX.getPolynomial();
                                break;
                            case 7:
                                crcPolynomial = Constants.CRC32Variant.CRC_32Q.getPolynomial();
                                break;
                            case 8:
                                crcPolynomial = Constants.CRC32Variant.CRC_32_JAMCRC.getPolynomial();
                                break;
                            case 9:
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
