public class CrcFactory {
    public static Crc getCrcObject(String crcType) {
        if (crcType == null) {
            return null;
        }
        if (crcType.equalsIgnoreCase(Constants.CRC8Variant.CRC_8.getVariantName())) {
            return new Crc8();
        } else if (crcType.equalsIgnoreCase(Constants.CRC16Variant.CRC_16.getVariantName())) {
            return new Crc16();
        } else if (crcType.equalsIgnoreCase(Constants.CRC32Variant.CRC_32.getVariantName())) {
            return new Crc32();
        } else if (crcType.equalsIgnoreCase(Constants.CRC64Variant.CRC_64.getVariantName())) {
            return new Crc64();
        } else {
            return new CrcCustom();
        }
    }
}
