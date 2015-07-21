package org.rest.automation.jobrunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Defines and parses the input arguements to the main class
 */

public class CommandLineInitializer {

    private static Logger LOGGER = LogManager.getLogger(CommandLineInitializer.class);
    private Options processOptions;

    public void initialize() {
        processOptions = new Options();
        Option specOption=new Option("spec", true, "folder or file containing the test cases in XML format");
        specOption.setRequired(true);
        Option propOption=new Option("props", true, "path to the property file including the file name");
        Option outputOption=new Option("output", true, "folder path in which the output files will be written into");
        outputOption.setRequired(true);
        Option junitOption=new Option("junit", false, "pass this option if you need the corresponding junit reports for each XML file");
        Option logOption=new Option("logPath", true, "the path in which the log files will be written into");
        Option logLevel=new Option("logLevel", true, "the logging levels for the app.Default is info");
        processOptions.addOption(specOption).addOption(propOption).addOption(outputOption).addOption(junitOption).addOption(logOption).addOption(logLevel);
    }

    public CommandLine parseArguments(String[] args) {
        CommandLineParser commandLineParser = new BasicParser();
        CommandLine commandLine = null;
        try {
            commandLine = commandLineParser.parse(processOptions, args);
        } catch (Exception e) {
            LOGGER.error("Exiting....Unable to process arguements " + e.getMessage());
            printHelp();
            System.exit(1);
        }
        return commandLine;
    }


    public void printHelp(){
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "api-automation", processOptions );
    }

}
