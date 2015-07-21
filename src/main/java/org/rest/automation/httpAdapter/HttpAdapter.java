package org.rest.automation.httpAdapter;

import org.rest.automation.model.HttpHeader;
import org.rest.automation.model.HttpResponseWrapper;

import java.io.IOException;
import java.util.List;


public interface HttpAdapter {

    HttpResponseWrapper processGetMethod(String resource) throws IOException;

    HttpResponseWrapper processGetMethod(String resource, List<HttpHeader> headers) throws IOException;

    HttpResponseWrapper processPut(String resource, List<HttpHeader> headers, String body) throws IOException;

    HttpResponseWrapper processPost(String resource, List<HttpHeader> headers, String body) throws IOException;

    HttpResponseWrapper processDelete(String resource, List<HttpHeader> headers) throws IOException;

    public void shutDown() throws IOException;
}
