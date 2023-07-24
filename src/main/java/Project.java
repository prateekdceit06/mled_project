
public class Project {
    public static void main(String[] args) {
//        long startTime = System.currentTimeMillis();
        //todo: change if you want to run code automatically without user input for different seed values
        int runCount = 0;
        do{
            runCount++;
            MledSimulator simulator = MledSimulator.newSimulator();


            System.out.print("Run " + runCount + " finished with seed ");
            boolean result = simulator.quickRun();

            if (result){
                System.out.println("Simulation finished successfully.");
                break;
            }

        } while(true);
        //todo: change if you want to run code automatically without user input for different seed values
//        MledSimulator simulator = MledSimulator.getInstance();
//        simulator.getOptionToStartSimulator();

        //check the time it takes to run the program

//        long endTime = System.currentTimeMillis();
//        long elapsedTime = endTime - startTime;
//        System.out.println("Total time taken: " + elapsedTime/1000 + " seconds");


    }
}
