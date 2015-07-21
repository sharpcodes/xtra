package org.rest.automation.httpAdapter;

import org.rest.automation.httpclient.ApacheHttpClient;
import org.rest.automation.model.HttpHeader;
import org.rest.automation.model.HttpResponseWrapper;

import java.io.IOException;
import java.util.List;

/**
 *
 * <p/>
 * The adapter class to the httpclient
 */
public class RestfulHttpAdapter implements HttpAdapter {

    private ApacheHttpClient apacheHttpClient;

    public ApacheHttpClient getApacheHttpClient() {
        return apacheHttpClient;
    }

    public void setApacheHttpClient(ApacheHttpClient apacheHttpClient) {
        this.apacheHttpClient = apacheHttpClient;
    }


    public HttpResponseWrapper processGetMethod(String resource) throws IOException {
        return apacheHttpClient.processGetMethod(resource);
    }


    public HttpResponseWrapper processGetMethod(String resource, List<HttpHeader> headers) throws IOException {
        return apacheHttpClient.processGetMethod(resource, headers);
    }


    public HttpResponseWrapper processPut(String resource, List<HttpHeader> headers, String body) throws IOException {
        return apacheHttpClient.processPut(resource, headers, body);
    }


    public HttpResponseWrapper processPost(String resource, List<HttpHeader> headers, String body) throws IOException {
        return apacheHttpClient.processPost(resource, headers, body);
    }


    public HttpResponseWrapper processDelete(String resource, List<HttpHeader> headers) throws IOException {
        return apacheHttpClient.processDelete(resource, headers);
    }

    public void shutDown() throws IOException {
        apacheHttpClient.close();

    }


}
