package org.rest.automation.executors;

import org.rest.automation.model.Environment;
import org.rest.automation.model.TestSuite;


public interface TestSuiteExecutor {


    void execute(TestSuite suite);

    void setEnvironment(Environment environment);

    void shutDown();


}
