package org.rest.automation.properties.manager;

/**
 * Created by sharpcodes on 4/22/15.
 */
public interface PropertiesManager {

    void loadProperties(String resourcePath);

    String getResource(String key);


}
