import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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

    public static void crcMenu() {
        System.out.println(PrintColor.printInCyan("CRC Menu"));
        System.out.println(PrintColor.printInCyan("1. CRC-8"));
        System.out.println(PrintColor.printInCyan("2. CRC-16"));
        System.out.println(PrintColor.printInCyan("3. CRC-32"));
        System.out.println(PrintColor.printInCyan("4. CRC-64"));
        System.out.println(PrintColor.printInCyan("5. CRC-Custom"));
        System.out.println(PrintColor.printInCyan("6. Exit"));
    }

    public static void crc8Menu() {
        System.out.println(PrintColor.printInCyan("CRC-8 Menu"));
        int index = 1;
        for (Constants.CRC8Variant variant : Constants.CRC8Variant.values()) {
            System.out.println(PrintColor.printInCyan(index + ". " + variant.getVariantName() + ", Polynomial used - " + variant.getPolynomial()));
            index++;
        }
        System.out.println(PrintColor.printInCyan(index + ". Exit"));
    }

    public static void crc16Menu() {
        System.out.println(PrintColor.printInCyan("CRC-16 Menu"));
        int index = 1;
        for (Constants.CRC16Variant variant : Constants.CRC16Variant.values()) {
            System.out.println(PrintColor.printInCyan(index + ". " + variant.getVariantName() + ", Polynomial used - " + variant.getPolynomial()));
            index++;
        }
        System.out.println(PrintColor.printInCyan(index + ". Exit"));
    }

    public static void crc32Menu() {
        System.out.println(PrintColor.printInCyan("CRC-32 Menu"));
        int index = 1;
        for (Constants.CRC32Variant variant : Constants.CRC32Variant.values()) {
            System.out.println(PrintColor.printInCyan(index + ". " + variant.getVariantName() + ", Polynomial used - " + variant.getPolynomial()));
            index++;
        }
        System.out.println(PrintColor.printInCyan(index + ". Exit"));
    }

    public static void crc64Menu() {
        System.out.println(PrintColor.printInCyan("CRC-64 Menu"));
        int index = 1;
        for (Constants.CRC64Variant variant : Constants.CRC64Variant.values()) {
            System.out.println(PrintColor.printInCyan(index + ". " + variant.getVariantName() + ", Polynomial used - " + variant.getPolynomial()));
            index++;
        }
        System.out.println(PrintColor.printInCyan(index + ". Exit"));
    }

    public static void errorModelMenu() {
        System.out.println(PrintColor.printInPurple("Error Model Menu"));
        int index = 1;
        DecimalFormat df = new DecimalFormat("#.#####");  // Adjust number of decimal places as needed
        for (Constants.ErrorType errorType : Constants.ErrorType.values()) {

            double[] errorValues = errorType.getErrorValues();

            String output = String.format("%d. %-30s ---> GOOD_TO_BAD: %-10s BAD_TO_GOOD: %-10s errorProbabilityGood: %-10s errorProbabilityBad: %-10s",
                    index,
                    errorType.getErrorName(),
                    df.format(errorValues[0]),
                    df.format(errorValues[1]),
                    df.format(errorValues[2]),
                    df.format(errorValues[3]));

            System.out.println(PrintColor.printInPurple(output));
            index++;
        }
        System.out.println(PrintColor.printInPurple(index++ + ". Custom Error Model"));

        System.out.println(PrintColor.printInPurple(index + ". Exit"));

    }

    public static void initialiseSimulatorMenu() {
        System.out.println(PrintColor.printInYellow("Initialise Simulator Menu"));
        System.out.println(PrintColor.printInYellow("1. Initialise Simulator from configuration file"));
        System.out.println(PrintColor.printInYellow("2. Initialise Simulator with custom configuration."));
        System.out.println(PrintColor.printInYellow("3. Exit"));
    }

    public static ArrayList<File> fileMenu(File[] files) {
        Arrays.sort(files, Comparator.comparing(File::getName)); // This will sort the files in alphabetical order.

        ArrayList<File> validFiles = new ArrayList<>();
        System.out.println(PrintColor.printInYellow("Please select a file from the list below: "));
        int count = 1;

        for (File file : files) {
            String fileName = file.getName();

            if (!fileName.startsWith(".") && !fileName.startsWith("_")) { // Ignore files starting with a dot (.)
                System.out.println(PrintColor.printInYellow(count + ". " + fileName));
                validFiles.add(file);
                count++;
            }
        }

        System.out.println(PrintColor.printInYellow(count + ". Exit"));
        return validFiles;
    }


}
