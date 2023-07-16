import java.util.List;

//Class that defines various validators in the simulator.
public class Validator {
    public static boolean isIntValid(int option, int min, int max) {
        if (option >= min && option <= max) {
            return true;
        }
        return false;
    }

    public static boolean isCharValid(char option, String values) {
        String[] splitValues = values.split(" ");
        for (String value : splitValues) {
            if (value.charAt(0) == option) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStringValid(String option, List<String> values) {
        if (values.contains(option)) {
            return true;
        }
        return false;
    }
}
