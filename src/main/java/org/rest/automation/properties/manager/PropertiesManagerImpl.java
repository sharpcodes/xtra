package org.rest.automation.properties.manager;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.exceptions.InvalidExpressionException;
import org.rest.automation.jsonparser.ExpressionResolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sharpcodes on 4/22/15.
 */
public class PropertiesManagerImpl implements PropertiesManager {

    private static Logger LOGGER = LogManager.getLogger(PropertiesManagerImpl.class);
    private String resourcePath;
    private Map<String, String> configuration = new HashMap<String, String>();
    private ExpressionResolver expressionResolver;
    private boolean notLoaded;

    public boolean isNotLoaded() {
        return notLoaded;
    }

    public void setNotLoaded(boolean notLoaded) {
        this.notLoaded = notLoaded;
    }

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }


    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }


    public void loadProperties(String resourcePath) {
        try {
            LOGGER.info("Preparing to load properties...");
            FileReader reader = new FileReader(new File(resourcePath));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] property = line.split("=");
                if (property.length == 2) {
                    if (property[1].trim().startsWith("${func:")) {
                        String result = expressionResolver.resolveFunction(property[1]);
                        configuration.put(property[0].trim(), result);

                    }
                    configuration.put(property[0].trim(), property[1].trim());
                } else {
                    LOGGER.warn("invalid entries in properties file -->" + line);
                }
            }
            LOGGER.info("---loaded properties successfully--");

        } catch (IOException e) {
            LOGGER.error("Exiting abruptly due to error when loading properties " + e.getMessage());
            System.exit(1);

        } catch (InvalidExpressionException e) {
            LOGGER.error("Exiting abruptly due to error when loading function expression.." + e.getMessage());
            System.exit(1);


        }

    }


    public String getResource(String key) {
        return configuration.get(key);
    }


}
