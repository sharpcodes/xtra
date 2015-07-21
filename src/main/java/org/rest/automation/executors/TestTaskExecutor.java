package org.rest.automation.executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.exceptions.InvalidExpressionException;
import org.rest.automation.exceptions.JsonPathException;
import org.rest.automation.decorators.TestResultDecorator;
import org.rest.automation.httpAdapter.ResourceInvocationHandler;
import org.rest.automation.model.*;
import org.rest.automation.specutil.NullCheckWrapperUtil;

import java.io.IOException;

/**
 * Responsible for executing the {@link TestTask}
 */
public class TestTaskExecutor {


    private static Logger LOGGER = LogManager.getLogger(TestTaskExecutor.class);
    private ResourceInvocationHandler resourceInvocationHandler;

    private AssertionExecutor assertionExecutor;
    private TestResultDecorator<TestSuite> resultDecorator;


    public AssertionExecutor getAssertionExecutor() {
        return assertionExecutor;
    }

    public void setAssertionExecutor(AssertionExecutor assertionExecutor) {
        this.assertionExecutor = assertionExecutor;
    }

    public TestResultDecorator getResultDecorator() {
        return resultDecorator;
    }

    public void setResultDecorator(TestResultDecorator<TestSuite> resultDecorator) {
        this.resultDecorator = resultDecorator;
    }

    public ResourceInvocationHandler getResourceInvocationHandler() {
        return resourceInvocationHandler;
    }

    public void setResourceInvocationHandler(ResourceInvocationHandler resourceInvocationHandler) {
        this.resourceInvocationHandler = resourceInvocationHandler;
    }

    /**
     *
     * @param testTask the testTask to be executed
     * @param testSuite the TestSuite wrapper for the testTask
     * @param environment the environment configuration
     */
    public void runTestTask(TestTask testTask, TestSuite testSuite, Environment environment) {

        /*
         * check if all mandatory fields are set
         */
        ProcessStatus notNullCheck = NullCheckWrapperUtil.checkIfNotNull(testTask, "id", "method", "resource");
        if (notNullCheck.isPassed()) {
            /*
              Run the http calls
             */try {

                resourceInvocationHandler.executeResourceCalls(testTask, testSuite);


            /*
             Run the status check assertions if set
             */
                if (testTask.getStatusCheck() != null) {
                    assertionExecutor.executeStatusAssertion(testTask, testSuite);
                }
            /*
            Run the value assertions if set
             */
                if (testTask.getAssertions() != null && testTask.getAssertions().size() > 0) {
                    for (ValueAssertion assertion : testTask.getAssertions()) {
                        assertionExecutor.executeAssertion(assertion, testSuite.getTestContext());
                        if (!assertion.isPassed()) {
                            testSuite.setFailureCount(testSuite.getFailureCount() + 1);
                            testTask.setPassed(false);
                        }

                    }
                }
            }catch (IOException e) {
                LOGGER.error("IO Exception when executing testSuiteId " + testSuite.getId(), e);
                testTask.setError(e.getMessage());
            } catch (JsonPathException e) {
                LOGGER.error("Json path Exception when executing testSuiteId " + testSuite.getId(), e);
                testTask.setError(e.getMessage());

            } catch (InvalidExpressionException e) {
                LOGGER.error("InvalidExpression  when executing testSuiteId" + testSuite.getId(), e);
                testTask.setError(e.getMessage());
            }
        } else {
            testTask.setError(notNullCheck.getMessage());
        }

    }



    /**
     *Shutdown the http client
     */
    public void shutDown() throws IOException {
        resourceInvocationHandler.getHttpAdapter().shutDown();
    }


}
