package org.rest.automation.model;

/**
 * Created by sharpcodes on 4/7/15.
 */
public class ValueAssertion {

    private final static String DEFAULT_ASSERTION_NAME = "assert equality";
    private String name = DEFAULT_ASSERTION_NAME;
    private boolean passed;
    private String message;
    private String expected;
    private String actual;


    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }
}
