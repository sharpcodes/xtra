package org.rest.automation.model;

/**
 * Created by sharpcodes on 5/12/15.
 */
public class ProcessStatus {

    private boolean passed;
    private String message;

    public ProcessStatus(boolean passed, String message) {
        this.passed = passed;
        this.message = message;
    }

    public static ProcessStatus createInstance(boolean status, String message) {
        return new ProcessStatus(status, message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
