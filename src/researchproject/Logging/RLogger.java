/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package researchproject.Logging;

import java.nio.file.FileSystem;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Iskandar
 */
public class RLogger {
    private static Logger logger; 
    public static void initialize()
    {        
        logger = Logger.getLogger("MyLog");  
        FileHandler fh;  
        try {
            // This block configure the logger with handler and formatter 
            String path = System.getProperty("user.dir")+System.getProperty("file.separator")+"LogFile.log";
            fh = new FileHandler(path); 
            logger.setUseParentHandlers(false); 
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  
            logger.info("== LOGGER ==");

        } catch (Exception e) {  
            e.printStackTrace();  
        } 
    }
    public static void info(String message)
    {
        logger.info(message + System.getProperty("line.separator"));
    }
}
