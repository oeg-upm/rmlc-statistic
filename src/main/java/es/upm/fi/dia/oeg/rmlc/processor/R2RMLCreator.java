package es.upm.fi.dia.oeg.rmlc.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * RMLC for RDF Data Cube
 * @author dchaves
 */

public class R2RMLCreator {


    private String[] headers;
    static Logger log = LoggerFactory.getLogger(R2RMLCreator.class.getPackage().toString());


    public R2RMLCreator (String csvPath){
        log.info("Starting the process for creating R2RML mappings based on statistics CSV");
        readCSVHeaders(csvPath);
    }

    public void readCSVHeaders(String csvPath){
        Path myPath = Paths.get(csvPath);
        log.info("Reading CSV headers");
        try {
            headers = Files.lines(myPath).map(s -> s.split(",")).findFirst().get();
        }catch (IOException e){
            log.error("Error reading the headers of the CSV file: "+e.getMessage());
        }
        log.info("CSV headers read");
    }


    public String createR2RML(String mapping, Integer start, Integer end){
        log.info("Starting RMLC to R2RML conversion");
        StringBuilder r2rmlMapping = new StringBuilder();
        Integer iterations = end-start+2;
        boolean prefixes = true;
        for(Integer i = 1 ; i<iterations ; i++){
            String mappingLines[] = mapping.split("\n");
            for(String line : mappingLines){
                if(line.matches(".*\\{\\$i}.*")){
                    if(i<10)
                        line=line.replace("{$i}","0"+i.toString());
                    else
                        line=line.replace("{$i}",i.toString());
                }
                if(line.matches(".*\\{\\$i.name}.*")){
                    line=line.replace("{$i.name}",headers[start].toUpperCase());
                }
                if(prefixes || !line.matches("@prefix.*"))
                    r2rmlMapping.append(line+"\n");
            }
            prefixes = false;
            start++;
        }
        log.info("Conversion completed!");
        return r2rmlMapping.toString();
    }


}
