package org.rest.automation.Utils;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rest.automation.properties.manager.PropertiesManagerImpl;


public class PropertyManagerTest {

    static PropertiesManagerImpl propertiesManager;


    @BeforeClass
    public static void loadProperties() {
        String path = "src/test/resources/test.properties";
        propertiesManager = new PropertiesManagerImpl();
        propertiesManager.loadProperties(path);
    }


    @Test
    public void testLoadedProperties() {
        Assert.assertEquals("sai", propertiesManager.getResource("name"));
    }


}
