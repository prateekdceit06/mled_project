import java.util.Random;

public class ErrorModelGilbertElliot implements ErrorModel {

    private final double goodToBad;
    private final double badToGood;
    private final double errorProbabilityGood;
    private final double errorProbabilityBad;
    private Random random;
    private int seed;
    private boolean state;  // true is bad, false is good
    private String errorModelName = "Gilbert-Elliot";

    //empty constructor
    public ErrorModelGilbertElliot() {
        this.goodToBad = Constants.GOOD_TO_BAD;
        this.badToGood = Constants.BAD_TO_GOOD;
        this.errorProbabilityGood = Constants.errorProbabilityGood;
        this.errorProbabilityBad = Constants.errorProbabilityBad;
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

    @Override
    public void configure() {
        // TODO Auto-generated method stub
        //take input from user for the parameters
        //goodToBad, badToGood, errorProbabilityGood, errorProbabilityBad
    }

    //override toString method to print the parameters
    @Override
    public String toString() {
        return "Error Model Name: " + errorModelName +
                ", Good to Bad: " + goodToBad +
                ", Bad to Good: " + badToGood +
                ", Error Probability Good: " + errorProbabilityGood +
                ", Error Probability Bad: " + errorProbabilityBad;

    }
}
