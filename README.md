# Multi Layered Detection Model (MLED) for Error Detection

Multi-Layer Error Detection (MLED) is a layered architecture designed to significantly reduce the Undetected Error
Probability (UEP) in file transfers. This is particularly important for petabyte-scale file transfers, which are often
used for data collected from scientific instruments.

The Multi-Layer Error Detection (MLED) architecture is parameterized by a number of layers *(n)*, and a policy *(P<sub>
i</sub>)* for each layer that describes its operation. The architecture is flexible and allows a different number of
layers over different regions of the network, and different policies to be applied at different layers. Importantly,
error detection schemes used by existing file transfer tools can be expressed in this architecture.
In the MLED architecture, each layer performs error detection between processes at the same layer communicating
logically.

The MLED architecture also includes transmitter/receiver protocols. The transmitter at layer *i* forms a PDU with `data`
in the payload, computes CRC according to *P<sub>i</sub>* and appends it. If it's not the base case of recursion, it
fragments the PDU into *k* fragments according to MTU of *P<sub>i+1</sub>* and sends each fragment to *layer (i+1)*. The
receiver at layer i appends the received fragment to a PDU. If a *layer i's* PDU is reassembled, it checks the CRC. If
the PDU is error-free, its payload is delivered to *layer (i - 1)*, which reassembles all fragments to form its own *
layer (i - 1)* PDU.

This project is for simulating the MLED architecture for error detection as defined above. The simulator is written in
Java and uses the `[GSON]` library for parsing JSON files for configurations. There is an option to add the user defined
configuration on the go as well.

# Running the MLED Simulator

To run the MLED Simulator, you must have Java 8 or higher installed on your computer. You can download the latest
version of Java from [here](https://www.java.com/en/download/).
Build the project using maven build and add the following dependency in your `pom.xml` file.

```xml

<dependencies>
    # GSON dependency
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.6</version>
    </dependency>

    # Logging dependencies
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.14.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.14.1</version>
    </dependency>
</dependencies>
```

The `configs` folder containing the configuration files must be in the root directory of the project. You can create
your custom configurations in `.json` format to run the project. After creating the `.json` file place it in this
configs folder. The `configs` folder contains some sample configurations that you can use to run the project.

# MLED Simulator Configuration Guide

The MLED Simulator uses a JSON configuration file to initialize the simulator with custom parameters. This guide
provides detailed explanations of these parameters and how to modify them.

## Configuration Parameters

The main JSON structure is divided into `simulator` and `layers` sections.

### Simulator

- `lastLayerMTU`: MTU (Maximum Transmission Unit) for the last layer. This represents the maximum amount of data that
  can be transmitted in one packet, excluding headers and trailers. The value should be an integer between 500 and
    10000.

- `seed`: Seed for the random number generator used in the simulation. Any integer can be used.

### Layers

An array of layer configurations, where each layer configuration is an object that includes the following properties.
Please note that the number of layers should be within the range of 1 to 99.

- `fragmentationParameter`: Parameter that determines how the data is fragmented in the layer. Should be an integer
  value between 1 and 10.
- `enableErrorDetection`: Boolean value that determines whether error detection is enabled in the layer. If set to
  `false`, the layer will not perform error detection. If set to `true`, the layer will perform error detection. If this
  value is not specified, it will be set to `true` by default. Though it is important to note that irrespective of the
  value of this parameter, the `errorDetectionMethod` parameter (as described below) for the layer has to be specified
  otherwise the config file will not be read correctly.

- `errorModel`: An object that specifies the error model for the layer. It includes the following properties:

    - `goodToBad`: The probability of transitioning from a good state to a bad state. The value should be a decimal
      between 0.0 and 1.0.

    - `badToGood`: The probability of transitioning from a bad state to a good state. The value should be a decimal
      between 0.0 and 1.0.

    - `errorProbabilityGood`: The probability of an error occurring in a bit when the state is good. The value should be
      a decimal between 0.0 and 1.0.

    - `errorProbabilityBad`: The probability of an error occurring in a bit when the state is bad. The value should be a
      decimal between 0.0 and 1.0.

- `errorDetectionMethod`: An object that specifies the error detection method used by the layer. It includes the
  following properties:

    - `name`: The name of the error detection method. Valid values are "CHECKSUM", "CRC", and "HASH".

    - `checksumLength`: (Required for "CHECKSUM" method) Length of the checksum in bytes. Should be an integer value
      between 1 and 10.

    - `crcPolynomial`: (Required for "CRC" method) A hexadecimal string representing the CRC polynomial. For Example, "
      0x1B" represents the polynomial x<sup>4</sup> + x<sup>3</sup> + x<sup>1</sup> + 1. Make sure that
      the first and last bits of the polynomial are 1.

    - `algorithm`: (Required for "HASH" method) The hashing algorithm used. Can be "MD5", "SHA1", or "SHA256".

## Usage

1. Modify the values of the parameters in the JSON file according to the requirements of your simulation.

2. Save the JSON file in the `configs` directory in the current working directory of the application.

3. Run the Java application. When prompted, select the number corresponding to your modified JSON file.

The MLED Simulator will run with the new configuration settings.

## Example Configuration File

Below is an example of a configuration file:

```json
{
  "simulator": {
    "lastLayerMTU": 500,
    "seed": 123456789
  },
  "layers": [
    {
      "fragmentationParameter": 1,
      "enableErrorDetection": true,
      "errorModel": {
        "goodToBad": 0.5,
        "badToGood": 0.5,
        "errorProbabilityGood": 0.5,
        "errorProbabilityBad": 0.5
      },
      "errorDetectionMethod": {
        "name": "CHECKSUM",
        "checksumLength": 1
      }
    },
    {
      "fragmentationParameter": 2,
      "enableErrorDetection": false,
      "errorModel": {
        "goodToBad": 0.5,
        "badToGood": 0.5,
        "errorProbabilityGood": 0.5,
        "errorProbabilityBad": 0.5
      },
      "errorDetectionMethod": {
        "name": "CRC",
        "crcPolynomial": "0x07"
      }
    },
    {
      "fragmentationParameter": 2,
      "errorModel": {
        "goodToBad": 0.5,
        "badToGood": 0.5,
        "errorProbabilityGood": 0.5,
        "errorProbabilityBad": 0.5
      },
      "errorDetectionMethod": {
        "name": "HASH",
        "algorithm": "MD5"
      }
    }
  ]
}
```

You can modify the values in this JSON file to match your desired configuration. Save the modified file in the "configs"
directory and then run the program. You will be prompted to select a configuration file. Choose the file you just saved
to start the simulation with your specified configurations.

