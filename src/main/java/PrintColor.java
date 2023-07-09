/*
 * A PrintColor class for printing in different colors or background colors
 * */
public class PrintColor {
    public final static String RESET = "\u001B[0m";
    public final static String BLACK = "\u001B[30m";
    public final static String BLACK_BACKGROUND = "\u001B[40m";
    //For Weapon, Monster
    public final static String RED = "\u001B[31m";
    public final static String RED_BACKGROUND = "\033[41m";
    //For Potion
    public final static String GREEN = "\u001B[32m";

    public final static String BRIGHT_GREEN = "\u001B[32;1m";
    public final static String GREEN_BACKGROUND = "\u001B[42m";
    public final static String YELLOW = "\u001B[33m";
    public final static String YELLOW_BACKGROUND = "\u001B[43m";
    //For Armor, Hero
    public final static String BLUE = "\u001B[34m";
    public final static String BLUE_BACKGROUND = "\u001B[44m";
    //For Spell
    public final static String PURPLE = "\u001B[35m";
    public final static String PURPLE_BACKGROUND = "\u001B[45m";
    public final static String CYAN = "\u001B[36m";
    public final static String CYAN_BACKGROUND = "\u001B[46m";
    public final static String WHITE = "\u001B[37m";
    public final static String WHITE_BACKGROUND = "\u001B[47m";
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";

    public static String printInBrightRedBack(String s) {return RED_BACKGROUND_BRIGHT + s + RESET;}

    /**
     * Return the given static String in black color
     * @param s the static String that is going to be changed
     * @return the given static String in black color
     */
    public static String printInBlack(String s) {
        return BLACK + s + RESET;
    }

    /**
     * Return the given static String in black background color
     * @param s the static String that is going to be changed
     * @return the given static String in black background color
     */
    public static String printInBlackBack(String s) {
        return BLACK_BACKGROUND + s + RESET;
    }

    /**
     * Return the given static String in red color
     * @param s the static String that is going to be changed
     * @return the given static String in red color
     */
    public static String printInRed(String s) {
        return RED + s + RESET;
    }

    /**
     * Return the given static String in red background color
     * @param s the static String that is going to be changed
     * @return the given static String in red background color
     */
    public static String printInRedBack(String s) {
        return RED_BACKGROUND + s + RESET;
    }

    /**
     * Return the given static String in green color
     * @param s the static String that is going to be changed
     * @return the given static String in green color
     */
    public static String printInGreen(String s) {
        return GREEN + s + RESET;
    }

    /**
     * Return the given static String in green background color
     * @param s the static String that is going to be changed
     * @return the given static String in green background color
     */
    public static String printInGreenBack(String s) {
        return GREEN_BACKGROUND + s + RESET;
    }

    /**
     * Return the given static String in yellow color
     * @param s the static String that is going to be changed
     * @return the given static String in yellow color
     */
    public static String printInYellow(String s) {
        return YELLOW + s + RESET;
    }

    /**
     * Return the given static String in yellow background color
     * @param s String the static String that is going to be changed
     * @return the given static String in yellow background color
     */
    public static String printInYellowBack(String s) {
        return YELLOW_BACKGROUND + s + RESET;
    }

    /**
     * Return the given static String in blue color
     * @param s String the static String that is going to be changed
     * @return the given static String in blue color
     */
    public static String printInBlue(String s) {
        return BLUE + s + RESET;
    }

    /**
     * Return the given static String in blue background color
     * @param s the static String that is going to be changed
     * @return the given static String in blue background color
     */
    public static String printInBlueBack(String s) {
        return BLUE_BACKGROUND + s + RESET;
    }

    /**
     * Return the given static String in purple color
     * @param s the static String that is going to be changed
     * @return the given static String in purple color
     */
    public static String printInPurple(String s) {
        return PURPLE + s + RESET;
    }

    /**
     * Return the given static String in purple background color
     * @param s the static String that is going to be changed
     * @return the given static String in purple background color
     */
    public static String printInPurpleBack(String s) {
        return PURPLE_BACKGROUND + s + RESET;
    }

    /**
     * Return the given static String in cyan color
     * @param s the static String that is going to be changed
     * @return the given static String in cyan color
     */
    public static String printInCyan(String s) {
        return CYAN + s + RESET;
    }

    /**
     * Return the given static String in cyan background color
     * @param s the static String that is going to be changed
     * @return the given static String in cyan background color
     */
    public static String printInCyanBack(String s){
        return CYAN_BACKGROUND + s + RESET;
    }

    /**
     * Return the given static String in white color
     * @param s the static String that is going to be changed
     * @return the given static String in white color
     */
    public static String printInWhite(String s) {
        return WHITE + s + RESET;
    }

    /**
     * Return the given static String in white background color
     * @param s the static String that is going to be changed
     * @return the given static String in white background color
     */
    public static String printInWhiteBack(String s) {
        return WHITE_BACKGROUND + s + RESET;
    }

    /**
     * Return a line to divide
     * @return a divider line
     */
    public static String divider() {
        return "==========================================================================================================================";
    }
}
