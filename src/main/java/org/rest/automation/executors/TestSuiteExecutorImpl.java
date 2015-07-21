package org.rest.automation.executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.model.Environment;
import org.rest.automation.model.TestSuite;
import org.rest.automation.model.TestTask;

import java.io.IOException;

/**
 * Sets up the {@link TestSuite} for execution
 */
public class TestSuiteExecutorImpl implements TestSuiteExecutor {


    private static Logger LOGGER = LogManager.getLogger(TestSuiteExecutorImpl.class);
    Environment environment;
    private TestTaskExecutor testTaskExecutor;

    public TestTaskExecutor getTestTaskExecutor() {
        return testTaskExecutor;
    }

    public void setTestTaskExecutor(TestTaskExecutor testTaskExecutor) {
        this.testTaskExecutor = testTaskExecutor;
    }


    public void execute(TestSuite suite){
        LOGGER.debug("Beginning execution of test suite");
        for (TestTask task : suite.getTestTasks()) {
            testTaskExecutor.runTestTask(task, suite, environment);
        }


    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void shutDown(){
        try {
            LOGGER.info("Shutting down http client");
            testTaskExecutor.shutDown();
        } catch (IOException e) {
            LOGGER.error("Error when releasing the http client");
        }
    }

}
