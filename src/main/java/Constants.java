
public class Constants {
    enum errorDetectionMethodEnum {CRC, CHECKSUM, HASH}

    enum hashAlgorithmsEnum {MD5, SHA1, SHA256}

    public enum CRC8Variant {
        CRC_8("CRC-8", "0x07"),
        CRC_8_CDMA2000("CRC-8/CDMA2000", "0x9B"),
        CRC_8_DARC("CRC-8/DARC", "0x39"),
        CRC_8_DVB_S2("CRC-8/DVB-S2", "0xD5"),
        CRC_8_EBU("CRC-8/EBU", "0x1D"),
        CRC_8_I_CODE("CRC-8/I-CODE", "0x1D"),
        CRC_8_ITU("CRC-8/ITU", "0x07"),
        CRC_8_MAXIM("CRC-8/MAXIM", "0x31"),
        CRC_8_ROHC("CRC-8/ROHC", "0x07"),
        CRC_8_WCDMA("CRC-8/WCDMA", "0x9B");

        private final String variantName;
        private final String polynomial;

        CRC8Variant(String variantName, String polynomial) {
            this.variantName = variantName;
            this.polynomial = polynomial;
        }

        public String getVariantName() {
            return variantName;
        }

        public String getPolynomial() {
            return polynomial;
        }
    }

    public enum CRC16Variant {
        CRC_16("CRC-16", "0x8005"),
        CRC_16_ARC("CRC-16/ARC", "0x8005"),
        CRC_16_CDMA2000("CRC-16/CDMA2000", "0xC867"),
        CRC_16_CMS("CRC-16/CMS", "0x8005"),
        CRC_16_DDS_110("CRC-16/DDS-110", "0x8005"),
        CRC_16_DECT_R("CRC-16/DECT-R", "0x0589"),
        CRC_16_DECT_X("CRC-16/DECT-X", "0x0589"),
        CRC_16_DNP("CRC-16/DNP", "0x3D65"),
        CRC_16_EN_13757("CRC-16/EN-13757", "0x3D65"),
        CRC_16_GENIBUS("CRC-16/GENIBUS", "0x1021"),
        CRC_16_MAXIM("CRC-16/MAXIM", "0x8005"),
        CRC_16_MCRF4XX("CRC-16/MCRF4XX", "0x1021"),
        CRC_16_RIELLO("CRC-16/RIELLO", "0x1021"),
        CRC_16_T10_DIF("CRC-16/T10-DIF", "0x8BB7"),
        CRC_16_TELEDISK("CRC-16/TELEDISK", "0xA097"),
        CRC_16_TMS37157("CRC-16/TMS37157", "0x1021"),
        CRC_16_USB("CRC-16/USB", "0x8005");

        private final String variantName;
        private final String polynomial;

        CRC16Variant(String variantName, String polynomial) {
            this.variantName = variantName;
            this.polynomial = polynomial;
        }

        public String getVariantName() {
            return variantName;
        }

        public String getPolynomial() {
            return polynomial;
        }
    }

    public enum CRC32Variant {
        CRC_32("CRC-32", "0x04C11DB7"),
        CRC_32_BZIP2("CRC-32/BZIP2", "0x04C11DB7"),
        CRC_32C("CRC-32C", "0x1EDC6F41"),
        CRC_32D("CRC-32D", "0xA833982B"),
        CRC_32_MPEG_2("CRC-32/MPEG-2", "0x04C11DB7"),
        CRC_32_POSIX("CRC-32/POSIX", "0x04C11DB7"),
        CRC_32Q("CRC-32Q", "0x814141AB"),
        CRC_32_JAMCRC("CRC-32/JAMCRC", "0x04C11DB7");

        private final String variantName;
        private final String polynomial;

        CRC32Variant(String variantName, String polynomial) {
            this.variantName = variantName;
            this.polynomial = polynomial;
        }

        public String getVariantName() {
            return variantName;
        }

        public String getPolynomial() {
            return polynomial;
        }
    }

    public enum CRC64Variant {
        CRC_64("CRC-64", "0x42F0E1EBA9EA3693"),
        CRC_64_ECMA_182("CRC-64/ECMA-182", "0x42F0E1EBA9EA3693"),
        CRC_64_GO_ISO("CRC-64/GO-ISO", "0x000000000000001B"),
        CRC_64_WE("CRC-64/WE", "0x42F0E1EBA9EA3693");

        private final String variantName;
        private final String polynomial;

        CRC64Variant(String variantName, String polynomial) {
            this.variantName = variantName;
            this.polynomial = polynomial;
        }

        public String getVariantName() {
            return variantName;
        }

        public String getPolynomial() {
            return polynomial;
        }
    }

}
