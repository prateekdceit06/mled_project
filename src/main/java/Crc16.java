import static java.lang.System.exit;

public class Crc16 implements Crc {
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
                            crcPolynomial = Constants.CRC16Variant.CRC_16.getPolynomial();
                            break;
                        case 2:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_ARC.getPolynomial();
                            break;
                        case 3:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_CDMA2000.getPolynomial();
                            break;
                        case 4:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_CMS.getPolynomial();
                            break;
                        case 5:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_DDS_110.getPolynomial();
                            break;
                        case 6:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_DECT_R.getPolynomial();
                            break;
                        case 7:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_DECT_X.getPolynomial();
                            break;
                        case 8:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_DNP.getPolynomial();
                            break;
                        case 9:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_EN_13757.getPolynomial();
                            break;
                        case 10:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_GENIBUS.getPolynomial();
                            break;
                        case 11:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_MAXIM.getPolynomial();
                            break;
                        case 12:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_MCRF4XX.getPolynomial();
                            break;
                        case 13:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_RIELLO.getPolynomial();
                            break;
                        case 14:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_T10_DIF.getPolynomial();
                            break;
                        case 15:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_TELEDISK.getPolynomial();
                            break;
                        case 16:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_TMS37157.getPolynomial();
                            break;
                        case 17:
                            crcPolynomial = Constants.CRC16Variant.CRC_16_USB.getPolynomial();
                            break;
                        case 18:
                            exit(1);
                        default:
                            errorFlag = true;
                    }


                }
            } catch (Exception e) {
                errorFlag = true;
            }
        } while (errorFlag);
        return crcPolynomial;
    }
}
