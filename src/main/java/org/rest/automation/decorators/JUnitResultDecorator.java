package org.rest.automation.decorators;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.concurrency.SpecLoader;
import org.rest.automation.model.TestSuite;
import org.rest.automation.model.junit.JUnitModelMapper;
import org.rest.automation.model.junit.Testsuite;


import java.io.File;


public class JUnitResultDecorator implements Decorator<TestSuite> {


    private static Logger LOGGER = LogManager.getLogger(JUnitResultDecorator.class);
    private String outputPath;
    private JUnitModelMapper jUnitModelMapper;

    public JUnitModelMapper getjUnitModelMapper() {
        return jUnitModelMapper;
    }

    public void setjUnitModelMapper(JUnitModelMapper jUnitModelMapper) {
        this.jUnitModelMapper = jUnitModelMapper;
    }

    public void formatResult(TestSuite suite) {

        try {
            /*
            map application object to JUNIT object
             */
            Testsuite testsuite = jUnitModelMapper.mapJunit(suite);

            String dir = outputPath + "/junitreport/" + suite.getFilePath();
            String filePath = "/suite-" + suite.getId() + ".xml";

            File file = SpecLoader.getFileForDestination(dir, filePath);
            /*
             *Marshal resource to JUNIT output
             */
            MarshellerUtil.<Testsuite>marshalResource(Testsuite.class, testsuite, file);

            LOGGER.debug("writing JUNIT file for testsuide id-->"+suite.getId());
        } catch (Exception e) {
            LOGGER.error("unable to generate JUNIT Reports " + e.getMessage());
        }

    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;


    }


}
