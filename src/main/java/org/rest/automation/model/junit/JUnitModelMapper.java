package org.rest.automation.model.junit;

import org.rest.automation.model.TestSuite;
import org.rest.automation.model.TestTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharpcodes on 5/18/15.
 */
public class JUnitModelMapper {


    public Testsuite mapJunit(TestSuite testSuite) {
        Testsuite jUnitsuite = new Testsuite();
        jUnitsuite.setId(testSuite.getId());
        jUnitsuite.setFailures(Integer.toString(testSuite.getFailureCount()));
        jUnitsuite.setTests(Integer.toString(testSuite.getTestTasks().size()));
        List<Testcase> testcaseList = new ArrayList<Testcase>();
        for (TestTask testTask : testSuite.getTestTasks()) {
            Testcase testcase = new Testcase();
            testcase.setName(testTask.getId());
            if (testTask.getError() != null) {
                Error error = new Error();
                error.setMessage(testTask.getError());
                List<Error> errorList = new ArrayList<Error>();
                errorList.add(error);
            }
            if (testTask.isPassed()) {
                testcase.setStatus("Success");
            } else {
                testcase.setStatus("Failed");
            }
            testcaseList.add(testcase);


        }

        jUnitsuite.testcase = testcaseList;
        return jUnitsuite;
    }
}
