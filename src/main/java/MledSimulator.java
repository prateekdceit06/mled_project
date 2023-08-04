
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.exit;

public class MledSimulator {

    private static MledSimulator instance = null;
    Scanner scanner = new Scanner(System.in);

    private int layerNum = 0;

    private List<Layer> layers = new ArrayList<>();

    private int lastNodeID;

    private ErrorModel errorModel;

    private int lastLayerMTU;

    private MledSimulator() {
    }


    //todo: change if you want to run code automatically without user input for different seed values
    public static MledSimulator newSimulator() {
        instance = new MledSimulator();
        return instance;
    }

    public static MledSimulator getInstance() {
        if (instance == null) {
            instance = new MledSimulator();
        }
        return instance;
    }

    public int getLayerNum() {
        return layerNum;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayerNum(int layerNum) {
        this.layerNum = layerNum;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public void setErrorModel(ErrorModel errorModel) {
        this.errorModel = errorModel;
    }

    public void setLastLayerMTU(int lastLayerMTU) {
        this.lastLayerMTU = lastLayerMTU;
    }

    public int getLastNodeID() {
        return lastNodeID;
    }

    public void setLastNodeID(int lastNodeID) {
        this.lastNodeID = lastNodeID;
    }

    public void getOptionToStartSimulator() {
        boolean errorFlag = false;
        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
            }
            Menu.initialiseSimulatorMenu();
            try {
                System.out.print(PrintColor.printInYellow("How do you want to initialise the simulator: "));
                int initialise = scanner.nextInt();
                scanner.nextLine();
                errorFlag = !Validator.isIntValid(initialise, 1, 3);
                switch (initialise) {
                    case 1:
                        initializeFromDefaultConfigFile();
                        break;
                    case 2:
                        initialiseCustomSimulator();
                        break;
                    case 3:
                        exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorFlag = true;
            }
        } while (errorFlag);

    }

    private void initializeFromDefaultConfigFile() {
        readConfigFromFile();
        start();
    }

    public void initialiseCustomSimulator() {
        configureSimulator();
        start();

    }

    private void configureSimulator() {
        boolean errorFlag = false;
        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
            }
            try {
                System.out.print(PrintColor.printInGreen("Enter the number of error checking layers [1-99]: "));
                layerNum = scanner.nextInt();
                scanner.nextLine();
                errorFlag = !Validator.isIntValid(layerNum, 1, 99);
                if (!errorFlag) {
                    lastNodeID = (int) Math.pow(2, layerNum - 1) + 1;
                }
            } catch (Exception e) {
                errorFlag = true;
            }
        } while (errorFlag);

        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
            }
            try {
                System.out.print(PrintColor.printInBlue("Enter the number MTU for the last layer [500-10000]: "));
                lastLayerMTU = scanner.nextInt();
                scanner.nextLine();
                errorFlag = !Validator.isIntValid(lastLayerMTU, 500, 10000);
            } catch (Exception e) {
                errorFlag = true;
            }
        } while (errorFlag);


        for (int i = 1; i <= layerNum; i++) {
            int fragmentationParameter = 1;
            do {
                if (errorFlag) {
                    System.out.println(PrintColor.printInRed("Error: Invalid input. Please try again."));
                }
                try {
                    if (i != 1) {
                        System.out.print(PrintColor.printInYellow("Enter the fragmentation parameter for layer " + i + " [1-10]: "));
                        fragmentationParameter = scanner.nextInt();
                        scanner.nextLine();
                        errorFlag = !Validator.isIntValid(fragmentationParameter, 1, 10);
                        if (errorFlag) {
                            continue;
                        }
                    }

                    Menu.errorModelMenu();
                    System.out.print(PrintColor.printInPurple("Enter the error model for layer " + i + ": "));
                    int edModel = scanner.nextInt();
                    scanner.nextLine();
                    errorFlag = !Validator.isIntValid(edModel, 1, 8);
                    if (edModel > 0 && edModel <= Constants.ErrorType.values().length) {
                        Constants.ErrorType selectedErrorType = Constants.ErrorType.values()[edModel - 1];
                        double[] errorValues = selectedErrorType.getErrorValues();
                        errorModel = new ErrorModelGilbertElliot(errorValues[0], errorValues[1], errorValues[2], errorValues[3]);
                    } else if (edModel == Constants.ErrorType.values().length + 1) {
                        errorModel = new ErrorModelGilbertElliot();
                        errorModel.configure();
                    } else if (edModel == Constants.ErrorType.values().length + 2) {
                        exit(1);
                    } else {
                        System.out.println(PrintColor.printInRedBack("Invalid input"));
                        errorFlag = true;
                    }
                    if (errorFlag) {
                        continue;
                    }

                    Menu.errorCheckMethodMenu();
                    System.out.print(PrintColor.printInCyan("Select the error detection method for layer " + i + ": "));
                    int edm = scanner.nextInt();
                    scanner.nextLine();
                    errorFlag = !Validator.isIntValid(edm, 1, 4);
                    if (!errorFlag) {
                        String errorDetectionMethodName = "";
                        switch (edm) {
                            case 1:
                                errorDetectionMethodName = Constants.errorDetectionMethodEnum.CHECKSUM.toString();
                                break;
                            case 2:
                                errorDetectionMethodName = Constants.errorDetectionMethodEnum.CRC.toString();
                                break;
                            case 3:
                                errorDetectionMethodName = Constants.errorDetectionMethodEnum.HASH.toString();
                                break;
                            case 4:
                                exit(1);
                            default:
                                System.out.println(PrintColor.printInRed("Invalid input"));
                                errorFlag = true;
                        }
                        ErrorDetectionMethod errorDetectionMethod =
                                ErrorDetectionMethodFactory.getErrorDetectionMethod(errorDetectionMethodName);
                        errorDetectionMethod.configure();
                        layers.add(new Layer(i, fragmentationParameter, errorDetectionMethodName,
                                errorDetectionMethod, errorModel));

                        // Number of nodes in each layer is 2^(i-1) + 1
                        int numNodes = (int) Math.pow(2, i - 1) + 1;
                        layers.get(i - 1).addNodes(layerNum, numNodes, errorDetectionMethod, errorModel, true);
                        System.out.println(PrintColor.printInGreenBack("Layer " + i +
                                " created with fragmentation parameter " + fragmentationParameter +
                                " and error detection method " + errorDetectionMethodName));
                        System.out.println();
                    }

                } catch (Exception e) {
                    errorFlag = true;
                }
            } while (errorFlag);
        }
    }

    private void createRoute() {

        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            List<Node> nodes = layer.getNodes();

            for (int j = 0; j < nodes.size(); j++) {
                Node node = nodes.get(j);
                if (node.getLayerID() != 1) {
                    int parentLayerID = node.getLayerID() - 1;
                    List<Integer> nodeIDs = layers.get(parentLayerID - 1).getNodeIDs();
                    int parentNodeID = node.getNodeID();
                    if (nodeIDs.contains(parentNodeID)) {
                        for (Node parentNode : layers.get(parentLayerID - 1).getNodes()) {
                            if (parentNode.getNodeID() == parentNodeID) {
                                node.setParentNode(parentNode);
                                parentNode.setChildNode(node);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void calculateMTU() {
        int LayerMTU = lastLayerMTU;
        for (int i = layerNum; i >= 1; i--) {
            layers.get(i - 1).setMTU(LayerMTU);
            for (Node node : layers.get(i - 1).getNodes()) {
                node.setMTU(LayerMTU);
            }
            LayerMTU = LayerMTU * layers.get(i - 1).getFragmentationParameter();

        }
    }

    private void runNetwork() {
        ApplicationSender applicationSender = new ApplicationSender();
        applicationSender.readFileAndSendData();
    }

    private void start() {
        calculateMTU();
        createRoute();
        System.out.println(PrintColor.printInGreenBack("MLED Simulator starting.."));
        CommonFunctions.printNetwork(layers);
        System.out.println(PrintColor.printInGreenBack("MLED Simulator started successfully."));
        CommonFunctions.pause();
        //todo:change print statements
        System.out.println(PrintColor.printInGreenBack("Running Simulation...."));
        runNetwork();
        CommonFunctions.printStats(layers);
        CommonFunctions.pause();
        //todo:change print statements
//        CommonFunctions.printInterestingPacketNames(layers);
        AnalyseNodesForErrorDetection analyseNodesForErrorDetection = new AnalyseNodesForErrorDetection();
        analyseNodesForErrorDetection.analyseNodesForErrorDetection(layerNum, layers);
        CommonFunctions.printNetwork(layers);

    }

    private void readConfigFromFile() {
        SimulatorConfig config = new SimulatorConfig();
        boolean errorFlag = false;
        do {
            try {
                if (errorFlag) {
                    System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
                }
                String currentWorkingDirectory = System.getProperty("user.dir");
                File directory = new File(currentWorkingDirectory + File.separator + "configs");
                File[] files = directory.listFiles();
                ArrayList<File> filteredFiles = Menu.fileMenu(files);

                System.out.print(PrintColor.printInYellow("Enter the number of the file you want to select: "));
                int choice = scanner.nextInt();

                if (choice > 0 && choice <= filteredFiles.size()) {
                    String fileName = directory + File.separator + filteredFiles.get(choice - 1).getName();
                    config.readConfig(fileName, this);
                } else if (choice == filteredFiles.size() + 1) {
                    exit(0);
                } else {
                    errorFlag = true;
                }
            } catch (Exception e) {
                System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
                errorFlag = true;
                e.printStackTrace();
            }

        } while (errorFlag);
    }


    //todo: change if you want to run code automatically without user input for different seed values
    public boolean quickRun() {

        SimulatorConfig config = new SimulatorConfig();
        int newValue = 0;
        try {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();  // create Gson instance with pretty printing
            Random random = new Random();  // create Random instance for generating random numbers

            try (Reader reader = new FileReader("./configs/_input_config_for_autorun_to_find_seed_for_undetected_error.json")) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();  // read JSON file

                // Generate a random integer
                newValue = random.nextInt(10000000);  // Generate random integers in range 0 to 1000000, change range as per your need

                // Modify the 'seed' property under 'simulator' with the random integer
                jsonObject.getAsJsonObject("simulator").addProperty("seed", newValue);
                System.out.println(newValue);

                try (Writer writer = new FileWriter("configs/_output.json")) {
                    gson.toJson(jsonObject, writer);  // write modified JSON to a new file
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            config.readConfig("./configs/_output.json", this);


            calculateMTU();
            createRoute();
            runNetwork();
//            CommonFunctions.printStats(layers);
            AnalyseNodesForErrorDetection analyseNodesForErrorDetection = new AnalyseNodesForErrorDetection();
            analyseNodesForErrorDetection.analyseNodesForErrorDetection(layerNum, layers);

            //todo: uncomment to find seed where at least an error is undetected at the lowest layer
            return flawedCRC(newValue);

            //todo: Uncomment to find 1 undetected error
//            return oneUndetectedError();

            //todo: Uncomment to find configuration in which hash misses the undetected error
//            return hashMissesUndetectedError();

        } catch (Exception e) {
            System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
            e.printStackTrace();

        }
        return false;

    }

    private boolean flawedCRC(int newValue) {
        int undetectedErrorCount = 0;

        for (Node node : layers.get(layerNum - 1).getNodes()) {
            undetectedErrorCount += node.getActualUndetectedErrorsCount();
        }

        if (undetectedErrorCount > 0) {
            try (FileWriter writer = new FileWriter("./undetectedError.txt", true)) {
                writer.write("undetectedErrorCount: " + undetectedErrorCount + " Seed: " + newValue + "\n");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }
            System.out.println(PrintColor.printInRedBack("undetectedErrorCount = " + undetectedErrorCount));
        }

        if (undetectedErrorCount > 5) {
            return true;
        }
        return false;
    }

    private boolean oneUndetectedError() {
        int errorAdded = 0;
        int errorDetected = 0;
        for (Layer layer : layers) {
            for (Node node : layer.getNodes()) {
                errorAdded += node.getErrorAddedCount();
                errorDetected += node.getErrorDetectedCount();
            }
        }


        if (errorAdded == 1 && errorDetected == 0) {
            return true;
        }
        return false;
    }

    private boolean hashMissesUndetectedError() {
        boolean fileExists = CommonFunctions.checkFileExistsInFolder("astroMLFiles", "receivedData.csv");


        if (fileExists) {
            try {
                byte[] file1Bytes = Files.readAllBytes(Paths.get("./astroMLFiles/receivedData.csv"));
                byte[] file2Bytes = Files.readAllBytes(Paths.get("./astroMLFiles/" + Constants.fileNameToRead));

                return !java.util.Arrays.equals(file1Bytes, file2Bytes);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
