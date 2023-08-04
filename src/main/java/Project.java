import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

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
            clearOutputFolder();
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
        clearOutputFolder();
        MledSimulator simulator = MledSimulator.getInstance();
        simulator.getOptionToStartSimulator();
    }

    private static void clearOutputFolder() {
        String folderPath = "./output";
        try {
            deleteFilesInFolder(folderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void deleteFilesInFolder(String folderPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.getFileName().toString().equals("receivedData.csv"))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}
