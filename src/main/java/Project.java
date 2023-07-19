
public class Project {
    public static void main(String[] args) {
        //todo: change if you need an error in a single packet
//        int runCount = 0;
//        do{
//            runCount++;
//            MledSimulator simulator = MledSimulator.newSimulator();


//            System.out.print("Run " + runCount + " finished with seed ");
//            boolean result = simulator.quickRun();

//            if (result){
//                System.out.println("Simulation finished successfully.");
//                break;
//            }

//        } while(true);
        MledSimulator simulator = MledSimulator.getInstance();
        simulator.getOptionToStartSimulator();


    }
}
