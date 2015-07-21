package org.rest.automation.jsonparser;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rest.automation.exceptions.JsonPathException;
import org.rest.automation.model.HttpResponseWrapper;
import org.rest.automation.properties.manager.PropertiesManagerImpl;

import java.util.HashMap;
import java.util.Map;


public class ExpressionResolverTest {

    static ExpressionResolver expressionResolver;
    static Map<String, HttpResponseWrapper> context;


    @BeforeClass
    public static void setUp() {

        expressionResolver = new ExpressionResolver();
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper();
        httpResponseWrapper.setStatus(200);
        httpResponseWrapper.setBody("{\"name\":\"john\",\"country\":\"usa\"}");
        Map<String, String> propertiesMap = new HashMap<String, String>();
        propertiesMap.put("googleHome", "http://www.google.com");
        propertiesMap.put("keyword", "hello");
        PropertiesManagerImpl propertiesManager = new PropertiesManagerImpl();
        propertiesManager.setConfiguration(propertiesMap);
        context = new HashMap<String, HttpResponseWrapper>();
        context.put("testId", httpResponseWrapper);
        expressionResolver.setPropertiesManager(propertiesManager);
        SimpleJsonParser simpleJsonParser = new SimpleJsonParser();
        expressionResolver.setSimpleJsonParser(simpleJsonParser);

    }

    @Test
    public void checkNotAnExpression() throws Exception{

      /*
         *Return value as it is when expression is not mean to be parsed
         */
        Assert.assertEquals("expressionString", expressionResolver.parseExpression("expressionString", context));

    }


    @Test
    public void checkNoMatchForPropertiesExpression() throws Exception{

        /*
         *Return the value as it is if no match for the expression can be found from properties
         */
        Assert.assertEquals("${props:junk}", expressionResolver.parseExpression("${props:junk}", context));

    }

    @Test(expected = JsonPathException.class)
    public void checkNoMatchForContextExpression() throws Exception{

        /*
         *Return the value as it is if no match for the context can be found
         */
        Assert.assertEquals("${context:testId.junk}", expressionResolver.parseExpression("${context:testId.junk}", context));

    }

    @Test
    public void checkInvalidContextIdExpression() throws Exception{

        /*
         *Return the value as it is if invalid Ids for the context were passed
         */
        Assert.assertEquals("${context:testIds.junk}", expressionResolver.parseExpression("${context:testIds.junk}", context));

    }


    @Test
    public void checkForPropertiesMatchForAnExpression() throws Exception{

        /*
         *Return value from properties when it matches
         */
        Assert.assertEquals("http://www.google.com", expressionResolver.parseExpression("${props:googleHome}", context));

    }


    @Test
    public void checkForContextMatchForAnExpression() throws Exception{

        /*
         *Return value as it is when expression is not mean to be parsed
         */
        Assert.assertEquals("john", expressionResolver.parseExpression("${context:testId.name}", context));

    }

    @Test
    public void checkForReplacementWithContextForAnExpression() throws Exception{

        /*
         *Return value as it is when expression is not mean to be parsed
         */
        Assert.assertEquals("my name is john", expressionResolver.parseExpression("my name is ${context:testId.name}", context));

    }

    @Test
    public void checkForReplacementWithValidAndInvalidExpressionCombined() throws Exception{

        /*
         *Return value as it is when expression is not mean to be parsed
         */
        Assert.assertEquals("my ${name} is john", expressionResolver.parseExpression("my ${name} is ${context:testId.name}", context));

    }

    @Test
    public void checkForSuccessiveReplacement() throws Exception{

        /*
         *Return value as it is when expression is not mean to be parsed
         */
        Assert.assertEquals("http://www.google.com?search=hello", expressionResolver.parseExpression("${props:googleHome}?search=${props:keyword}", context));

    }


}
