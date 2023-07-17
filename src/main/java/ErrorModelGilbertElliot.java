import java.util.Random;
import java.util.Scanner;

public class ErrorModelGilbertElliot implements ErrorModel {

    private double goodToBad = 0.0;
    private double badToGood = 0.0;
    private double errorProbabilityGood = 0.0;
    private double errorProbabilityBad = 0.0;
    private Random random;
    private int seed;
    private boolean state;  // true is bad, false is good

    public void setSeed(int seed) {
        this.seed = seed;
    }

    //empty constructor
    public ErrorModelGilbertElliot() {
        this.seed = Constants.SEED;
        this.random = new Random(this.seed);
        this.state = false; // start with good state
    }


    public ErrorModelGilbertElliot(double goodToBad, double badToGood, double errorProbabilityGood, double errorProbabilityBad) {
        this.goodToBad = goodToBad;
        this.badToGood = badToGood;
        this.errorProbabilityGood = errorProbabilityGood;
        this.errorProbabilityBad = errorProbabilityBad;
        this.seed = Constants.SEED;
        this.random = new Random(this.seed);
        this.state = false; // start with good state
    }

    public boolean isError() {
        // determine if state changes
        if (state) {
            if (random.nextDouble() < badToGood) {
                state = false;
            }
        } else {
            if (random.nextDouble() < goodToBad) {
                state = true;
            }
        }

        // determine if error occurs
        return (state) ? random.nextDouble() < errorProbabilityBad
                : random.nextDouble() < errorProbabilityGood;
    }


    public void configure() {

        Scanner scanner = MledSimulator.getInstance().getScanner();
        boolean errorFlag = false;
        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRed("Error: Invalid input. Please try again."));
            }
            try {
                System.out.print(PrintColor.printInPurple("Please enter GOOD_TO_BAD value: "));
                double goodToBad = scanner.nextDouble();
                errorFlag = !Validator.isDoubleValid(goodToBad, 0.0, 1.0);
                if (errorFlag) {
                    continue;
                }
                System.out.print(PrintColor.printInPurple("Please enter BAD_TO_GOOD value: "));
                double badToGood = scanner.nextDouble();
                errorFlag = !Validator.isDoubleValid(badToGood, 0.0, 1.0);
                if (errorFlag) {
                    continue;
                }

                System.out.print(PrintColor.printInPurple("Please enter errorProbabilityGood value: "));
                double errorProbabilityGood = scanner.nextDouble();
                errorFlag = !Validator.isDoubleValid(errorProbabilityGood, 0.0, 1.0);
                if (errorFlag) {
                    continue;
                }

                System.out.print(PrintColor.printInPurple("Please enter errorProbabilityBad value: "));
                double errorProbabilityBad = scanner.nextDouble();
                errorFlag = !Validator.isDoubleValid(errorProbabilityBad, 0.0, 1.0);
                if (errorFlag) {
                    continue;
                }

                this.goodToBad = goodToBad;
                this.badToGood = badToGood;
                this.errorProbabilityGood = errorProbabilityGood;
                this.errorProbabilityBad = errorProbabilityBad;
            } catch (Exception e) {
                e.printStackTrace();
                errorFlag = true;
            }
        } while (errorFlag);
    }


    //override toString method to print the parameters
    @Override
    public String toString() {
        return " Good to Bad: " + goodToBad +
                ", Bad to Good: " + badToGood +
                ", Error Probability Good: " + errorProbabilityGood +
                ", Error Probability Bad: " + errorProbabilityBad;

    }
}
