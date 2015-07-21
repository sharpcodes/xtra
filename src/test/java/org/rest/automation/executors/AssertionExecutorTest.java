package org.rest.automation.executors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rest.automation.model.HttpResponseWrapper;
import org.rest.automation.model.TestSuite;
import org.rest.automation.model.TestTask;
import org.rest.automation.model.ValueAssertion;
import org.rest.automation.specutil.ConstantWrapper;

import java.util.HashMap;
import java.util.Map;


public class AssertionExecutorTest {

    @Before
    public void setUp() {

    }

    @Test
    public void checkNullAssertion() throws Exception{
        ValueAssertion valueAssertion = new ValueAssertion();
        Map<String, HttpResponseWrapper> context = null;
        AssertionExecutor assertionExecutor = new AssertionExecutor();
        assertionExecutor.executeAssertion(valueAssertion, context);
        /*The method exits without affecting the state of any object
         *
         */
        Assert.assertEquals(null, valueAssertion.getActual());
        Assert.assertEquals(null, valueAssertion.getExpected());
        Assert.assertEquals(null, valueAssertion.getMessage());

    }


    @Test
    public void checkStatusAssertionSkip() throws Exception{
        ValueAssertion valueAssertion = new ValueAssertion();
        valueAssertion.setName(ConstantWrapper.StringConstants.STATUS_CHECK.getValue());
        Map<String, HttpResponseWrapper> context = null;
        AssertionExecutor assertionExecutor = new AssertionExecutor();
        assertionExecutor.executeAssertion(valueAssertion, context);
        /*The method exits without affecting the state of any object
         *
         */
        Assert.assertEquals(null, valueAssertion.getActual());
        Assert.assertEquals(null, valueAssertion.getExpected());


    }

    @Test
    public void testStatusAssertion() {
        TestSuite testSuite = new TestSuite();
        Map<String, HttpResponseWrapper> context = new HashMap<String, HttpResponseWrapper>();
        TestTask testTask = new TestTask();
        testTask.setId("101");
        testTask.setStatusCheck(200);
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        httpResponseWrapper.setBody("hello");
        httpResponseWrapper.setStatus(200);
        context.put("101", httpResponseWrapper);
        testSuite.setTestContext(context);
        AssertionExecutor assertionExecutor = new AssertionExecutor();
        assertionExecutor.executeStatusAssertion(testTask, testSuite);
        Assert.assertEquals(testTask.getAssertions().get(0).isPassed(), true);

    }


    public void testValueAssertion() {


    }


}
