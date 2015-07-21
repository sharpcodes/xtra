package org.rest.automation.decorators;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.concurrency.SpecLoader;
import org.rest.automation.model.TestSuite;


import java.io.File;



public class XMLTestDecorator implements Decorator<TestSuite> {

    private static Logger LOGGER = LogManager.getLogger(XMLTestDecorator.class);
    private String outputPath;

    public void formatResult(TestSuite suite) {
        try {
            String dir = outputPath + "/" + suite.getFilePath();
            String filePath = "/suite-" + suite.getId() + ".xml";
            File file = SpecLoader.getFileForDestination(dir, filePath);
            /*
             *Marshal resource to JUNIT output
             */
            MarshellerUtil.<TestSuite>marshalResource(TestSuite.class, suite, file);

        } catch (Exception e) {
            LOGGER.error("Error when writing XML output files" + e.getMessage());
        }


    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
        File file = new File(outputPath);
        file.mkdirs();

    }


}
