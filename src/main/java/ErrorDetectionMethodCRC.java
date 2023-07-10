import static java.lang.System.exit;

public class ErrorDetectionMethodCRC implements ErrorDetectionMethod {

    String crcPolynomialHex;
    String getCrcPolynomialBinary;
    String type = "";

    public void setGetCrcPolynomialBinary(String getCrcPolynomialBinary) {
        this.getCrcPolynomialBinary = getCrcPolynomialBinary;
    }

    public void configure() {
        Menu.crcMenu();
        boolean errorFlag = false;
        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
            }
            try {
                System.out.print(PrintColor.printInRed("Enter the CRC you want to use: "));
                int choice = MledSimulator.getInstance().getScanner().nextInt();
                MledSimulator.getInstance().getScanner().nextLine();
                errorFlag = !Validator.isIntValid(choice, 1, 6);

                if (!errorFlag) {
                    int choice2 = 0;
                    switch (choice){
                        case 1:
                            Menu.crc8Menu();
                            System.out.print(PrintColor.printInRed("Enter the CRC you want to use: "));
                            choice2 = MledSimulator.getInstance().getScanner().nextInt();
                            MledSimulator.getInstance().getScanner().nextLine();
                            errorFlag = !Validator.isIntValid(choice2, 1, 11);
                            type = Constants.CRC8Variant.CRC_8.getVariantName();
                            break;
                        case 2:
                            Menu.crc16Menu();
                            System.out.print(PrintColor.printInRed("Enter the CRC you want to use: "));
                            choice2 = MledSimulator.getInstance().getScanner().nextInt();
                            MledSimulator.getInstance().getScanner().nextLine();
                            errorFlag = !Validator.isIntValid(choice2, 1, 18);
                            type = Constants.CRC16Variant.CRC_16.getVariantName();
                            break;
                        case 3:
                            Menu.crc32Menu();
                            System.out.print(PrintColor.printInRed("Enter the CRC you want to use: "));
                            choice2 = MledSimulator.getInstance().getScanner().nextInt();
                            MledSimulator.getInstance().getScanner().nextLine();
                            errorFlag = !Validator.isIntValid(choice2, 1, 9);
                            type = Constants.CRC32Variant.CRC_32.getVariantName();
                            break;
                        case 4:
                            Menu.crc64Menu();
                            System.out.print(PrintColor.printInRed("Enter the CRC you want to use: "));
                            choice2 = MledSimulator.getInstance().getScanner().nextInt();
                            MledSimulator.getInstance().getScanner().nextLine();
                            errorFlag = !Validator.isIntValid(choice2, 1, 5);
                            type = Constants.CRC64Variant.CRC_64.getVariantName();
                            break;
                        case 5:
                            break;
                        case 6:
                            exit(1);
                    }
                    if (!errorFlag){
                        Crc crc = new CrcFactory().getCrcObject(type);
                        crcPolynomialHex = crc.getPolynomial(choice2);
                        getCrcPolynomialBinary = hexToBin(crcPolynomialHex);
                        if (getCrcPolynomialBinary.charAt(0) != '1' || getCrcPolynomialBinary.charAt(getCrcPolynomialBinary.length() - 1) != '1') {
                            System.out.println(PrintColor.printInRedBack("Invalid generator polynomial. The first and last bits must be 1."));
                            errorFlag = true;
                        }
                    }
                }
            } catch (Exception e) {
                errorFlag = true;
            }
        } while (errorFlag);
    }
    public String calculate(byte[] input) {
        String binaryString = byteArrayToString(input) + getZeros(getCrcPolynomialBinary.length() - 1);
        String remainder = divide(binaryString, getCrcPolynomialBinary);
        return remainder;
    }

    public boolean verify(byte[] input, String valueToCompare) {
        String binaryString = byteArrayToString(input) + valueToCompare;
        String remainder = divide(binaryString, getCrcPolynomialBinary);
        String zeros = getZeros(getCrcPolynomialBinary.length() - 1);
        if (remainder.equals(zeros)) {
            return true;
        }
        return false;
    }

    public String hexToBin(String hex){
        String bin = "";
        String binFragment = "";
        int iHex;
        hex = hex.trim();
        hex = hex.replaceFirst("0x", "");

        for(int i = 0; i < hex.length(); i++){
            iHex = Integer.parseInt(""+hex.charAt(i),16);
            binFragment = Integer.toBinaryString(iHex);

            while(binFragment.length() < 4){
                binFragment = "0" + binFragment;
            }
            bin += binFragment;
        }
        return bin.replaceFirst("^0+", "");
    }




    private String byteArrayToString(byte[] byteArray) {
        StringBuilder binaryString = new StringBuilder();
        for (byte b : byteArray) {
            String binaryByte = Integer.toBinaryString(b & 0xFF);
            while (binaryByte.length() < 8) {
                binaryByte = "0" + binaryByte;
            }
            binaryString.append(binaryByte);
        }
        return binaryString.toString();
    }

    private String getZeros(int n) {
        StringBuilder result = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            result.append('0');
        }
        return result.toString();
    }

    private String divide(String dividend, String divisor) {
        int pointer = divisor.length();
        String remainder = dividend.substring(0, pointer);
        StringBuilder result = new StringBuilder(remainder);

        while (pointer < dividend.length()) {
            if (remainder.charAt(0) == '1') {
                remainder = xor(remainder, divisor) + dividend.charAt(pointer);
            } else {
                remainder = xor(remainder, getZeros(divisor.length())) + dividend.charAt(pointer);
            }

            remainder = remainder.substring(1);
            pointer++;
        }

        if (remainder.charAt(0) == '1') {
            remainder = xor(remainder, divisor);
        } else {
            remainder = xor(remainder, getZeros(divisor.length()));
        }

        return remainder.substring(1);
    }

    private String xor(String a, String b) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i)) {
                result.append('0');
            } else {
                result.append('1');
            }
        }

        return result.toString();
    }

    //Override toString() method to print the name of the CRC
    @Override
    public String toString() {
        return "CRC Polynominal: " + crcPolynomialHex +
                ", CRC Polynominal in binary: " + getCrcPolynomialBinary +
                ", CRC Variant: " + type;
    }

}
