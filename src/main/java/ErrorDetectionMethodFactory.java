public class ErrorDetectionMethodFactory {
    public static ErrorDetectionMethod getErrorDetectionMethod(String method) {
        if (method.equals(Constants.errorDetectionMethodEnum.HASH.toString())) {
            return new ErrorDetectionMethodHash();
        } else if (method.equals(Constants.errorDetectionMethodEnum.CRC.toString())) {
            return new ErrorDetectionMethodCRC();
        } else if (method.equals(Constants.errorDetectionMethodEnum.CHECKSUM.toString())) {
            return new ErrorDetectionMethodChecksum();
        } else {
            return null;
        }
    }
}
