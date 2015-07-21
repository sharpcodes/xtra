package org.rest.automation.jsonparser;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.rest.automation.exceptions.JsonPathException;

public class SimpleJsonParserTest {
    private static Logger LOGGER = LogManager.getLogger(SimpleJsonParserTest.class);

    @Test
    public void testHappyPath() throws Exception {
        SimpleJsonParser simpleJsonParser = new SimpleJsonParser();
        String testJson = "{\"name\":\"sai\"}";
        String result = simpleJsonParser.parseExpression("name", testJson);
        Assert.assertEquals("sai", result);
    }

    @Test(expected = JsonPathException.class)
    public void testNonExistingNode() throws Exception {
        SimpleJsonParser simpleJsonParser = new SimpleJsonParser();
        String testJson = "{\"name\":\"sai\"}";
        String result = simpleJsonParser.parseExpression("name.age", testJson);
        Assert.assertEquals(null, result);


    }

    @Test
    public void testNestedJson() throws Exception {
        SimpleJsonParser simpleJsonParser = new SimpleJsonParser();
        String testJson = "{\"name\":\"sai\",\"values\":[\"value1\",\"value2\",\"value3\"]}";
        String result = simpleJsonParser.parseExpression("values[1]", testJson);
        Assert.assertEquals("value2", result);
    }

    @Test(expected = JsonPathException.class)
    public void testInvalidParsingBehavior() throws Exception {
        SimpleJsonParser simpleJsonParser = new SimpleJsonParser();
        String testJson = "{\"name\":\"sai\",\"values\":[\"value1\",\"value2\",\"value3\"]}";
        String result = simpleJsonParser.parseExpression("junk[1]", testJson);
        Assert.assertEquals(null, result);
    }


    public void testArrayAccessBehavior() throws Exception {
        SimpleJsonParser simpleJsonParser = new SimpleJsonParser();
        String testJson = "{\"name\":\"sai\",\"values\":[\"value1\",\"value2\",\"value3\"]}";
        String result = simpleJsonParser.parseExpression("values[1]", testJson);
        Assert.assertEquals("value2", result);
    }

    @Test(expected = JsonPathException.class)
    public void testInvalidArrayAccessBehavior() throws Exception {
        SimpleJsonParser simpleJsonParser = new SimpleJsonParser();
        String testJson = "{\"name\":\"sai\",\"values\":[\"value1\",\"value2\",\"value3\"]}";
        String result = simpleJsonParser.parseExpression("values[n]", testJson);
        Assert.assertEquals("value2", result);
    }


}
