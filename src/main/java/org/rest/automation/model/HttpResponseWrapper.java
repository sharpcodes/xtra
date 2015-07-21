package org.rest.automation.model;

/**
 * Created by sharpcodes on 4/7/15.
 */
public class HttpResponseWrapper {

    private int status;
    private String body;
    private String error;
    private boolean failed;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
