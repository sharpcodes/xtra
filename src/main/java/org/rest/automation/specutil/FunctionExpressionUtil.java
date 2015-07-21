package org.rest.automation.specutil;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.exceptions.InvalidExpressionException;

import java.math.BigDecimal;

/**
 * Created by sharpcodes on 5/29/15.
 */
public class FunctionExpressionUtil {

    private static final String EMAIL_SUFFIX="@testEmail.com";
    private static Logger LOGGER = LogManager.getLogger(FunctionExpressionUtil.class);


    public enum FunctionExpression{
        RANDOMINT("randomInt"),RANDOMSTRING("randomString"),
        RANDOMDECIMAL("randomDecimal"),RANDOMEMAIL("randomEmail");
        private String name;
        FunctionExpression(String name){
        this.name=name;
        }
    }

    public static FunctionExpression getFunctionByName(String name){
        for(FunctionExpression functionExpression:FunctionExpression.values()){

            if(functionExpression.name.equals(name))
                return functionExpression;
        }
        return null;
    }


    public static String getFunctionValue(String expression) throws InvalidExpressionException{
        LOGGER.debug("received function expression "+expression);
        FunctionExpression functionExpression=getFunctionByName(expression);
        if(functionExpression!=null && functionExpression.equals(FunctionExpression.RANDOMINT)){
             Long longValue=(long)(Math.random()*Integer.MAX_VALUE);
            return String.valueOf(longValue.intValue());
        }
        if(functionExpression!=null && functionExpression.equals(FunctionExpression.RANDOMEMAIL)){
            return RandomStringUtils.randomAlphabetic(10)+EMAIL_SUFFIX;
        }
        if(functionExpression!=null && functionExpression.equals(FunctionExpression.RANDOMSTRING)){
            return RandomStringUtils.randomAlphabetic(10);
        }
        if(functionExpression!=null && functionExpression.equals(FunctionExpression.RANDOMDECIMAL)){
            return new BigDecimal(Math.random()).setScale(2).toString();
        }
     throw new InvalidExpressionException("function not recognized -->"+expression);
    }

}
