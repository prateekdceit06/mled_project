public class CrcCustom implements Crc{
    @Override
    public String getPolynomial(int choice) {
        String crcPolynomial = "";
        boolean errorFlag = false;
        do{
            try{
                if (errorFlag) {
                    System.out.println(PrintColor.printInRed("Error: Invalid input. Please try again."));
                }
                System.out.println("Enter the generator polynomial as a hexadecimal string starting with 0x:");
                String generatorHex = MledSimulator.getInstance().getScanner().nextLine();

                if (!generatorHex.startsWith("0x")) {
                    System.out.println("Invalid generator polynomial. Please try again.");
                    errorFlag = true;
                } else {
                    crcPolynomial = generatorHex;
                    errorFlag = false;
                }

            } catch (Exception e) {
                System.out.println("Invalid generator polynomial. Please try again.");
                errorFlag = true;
            }
        } while (errorFlag);
        return crcPolynomial;
    }


}
