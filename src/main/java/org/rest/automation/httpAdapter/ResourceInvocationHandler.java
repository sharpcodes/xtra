package org.rest.automation.httpAdapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.exceptions.InvalidExpressionException;
import org.rest.automation.exceptions.JsonPathException;
import org.rest.automation.jsonparser.ExpressionResolver;
import org.rest.automation.model.HttpResponseWrapper;
import org.rest.automation.model.TestSuite;
import org.rest.automation.model.TestTask;
import org.rest.automation.specutil.ConstantWrapper;

import java.io.IOException;
import java.util.Map;


public class ResourceInvocationHandler {

    
    private static Logger LOGGER = LogManager.getLogger(ResourceInvocationHandler.class);

    private RestfulHttpAdapter httpAdapter;

    public RestfulHttpAdapter getHttpAdapter() {
        return httpAdapter;
    }

    public void setHttpAdapter(RestfulHttpAdapter httpAdapter) {
        this.httpAdapter = httpAdapter;
    }


    private ExpressionResolver expressionResolver;

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }



    /**
     * Method calling the http-resource
     *
     * @param testTask
     * @param testSuite
     */
    public void executeResourceCalls(TestTask testTask, TestSuite testSuite) throws IOException, JsonPathException, InvalidExpressionException {
        String httpMethod = testTask.getMethod();
        HttpResponseWrapper httpResponseWrapper;
        /*
        Resolve URL and repalce with the resolved value
         */
        Map<String, HttpResponseWrapper> context = testSuite.getTestContext();
        String resolvedValue=expressionResolver.parseExpression(testTask.getResource(), testSuite.getTestContext());
        testTask.setResource(resolvedValue);

        /*
        Resolve requestbody and replace with the resolved value
         */
        if( testTask.getRequestBody()!=null){
            testTask.setRequestBody(expressionResolver.parseExpression(testTask.getRequestBody(), testSuite.getTestContext()));
        }



        if (httpMethod.equalsIgnoreCase(ConstantWrapper.StringConstants.HTTP_GET.getValue())) {
            LOGGER.info("executing get call --" + testTask.getResource());
            if (testTask.getHeaders() != null) {
                httpResponseWrapper = httpAdapter.processGetMethod(resolvedValue, expressionResolver.resolveExpressions(testTask.getHeaders(), testSuite.getTestContext()));
            } else {
                httpResponseWrapper = httpAdapter.processGetMethod(resolvedValue);
            }
            context.put(testTask.getId(), httpResponseWrapper);
        }

        if (httpMethod.equalsIgnoreCase(ConstantWrapper.StringConstants.HTTP_PUT.getValue())) {

            LOGGER.info("executing get call --" + testTask.getResource());
            httpResponseWrapper = httpAdapter.processPut(resolvedValue, expressionResolver.resolveExpressions(testTask.getHeaders(), testSuite.getTestContext()), expressionResolver.parseExpression(testTask.getRequestBody(), testSuite.getTestContext()));
            context.put(testTask.getId(), httpResponseWrapper);

        }
        if (httpMethod.equalsIgnoreCase(ConstantWrapper.StringConstants.HTTP_POST.getValue())) {

            LOGGER.info("executing get call --" + testTask.getResource());
            httpResponseWrapper = httpAdapter.processPost(resolvedValue, expressionResolver.resolveExpressions(testTask.getHeaders(), testSuite.getTestContext()), expressionResolver.parseExpression(testTask.getRequestBody(), testSuite.getTestContext()));
            context.put(testTask.getId(), httpResponseWrapper);


        }
        if (httpMethod.equalsIgnoreCase(ConstantWrapper.StringConstants.HTTP_DELETE.getValue())) {

            LOGGER.info("executing get call --" + testTask.getResource());
            httpResponseWrapper = httpAdapter.processDelete(resolvedValue, expressionResolver.resolveExpressions(testTask.getHeaders(), testSuite.getTestContext()));
            context.put(testTask.getId(), httpResponseWrapper);

        }


    }

}
