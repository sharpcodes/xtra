package org.rest.automation.executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.exceptions.InvalidExpressionException;
import org.rest.automation.exceptions.JsonPathException;
import org.rest.automation.jsonparser.ExpressionResolver;
import org.rest.automation.model.HttpResponseWrapper;
import org.rest.automation.model.TestSuite;
import org.rest.automation.model.TestTask;
import org.rest.automation.model.ValueAssertion;
import org.rest.automation.specutil.ConstantWrapper;

import java.util.ArrayList;
import java.util.Map;

/**
 * Resonsible for executing assertions specified in the {@link TestTask}
 */
public class AssertionExecutor {


    private static Logger LOGGER = LogManager.getLogger(AssertionExecutor.class);
    private ExpressionResolver expressionResolver;

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    /**
     * @param testTask containing the assertions
     * @param testSuite containing the testTask
     */
    public void executeStatusAssertion(TestTask testTask, TestSuite testSuite) {
        ValueAssertion valueAssertion = new ValueAssertion();
        valueAssertion.setName(ConstantWrapper.StringConstants.STATUS_CHECK.getValue());
        valueAssertion.setExpected(testTask.getStatusCheck().toString());
        HttpResponseWrapper httpResponseWrapper = testSuite.getTestContext().get(testTask.getId());
        if (httpResponseWrapper != null) {
            valueAssertion.setActual(String.valueOf(httpResponseWrapper.getStatus()));
            if (valueAssertion.getActual().equals(valueAssertion.getExpected())) {
                valueAssertion.setPassed(true);
            }
        } else {
            valueAssertion.setPassed(false);
            valueAssertion.setMessage("unable to get response from the server to perform assertion");
        }
        if (testTask.getAssertions() == null) {
            testTask.setAssertions(new ArrayList<ValueAssertion>());
        }
        testTask.addAssertion(valueAssertion);

    }

    /**
     * @param assertion  to be executed
     * @param context   for the current executing {@link TestSuite}
     */
    public void executeAssertion(ValueAssertion assertion, Map<String, HttpResponseWrapper> context) throws JsonPathException,InvalidExpressionException{
        if (!(assertion.getActual() == null || assertion.getExpected() == null) && !assertion.getName().equals(ConstantWrapper.StringConstants.STATUS_CHECK.getValue())) {
            String parsedValue = expressionResolver.parseExpression(assertion.getActual(), context);
            LOGGER.debug("parsed expression value --" + parsedValue);
            if (parsedValue != null && parsedValue.equalsIgnoreCase(assertion.getExpected())) {
                assertion.setPassed(true);
                assertion.setMessage("Passed");
                LOGGER.debug("Assertion passed");
            }
        }
    }
}
