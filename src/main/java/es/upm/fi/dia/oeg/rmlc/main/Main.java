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

        if(commandLine.getOptions().length != 4){
            CommandLineProcessor.displayHelp();
        }

        String mapping = commandLine.getOptionValue("m");
        String csv = commandLine.getOptionValue("c");
        Integer start = Integer.parseInt(commandLine.getOptionValue("s"));
        Integer end = Integer.parseInt(commandLine.getOptionValue("e"));

        R2RMLCreator creator = new R2RMLCreator(csv);
        MappingIO.writeMapping(mapping,creator.createR2RML(MappingIO.readMapping(mapping),start,end));

    }
}
