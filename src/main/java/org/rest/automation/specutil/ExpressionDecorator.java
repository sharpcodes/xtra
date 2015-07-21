package org.rest.automation.specutil;

import org.rest.automation.exceptions.InvalidExpressionException;
import org.rest.automation.exceptions.JsonPathException;
import org.rest.automation.jsonparser.ExpressionResolver;
import org.rest.automation.model.HttpHeader;
import org.rest.automation.model.HttpResponseWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sharpcodes on 5/6/15.
 */
public class ExpressionDecorator {

    private ExpressionResolver expressionResolver;

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    public String resolveExpressions(String expressionContainer, Map<String, HttpResponseWrapper> context) throws InvalidExpressionException,JsonPathException{
        return expressionResolver.parseExpression(expressionContainer, context);
    }

    public List<HttpHeader> resolveExpressions(List<HttpHeader> expressionContainer, Map<String, HttpResponseWrapper> context) throws InvalidExpressionException,JsonPathException{

        List<HttpHeader> headerValueCopyParsed = new ArrayList<HttpHeader>();
        for (HttpHeader header : expressionContainer) {
            HttpHeader headerValue = new HttpHeader();
            headerValue.setKey(header.getKey());
            headerValue.setValue(expressionResolver.parseExpression(header.getValue(), context));
            headerValueCopyParsed.add(headerValue);
        }

        return headerValueCopyParsed;
    }


}
