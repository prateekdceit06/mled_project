import static java.lang.System.exit;

public class Crc8 implements Crc {
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
                            crcPolynomial = Constants.CRC8Variant.CRC_8.getPolynomial();
                            break;
                        case 2:
                            crcPolynomial = Constants.CRC8Variant.CRC_8_CDMA2000.getPolynomial();
                            break;
                        case 3:
                            crcPolynomial = Constants.CRC8Variant.CRC_8_DARC.getPolynomial();
                            break;
                        case 4:
                            crcPolynomial = Constants.CRC8Variant.CRC_8_DVB_S2.getPolynomial();
                            break;
                        case 5:
                            crcPolynomial = Constants.CRC8Variant.CRC_8_EBU.getPolynomial();
                            break;
                        case 6:
                            crcPolynomial = Constants.CRC8Variant.CRC_8_I_CODE.getPolynomial();
                            break;
                        case 7:
                            crcPolynomial = Constants.CRC8Variant.CRC_8_ITU.getPolynomial();
                            break;
                        case 8:
                            crcPolynomial = Constants.CRC8Variant.CRC_8_MAXIM.getPolynomial();
                            break;
                        case 9:
                            crcPolynomial = Constants.CRC8Variant.CRC_8_ROHC.getPolynomial();
                            break;
                        case 10:
                            crcPolynomial = Constants.CRC8Variant.CRC_8_WCDMA.getPolynomial();
                            break;
                        case 11:
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
