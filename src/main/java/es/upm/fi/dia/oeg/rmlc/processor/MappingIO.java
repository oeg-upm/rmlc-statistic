package es.upm.fi.dia.oeg.rmlc.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * RMLC for RDF Data Cube
 * @author dchaves
 */

public class MappingIO {

    static Logger log = LoggerFactory.getLogger(MappingIO.class.getPackage().toString());

    public static String readMapping(String path){
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        }catch (IOException e){
            log.error("Error reading the mapping file: "+e.getMessage());
            return null;
        }
        log.info("RMLC mapping reading correctly");
        return content;
    }


    public static boolean writeMapping(String path, String content){
        try {

            Files.write(Paths.get(path.split("\\.")[0]+".r2rml.ttl"), content.getBytes());
        }catch (IOException e){
            log.error("Error writing the R2RML file: "+e.getMessage());
            return false;
        }
        log.info("Writing R2RML mapping file...");
        return true;
    }
}
