package org.rest.automation.specutil;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.model.TestSuite;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by sharpcodes on 4/15/15.
 */
public class SpecResolver {

    private static Logger LOGGER = LogManager.getLogger(SpecResolver.class);

    public TestSuite resolveSpec(String specFile) {
        try {
            File file = new File(specFile);
            JAXBContext jaxbContext = JAXBContext.newInstance(TestSuite.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            TestSuite suite = (TestSuite) jaxbUnmarshaller.unmarshal(file);
            LOGGER.debug("Spec file unmarshalled with Id=" + suite.getId());
            return suite;

        } catch (JAXBException e) {
            LOGGER.error("Error umarshalling xml", e);
        }
        return null;
    }
}
