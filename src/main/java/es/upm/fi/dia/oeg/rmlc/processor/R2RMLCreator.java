package es.upm.fi.dia.oeg.rmlc.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * RMLC for RDF Data Cube
 * @author dchaves
 */

public class R2RMLCreator {


    private String[] headers;
    private Integer start;
    private Integer end;
    private String rmlcMapping;
    static Logger log = LoggerFactory.getLogger(R2RMLCreator.class.getPackage().toString());


    public R2RMLCreator (String csvPath, String mapping){
        log.info("Starting the process for creating R2RML mappings based on statistics CSV");
        this.rmlcMapping = mapping;
        readCSVHeaders(csvPath);
        this.start = null;
        this.end = null;
        readStartEnd();
    }

    public String createR2RML(){
        log.info("Starting RMLC to R2RML conversion");
        StringBuilder r2rmlMapping = new StringBuilder();
        if(start==null || end ==null){
            log.error("Check the start and end columns in the RMLC mapping, they do not exist");
        }
        else {
            Integer iterations = end - start + 2;
            ArrayList<String> mappingLines = readAndRemovePrefix(r2rmlMapping);
            for (Integer i = 1; i < iterations; i++) {
                ArrayList<String> auxMappingLines = (ArrayList<String>) mappingLines.clone();
                for (String line : auxMappingLines) {
                    if (line.matches(".*\\{\\$i}.*")) {
                        if (i < 10)
                            line = line.replace("{$i}", "0" + i.toString());
                        else
                            line = line.replace("{$i}", i.toString());
                    }
                    if (line.matches(".*\\{\\$i.name}.*")) {
                        line = line.replace("{$i.name}", headers[start].toUpperCase());
                    }
                    if (!line.matches(".*rmlc.*"))
                        r2rmlMapping.append(line + "\n");
                }
                start++;
            }
            log.info("Conversion completed!");
        }
        return r2rmlMapping.toString();
    }


    private ArrayList<String> readAndRemovePrefix (StringBuilder r2rmlMapping){
        String mappingLines[] = rmlcMapping.split("\n");
        ArrayList<String> linesWithoutPrefix = new ArrayList<>();
        for(String line : mappingLines){
            if(line.matches("@prefix.*")){
                r2rmlMapping.append(line+"\n");
            }
            else{
                linesWithoutPrefix.add(line);
            }
        }
        return linesWithoutPrefix;
    }


    private void readCSVHeaders(String csvPath){
        Path myPath = Paths.get(csvPath);
        log.info("Reading CSV headers");
        try {
            headers = Files.lines(myPath).map(s -> s.split(",")).findFirst().get();
        }catch (IOException e){
            log.error("Error reading the headers of the CSV file: "+e.getMessage());
        }
        log.info("CSV headers read");
    }

    private void readStartEnd(){
        String mappingLines[] = rmlcMapping.split("\n");
        String startColumn="",endColumn="";
        for(String line : mappingLines){
            if(line.matches(".*rmlc:columnStart.*")){
                startColumn = line.split("\"")[1];
            }
            else if(line.matches(".*rmlc:columnEnd.*")){
                endColumn = line.split("\"")[1];
            }
        }

        for(int i=0; i<headers.length ; i++){
            if(headers[i].equals(startColumn)){
                this.start = i;
            }
            else if(headers[i].equals(endColumn)){
                this.end = i;
            }
        }
    }




}
