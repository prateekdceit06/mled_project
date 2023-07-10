
public class Test {
    public static void main(String[] args) {
        ErrorDetectionMethodCRC crc = new ErrorDetectionMethodCRC();
        String generatorPolynomialHex = "07"; // This should be set according to your specific CRC variant
        String generatorPolynomialBinary = crc.hexToBin(generatorPolynomialHex);
        crc.setGetCrcPolynomialBinary(generatorPolynomialBinary);

        // Test data
        byte[] testData = new byte[] {
                (byte) 0b00100011,
                (byte) 0b00100000,
                (byte) 0b01101101,
                (byte) 0b01101111,
                (byte) 0b01001001,
                (byte) 0b01000100,
                (byte) 0b00101100,
                (byte) 0b01110011,
                (byte) 0b01100100,
                (byte) 0b01110011,
                (byte) 0b01110011,
                (byte) 0b01011111,
                (byte) 0b01110010,
                (byte) 0b01110101,
                (byte) 0b01101110,
                (byte) 0b00101100,
                (byte) 0b01110011,
                (byte) 0b01100100,
                (byte) 0b01110011,
                (byte) 0b01110011,
                (byte) 0b01011111,
                (byte) 0b01100011,
                (byte) 0b01101111,
                (byte) 0b01101100,
                (byte) 0b00101100,
                (byte) 0b01110011,
                (byte) 0b01100100,
                (byte) 0b01110011,
                (byte) 0b01110011,
                (byte) 0b01011111,
                (byte) 0b01100110,
                (byte) 0b01101001,
                (byte) 0b01100101,
                (byte) 0b01101100,
                (byte) 0b01100100,
                (byte) 0b00101100,
                (byte) 0b01110011,
                (byte) 0b01100100,
                (byte) 0b01110011,
                (byte) 0b01110011,
                (byte) 0b01011111,
                (byte) 0b01101111
        };
        System.out.println("Test data: " + testData.length*8 + " bytes");

        // Calculate the CRC
        String crcValue = crc.calculate(testData);
        System.out.println("Calculated CRC: " + crcValue);

        // Verify the CRC
        boolean isValid = crc.verify(testData, crcValue);
        System.out.println("CRC is valid: " + isValid);
    }


}



