package es.upm.fi.dia.oeg.rmlc.main;


import es.upm.fi.dia.oeg.rmlc.processor.MappingIO;
import es.upm.fi.dia.oeg.rmlc.processor.R2RMLCreator;
import org.apache.commons.cli.CommandLine;
import org.apache.log4j.BasicConfigurator;

/**
 * RMLC for RDF Data Cube
 * @author dchaves
 */
public class Main
{

    public static void main( String[] args )
    {
        BasicConfigurator.configure();
        CommandLine commandLine = CommandLineProcessor.parseArguments(args);

        if(commandLine.getOptions().length != 2){
            CommandLineProcessor.displayHelp();
        }

        String mapping = commandLine.getOptionValue("m");
        String csv = commandLine.getOptionValue("c");

        R2RMLCreator creator = new R2RMLCreator(csv,MappingIO.readMapping(mapping));
        MappingIO.writeMapping(mapping,creator.createR2RML());

    }
}
