package org.rest.automation.specutil;

/**
 * Created by sharpcodes on 6/16/15.
 *
 * Wrapper class for managing all string constants
 */
public class ConstantWrapper {

    public enum StringConstants{
        HTTP_GET("get"),HTTP_POST("post"),HTTP_PUT("put"),HTTP_DELETE("delete"),
        COMMA(","),COLON(":"),EMAIL_SUFFIX("@testmail.com"),EXPRESSION_IDENTIFIER("${"),
        PROPERTIES_PREFIX("${props:"),CONTEXT_PREFIX("${context:"),FUNCTION_PREFIX("${func:"),STATUS_CHECK("http status code");

        private String value;
        public String getValue(){
            return value;
        }
        StringConstants(String value){
            this.value=value;
        }

    }


}


