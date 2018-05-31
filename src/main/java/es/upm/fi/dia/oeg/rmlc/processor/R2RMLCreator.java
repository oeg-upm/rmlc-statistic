package es.upm.fi.dia.oeg.rmlc.processor;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


/**
 * RMLC
 * @author dchaves
 */

public class R2RMLCreator {


    private String[] headers;
    private ArrayList<String> mappingColumns;
    private JSONObject dictionary;
    private boolean processType;
    private Integer start;
    private Integer end;
    private String rmlcMapping;
    static Logger log = LoggerFactory.getLogger(R2RMLCreator.class.getPackage().toString());


    public R2RMLCreator (String csvPath, String mapping){
        log.info("Starting the process for creating R2RML mappings based on statistics CSV");
        this.rmlcMapping = mapping;
        mappingColumns = new ArrayList<>();
        this.start = null;
        this.end = null;
        readCSVHeaders(csvPath);
        readRange();
    }

    public String createR2RML(){
        log.info("Starting RMLC to R2RML conversion");
        String r2rmlMapping="";
        if((start!=null && end !=null) || !mappingColumns.isEmpty()){
            if(processType)
                r2rmlMapping = createR2RMLbyRange();
            else
                r2rmlMapping = createR2RMLbyColumns();
            log.info("Conversion completed!");
        }
        else if(!processType){
            log.error("Check the rmlc:columns in the RMLC mapping");
        }
        else if(processType){
            log.error("Check the rmlc:columnRange in the RMLC mapping");
        }
        return r2rmlMapping;
    }

    private String createR2RMLbyRange(){
        Integer iterations = end - start + 2;
        StringBuilder r2rmlMapping = new StringBuilder();
        ArrayList<String> mappingLines = readAndRemovePrefix(r2rmlMapping);
        for (int i = 1; i < iterations; i++) {
            ArrayList<String> auxMappingLines = (ArrayList<String>) mappingLines.clone();
            for (String line : auxMappingLines) {
                if (line.matches(".*\\{\\$column}.*")) {
                    line = line.replace("{$column}", headers[start].toUpperCase());
                }
                if (line.matches(".*\\{\\$alias}.*")) {
                    if(dictionary.has(headers[start]))
                        line = line.replace("{$alias}", dictionary.getString(headers[start]));
                    else
                        line = line.replace("{$alias}", headers[start]);
                }
                if (!line.matches(".*rmlc.*"))
                    r2rmlMapping.append(line + "\n");
            }
            start++;
        }
        return r2rmlMapping.toString();
    }

    private String createR2RMLbyColumns(){
        Integer iterations = mappingColumns.size();
        StringBuilder r2rmlMapping = new StringBuilder();
        ArrayList<String> mappingLines = readAndRemovePrefix(r2rmlMapping);
        for(int i=0; i < iterations ; i++){
            ArrayList<String> auxMappingLines = (ArrayList<String>) mappingLines.clone();
            for (String line : auxMappingLines) {
                if (line.matches(".*\\{\\$column}.*")) {
                    line = line.replace("{$column}", mappingColumns.get(i).toUpperCase());
                }
                if (line.matches(".*\\{\\$alias}.*")) {
                    if(dictionary.has(mappingColumns.get(i)))
                        line = line.replace("{$alias}", dictionary.getString(mappingColumns.get(i)));
                    else
                        line = line.replace("{$alias}", mappingColumns.get(i));
                }
                if (!line.matches(".*rmlc.*"))
                    r2rmlMapping.append(line + "\n");
            }
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

    private void readRange(){
        String mappingLines[] = rmlcMapping.split("\n");
        for(String line : mappingLines){
            if(line.matches(".*rmlc:columnRange.*")){
                String startColumn = line.split("\"")[1];
                String endColumn = line.split("\"")[3];
                for(int i=0; i<headers.length ; i++){
                    if(headers[i].equals(startColumn)){
                        this.start = i;
                    }
                    else if(headers[i].equals(endColumn)){
                        this.end = i;
                    }
                }
                processType = true;
                log.info("Column range loaded");
            }
            else if(line.matches(".*rmlc:columns.*")){
                StringTokenizer st = new StringTokenizer(line,"[");
                st.nextToken();
                st = new StringTokenizer(st.nextToken(),"]");
                st = new StringTokenizer(st.nextToken(),",");
                while(st.hasMoreTokens()){
                    mappingColumns.add(st.nextToken().replace("\"",""));
                }
                processType=false;
                log.info("Columns loaded");
            }
            else if(line.matches(".*rmlc:dictionary.*\\{.*\\};")){
                StringTokenizer st = new StringTokenizer(line,"{");
                st.nextToken();
                dictionary = new JSONObject("{"+st.nextToken().replace(";",""));
                log.info("Dictionary loaded");
            }
            else if(line.matches(".*rmlc:dictionary.*\".*")){
                StringTokenizer st = new StringTokenizer(line,"\"");
                st.nextToken();
                dictionary = new JSONObject(readjson(st.nextToken().replace(";","")));
                log.info("Dictionary loaded");
            }
        }

    }

    public String readjson(String path){
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        }catch (IOException e){
            log.error("Error reading the json file: "+e.getMessage());
            return null;
        }
        log.info("JSON file reading correctly");
        return content;
    }




}
