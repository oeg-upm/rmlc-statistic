package es.upm.fi.dia.oeg.rmlc.main;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * RMLC for RDF Data Cube
 * @author dchaves
 */
public class CommandLineProcessor {
    private static final Logger log = LoggerFactory.getLogger(CommandLineProcessor.class);
    private static final Options cliOptions = generateCLIOptions();



    public static CommandLine parseArguments(String[] args) {
        CommandLineParser cliParser = new DefaultParser();
        CommandLine commandLine = null;
        try{
            commandLine=cliParser.parse(getCliOptions(), args);
        }catch (ParseException e){
            log.error("Error parsing the command line options: "+e.getMessage());
        }
        return commandLine;
    }


    private static Options generateCLIOptions() {
        Options cliOptions = new Options();

        cliOptions.addOption("h", "help", false,
                "show this help message");
        cliOptions.addOption("m", "mapping document", true,
                "the URI of the mapping file (required)");
        cliOptions.addOption("c", "csv file", true,
                "the URI of the csv file (required)");

        return cliOptions;
    }

    public static Options getCliOptions() {
        return cliOptions;
    }

    public static void displayHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Statistic CSV to R2RML", getCliOptions());
        System.exit(1);
    }
}
