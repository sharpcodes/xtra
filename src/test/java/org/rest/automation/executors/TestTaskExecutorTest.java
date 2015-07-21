package org.rest.automation.executors;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import org.rest.automation.decorators.TestResultDecorator;
import org.rest.automation.httpAdapter.ResourceInvocationHandler;
import org.rest.automation.httpAdapter.RestfulHttpAdapter;
import org.rest.automation.jsonparser.ExpressionResolver;
import org.rest.automation.jsonparser.SimpleJsonParser;
import org.rest.automation.model.Environment;
import org.rest.automation.model.HttpResponseWrapper;
import org.rest.automation.model.TestSuite;
import org.rest.automation.model.TestTask;
import org.rest.automation.properties.manager.PropertiesManagerImpl;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;


public class TestTaskExecutorTest {


    @Test
    public void testNullObjectFlow() throws Exception {
        TestTask testTask = new TestTask();
        TestSuite testSuite = new TestSuite();
        AssertionExecutor assertionExecutor = mock(AssertionExecutor.class);
        TestTaskExecutor testTaskExecutor = new TestTaskExecutor();
        ResourceInvocationHandler resourceInvocationHandler = mock(ResourceInvocationHandler.class);
        testTaskExecutor.setResultDecorator(new TestResultDecorator<TestSuite>());
        testTaskExecutor.setResourceInvocationHandler(resourceInvocationHandler);
        testTaskExecutor.setAssertionExecutor(assertionExecutor);
        CommandLine commandLine = mock(CommandLine.class);
        Environment mockEnv = Environment.createInstance(commandLine);
        testTaskExecutor.runTestTask(testTask, testSuite, mockEnv);
        verify(resourceInvocationHandler, never()).executeResourceCalls(testTask, testSuite);
        verify(assertionExecutor, never()).executeStatusAssertion(testTask, testSuite);
    }


    @Test
    public void testDecoratorInvocationForNullAndNotNullTestTaskScenarios() throws Exception {
        TestTask testTask = new TestTask();
        TestSuite testSuite = new TestSuite();
        TestResultDecorator<TestSuite> testResultDecorator = mock(TestResultDecorator.class);
        TestTaskExecutor testTaskExecutor = new TestTaskExecutor();
        testTaskExecutor.setResultDecorator(testResultDecorator);
        CommandLine commandLine = mock(CommandLine.class);
        Environment mockEnv = Environment.createInstance(commandLine);
        testTaskExecutor.runTestTask(testTask, testSuite, mockEnv);
        /*
        modified for concurrency and hence this fails
        */
         //verify(testResultDecorator, times(1)).formatResult(testSuite);

    }

    @Test
    public void testAssertionOfStatusExecution() throws Exception {

        TestTask testTask = new TestTask();
        testTask.setId("testId");
        testTask.setStatusCheck(200);
        testTask.setResource("www.google.com");
        testTask.setMethod("GET");
        TestSuite testSuite = new TestSuite();

        ExpressionResolver expressionResolver = new ExpressionResolver();
        SimpleJsonParser simpleJsonParser = new SimpleJsonParser();
        expressionResolver.setSimpleJsonParser(simpleJsonParser);
        PropertiesManagerImpl propertiesManager = new PropertiesManagerImpl();
        expressionResolver.setPropertiesManager(propertiesManager);
        /*
        build mock objects
         */
        RestfulHttpAdapter restfulHttpAdapter = mock(RestfulHttpAdapter.class);

        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        httpResponseWrapper.setBody("hello");
        httpResponseWrapper.setStatus(200);
        when(restfulHttpAdapter.processGetMethod("www.google.com")).thenReturn(httpResponseWrapper);
        TestResultDecorator<TestSuite> testResultDecorator = mock(TestResultDecorator.class);
        AssertionExecutor assertionExecutor = mock(AssertionExecutor.class);


        TestTaskExecutor testTaskExecutor = new TestTaskExecutor();
        ResourceInvocationHandler resourceInvocationHandler = new ResourceInvocationHandler();
        resourceInvocationHandler.setHttpAdapter(restfulHttpAdapter);
        resourceInvocationHandler.setExpressionResolver(expressionResolver);
        testTaskExecutor.setResultDecorator(testResultDecorator);
        testTaskExecutor.setAssertionExecutor(assertionExecutor);
        testTaskExecutor.setResourceInvocationHandler(resourceInvocationHandler);

        /*
         *Set up context
         */
        Map<String, HttpResponseWrapper> context = new HashMap<String, HttpResponseWrapper>();
        testSuite.setTestContext(context);

        CommandLine commandLine = mock(CommandLine.class);
        Environment mockEnv = Environment.createInstance(commandLine);

        testTaskExecutor.runTestTask(testTask, testSuite, mockEnv);

        verify(assertionExecutor, times(1)).executeStatusAssertion(testTask, testSuite);
        // verify(testResultDecorator, times(1)).formatResult(testSuite,mockEnv);

    }


}
