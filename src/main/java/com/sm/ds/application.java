package com.sm.ds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by mcsri on 6/5/2017.
 */
@SuppressWarnings("ALL")
public class application {

    private static final Logger LOGGER = LoggerFactory.getLogger(application.class);
    private static Properties PROP = null;

    /**
     * Main function of the application
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {

        // Read the configuration file
        if (args.length !=1 ) { System.out.println("Missing configuration file"); System.exit(0);}

        if(PROP == null) {
            loadProperties(args[0]);
        }

        // Switch cases based on configuration.properties
        if(PROP.getProperty("op").toLowerCase().equals("token")){
            String file = PROP.getProperty("tokenizeIp");
            LOGGER.info("Initiating tokenization of the input file: " + file);
            tokenizer t = new tokenizer();
            t.execute(file);
            LOGGER.info("Done.");
        } else if (PROP.getProperty("op").toLowerCase().equals("train")) {
            LOGGER.info("Initiating training using CRFClassifier");
            classifier c = new classifier();
            c.train(PROP);
            LOGGER.info("Done.");
        } else if (PROP.getProperty("op").toLowerCase().equals("test")) {
            LOGGER.info("Initiating testing");
            classifier c = new classifier();
            c.test(PROP);
            LOGGER.info("Done.");
        }
    }

    /**
     * Load property file
     * @param configFile (Configuration file location)
     * @throws IOException
     */
    private static void loadProperties(String configFile) throws IOException {
        Properties prop = new Properties();
        String propFileName = configFile;

        //Read config file as input stream
        InputStream inputStream = new FileInputStream(new File(propFileName));

        if (inputStream != null) {
            prop.load(inputStream);
            PROP = prop;
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found");
        }

    }
}
