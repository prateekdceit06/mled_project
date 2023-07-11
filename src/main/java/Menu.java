public class Menu {

    public static void errorCheckMethodMenu() {
        System.out.println(PrintColor.printInCyan("Error Check Method Menu"));
        System.out.println(PrintColor.printInCyan("1. Checksum"));
        System.out.println(PrintColor.printInCyan("2. CRC"));
        System.out.println(PrintColor.printInCyan("3. Hash"));
        System.out.println(PrintColor.printInCyan("4. Exit"));
    }

    public static void hashAlgorithmMenu() {
        System.out.println(PrintColor.printInCyan("Hash Algorithm Menu"));
        System.out.println(PrintColor.printInCyan("1. " + Constants.hashAlgorithmsEnum.MD5));
        System.out.println(PrintColor.printInCyan("2. " + Constants.hashAlgorithmsEnum.SHA1));
        System.out.println(PrintColor.printInCyan("3. " + Constants.hashAlgorithmsEnum.SHA256));
        System.out.println(PrintColor.printInCyan("4. Exit"));
    }

    public static void crcMenu(){
        System.out.println(PrintColor.printInCyan("CRC Menu"));
        System.out.println(PrintColor.printInCyan("1. CRC-8"));
        System.out.println(PrintColor.printInCyan("2. CRC-16"));
        System.out.println(PrintColor.printInCyan("3. CRC-32"));
        System.out.println(PrintColor.printInCyan("4. CRC-64"));
        System.out.println(PrintColor.printInCyan("5. CRC-Custom"));
        System.out.println(PrintColor.printInCyan("6. Exit"));
    }

    public static void crc8Menu(){
        System.out.println(PrintColor.printInCyan("CRC-8 Menu"));
        int index = 1;
        for (Constants.CRC8Variant variant : Constants.CRC8Variant.values()) {
            System.out.println(PrintColor.printInCyan(index + ". " + variant.getVariantName() + ", Polynomial used - " + variant.getPolynomial()));
            index++;
        }
        System.out.println(PrintColor.printInCyan(index + ". Exit"));
    }

    public static void crc16Menu(){
        System.out.println(PrintColor.printInCyan("CRC-16 Menu"));
        int index = 1;
        for (Constants.CRC16Variant variant : Constants.CRC16Variant.values()) {
            System.out.println(PrintColor.printInCyan(index + ". " + variant.getVariantName() + ", Polynomial used - " + variant.getPolynomial()));
            index++;
        }
        System.out.println(PrintColor.printInCyan(index + ". Exit"));
    }

    public static void crc32Menu(){
        System.out.println(PrintColor.printInCyan("CRC-32 Menu"));
        int index = 1;
        for (Constants.CRC32Variant variant : Constants.CRC32Variant.values()) {
            System.out.println(PrintColor.printInCyan(index + ". " + variant.getVariantName() + ", Polynomial used - " + variant.getPolynomial()));
            index++;
        }
        System.out.println(PrintColor.printInCyan(index + ". Exit"));
    }

    public static void crc64Menu(){
        System.out.println(PrintColor.printInCyan("CRC-64 Menu"));
        int index = 1;
        for (Constants.CRC64Variant variant : Constants.CRC64Variant.values()) {
            System.out.println(PrintColor.printInCyan(index + ". " + variant.getVariantName() + ", Polynomial used - " + variant.getPolynomial()));
            index++;
        }
        System.out.println(PrintColor.printInCyan(index + ". Exit"));
    }

    public static void errorModelMenu(){
        System.out.println(PrintColor.printInPurple("Error Model Menu"));
        System.out.println(PrintColor.printInPurple("1. Default " +
                "[ Good to Bad-"+ Constants.GOOD_TO_BAD +
                ", Bad to Good-" + Constants.BAD_TO_GOOD +
                ", Error Probability Good-" + Constants.errorProbabilityGood +
                ", Error Probability Bad-" + Constants.errorProbabilityBad +
                "]: "));
        System.out.println(PrintColor.printInPurple("2. Custom"));
        System.out.println(PrintColor.printInPurple("3. Exit"));

    }




}
