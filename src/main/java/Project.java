
public class Project {
    public static void main(String[] args) {
        //todo: change if you want to run code automatically without user input for different seed values
//        runSeedFinder();

        //todo: change if you want to run code automatically without user input for different seed values
        runUserConfig();


    }

    private static void runSeedFinder() {
        int runCount = 0;
        do {
            runCount++;
            MledSimulator simulator = MledSimulator.newSimulator();

            System.out.print("Run " + runCount + " finished with seed ");
            boolean result = simulator.quickRun();

            if (result) {
                System.out.println("Simulation finished successfully.");
                break;
            }

        } while (true);
    }

    private static void runUserConfig() {
        MledSimulator simulator = MledSimulator.getInstance();
        simulator.getOptionToStartSimulator();
    }
}
