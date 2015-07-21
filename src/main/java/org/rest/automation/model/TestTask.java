package org.rest.automation.model;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by sharpcodes on 4/6/15.
 */

@XmlRootElement(name = "testTask")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestTask {

    private String resource;
    private String method;
    @XmlAttribute
    private String id;
    private Integer statusCheck;
    @XmlElement(name = "assertion")
    private List<ValueAssertion> assertions;
    @XmlElement(name = "header")
    private List<HttpHeader> headers;
    @XmlElement(name = "body")
    private String requestBody;


    private String error;

    private boolean passed = true;


    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HttpHeader> headers) {
        this.headers = headers;
    }

    public List<ValueAssertion> getAssertions() {
        return assertions;
    }

    public void setAssertions(List<ValueAssertion> assertions) {
        this.assertions = assertions;
    }

    public String getResource() {
        return resource;
    }


    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Integer getStatusCheck() {
        return statusCheck;
    }

    public void setStatusCheck(Integer statusCheck) {
        this.statusCheck = statusCheck;
    }

    public void addAssertion(ValueAssertion valueAssertion) {
        this.assertions.add(valueAssertion);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
