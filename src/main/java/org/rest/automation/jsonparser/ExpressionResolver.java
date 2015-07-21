package org.rest.automation.jsonparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.exceptions.InvalidExpressionException;
import org.rest.automation.exceptions.JsonPathException;
import org.rest.automation.model.HttpHeader;
import org.rest.automation.model.HttpResponseWrapper;
import org.rest.automation.properties.manager.PropertiesManager;
import org.rest.automation.properties.manager.PropertiesManagerImpl;
import org.rest.automation.specutil.ConstantWrapper;
import org.rest.automation.specutil.FunctionExpressionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The bean that handles all expression parsing.
 *
 */
public class ExpressionResolver {

    private static Logger LOGGER = LogManager.getLogger(ExpressionResolver.class);
   /*
   required to parse expression that needs a propery look up
    */
    private PropertiesManagerImpl propertiesManager;
    /*
    Required to parse json paths
     */
    private SimpleJsonParser simpleJsonParser;

    public SimpleJsonParser getSimpleJsonParser() {
        return simpleJsonParser;
    }

    public void setSimpleJsonParser(SimpleJsonParser simpleJsonParser) {
        this.simpleJsonParser = simpleJsonParser;
    }

    public PropertiesManager getPropertiesManager() {
        return propertiesManager;
    }

    public void setPropertiesManager(PropertiesManagerImpl propertiesManager) {
        this.propertiesManager = propertiesManager;
    }


    /**
     * @param expression
     * @param context
     * @return
     */
    public String parseExpression(String expression, Map<String, HttpResponseWrapper> context) throws JsonPathException, InvalidExpressionException {
        if (expression.indexOf(ConstantWrapper.StringConstants.EXPRESSION_IDENTIFIER.getValue()) < 0) {
            return expression;
        } else {
            String output = expression;
            Pattern p = Pattern.compile("\\$\\{[^\\}]+\\}");
            Matcher matcher = p.matcher(expression);
            String replacement;
            while (matcher.find()) {
                replacement = null;
                String match = matcher.group();
                if (match.startsWith(ConstantWrapper.StringConstants.PROPERTIES_PREFIX.getValue())) {
                    if(propertiesManager.isNotLoaded()){
                        throw new InvalidExpressionException("Expression-->"+expression +" cannot be resolved as no properties were specified");
                    }
                    String[] tokens = match.split("\\:");
                    if (tokens.length != 2) {
                        return null;
                    }
                    replacement = propertiesManager.getResource(tokens[1].substring(0, tokens[1].length() - 1));
                    LOGGER.info("retrieved property with key=" + expression + " and value=" + replacement);
                }
                if (match.startsWith(ConstantWrapper.StringConstants.CONTEXT_PREFIX.getValue())) {
                    String[] tokens = match.split("\\:");
                    if (tokens.length != 2) {
                        return null;
                    }
                    String[] contextPath = tokens[1].split("\\.");
                    HttpResponseWrapper responseWrapper = context.get(contextPath[0]);
                    if (responseWrapper != null) {
                        replacement = simpleJsonParser.parseExpression(match.substring(match.indexOf(contextPath[1]), match.length() - 1), responseWrapper.getBody());
                        LOGGER.info("parsed json with key=" + expression + " and value=" + replacement);
                    }

                }
                if (match.startsWith(ConstantWrapper.StringConstants.FUNCTION_PREFIX.getValue())) {
                   replacement=resolveFunction(match);
                }
                if (replacement != null) {
                    output = output.replace(matcher.group(), replacement);
                }

            }
            return output;

        }

    }

    /**
     * @param expressionContainer
     * @param context
     * @return
     */
    public List<HttpHeader> resolveExpressions(List<HttpHeader> expressionContainer, Map<String, HttpResponseWrapper> context) throws JsonPathException, InvalidExpressionException {

        List<HttpHeader> headerValueCopyParsed = new ArrayList<HttpHeader>();
        for (HttpHeader header : expressionContainer) {
            HttpHeader headerValue = new HttpHeader();
            headerValue.setKey(header.getKey());
            headerValue.setValue(parseExpression(header.getValue(), context));
            headerValueCopyParsed.add(headerValue);
        }

        return headerValueCopyParsed;
    }


    /**
     *
     * @param expression
     * @return
     * @throws InvalidExpressionException
     */
    public String resolveFunction(String expression) throws InvalidExpressionException{

        String[] tokens = expression.split("\\:");
        if (tokens.length != 2) {
            return null;
        }

        String result = FunctionExpressionUtil.getFunctionValue(tokens[1].substring(0, tokens[1].length() - 1));
        LOGGER.info("parsed function expression with key=" + expression + " and value=" + result);

        return result;

    }

}
