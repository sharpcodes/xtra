package org.rest.automation.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.model.HttpHeader;
import org.rest.automation.model.HttpResponseWrapper;

import java.io.IOException;
import java.util.List;


/**
 * The httpclient object from apache
 */

public class ApacheHttpClient {

    private static Logger LOGGER = LogManager.getLogger(ApacheHttpClient.class);

    CloseableHttpClient httpClient;


    public void initialize() {
        httpClient = HttpClients.createDefault();

    }

    /**
     * @param resource the url for the resource
     * @return {@link HttpResponseWrapper} containing the response body and status
     * @throws IOException
     */
    public HttpResponseWrapper processGetMethod(String resource) throws IOException {
        CloseableHttpResponse response1 = null;
        HttpResponseWrapper httpResponseWrapper = null;

        HttpGet httpGet = new HttpGet(resource);
        response1 = httpClient.execute(httpGet);
        httpResponseWrapper = new HttpResponseWrapper();
        httpResponseWrapper.setStatus(response1.getStatusLine().getStatusCode());
        HttpEntity entity1 = response1.getEntity();
        httpResponseWrapper.setBody(EntityUtils.toString(entity1));
        response1.close();

        return httpResponseWrapper;
    }

    /**
     * @param resource the url for the resource
     * @param headers  the headers that must be sent with the request
     * @return {@link HttpResponseWrapper} containing the response body and status
     * @throws IOException
     */
    public HttpResponseWrapper processGetMethod(String resource, List<HttpHeader> headers) throws IOException {

        CloseableHttpResponse response1 = null;
        HttpResponseWrapper httpResponseWrapper = null;
        try {
            HttpGet httpGet = new HttpGet(resource);
            for (HttpHeader header : headers) {
                httpGet.addHeader(header.getKey(), header.getValue());
            }
            response1 = httpClient.execute(httpGet);
            httpResponseWrapper = new HttpResponseWrapper();
            httpResponseWrapper.setStatus(response1.getStatusLine().getStatusCode());
            HttpEntity entity1 = response1.getEntity();
            httpResponseWrapper.setBody(EntityUtils.toString(entity1));


        } catch (IOException e) {
            LOGGER.error("error when processing url" + resource + " ---" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (response1 != null) {
                response1.close();
            }
        }
        return httpResponseWrapper;
    }

    /**
     * @param resource the url for the resource
     * @param headers  the headers that must be sent with the request
     * @param body     the request body
     * @return {@link HttpResponseWrapper} containing the response body and status
     * @throws IOException
     */
    public HttpResponseWrapper processPut(String resource, List<HttpHeader> headers, String body) throws IOException {

        CloseableHttpResponse response1 = null;
        HttpResponseWrapper httpResponseWrapper = null;

        HttpPut httpPut = new HttpPut(resource);
        httpPut.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        for (HttpHeader header : headers) {
            httpPut.addHeader(header.getKey(), header.getValue());
        }
        response1 = httpClient.execute(httpPut);
        httpResponseWrapper = new HttpResponseWrapper();
        httpResponseWrapper.setStatus(response1.getStatusLine().getStatusCode());
        HttpEntity entity1 = response1.getEntity();
        httpResponseWrapper.setBody(EntityUtils.toString(entity1));
        response1.close();


        return httpResponseWrapper;
    }

    /**
     * @param resource the url for the resource
     * @param headers  the headers that must be sent with the request
     * @param body     the request body
     * @return {@link HttpResponseWrapper} containing the response body and status
     * @throws IOException
     */
    public HttpResponseWrapper processPost(String resource, List<HttpHeader> headers, String body) throws IOException {

        CloseableHttpResponse response1 = null;
        HttpResponseWrapper httpResponseWrapper = null;

        HttpPost httpPost = new HttpPost(resource);
        httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        for (HttpHeader header : headers) {
            httpPost.addHeader(header.getKey(), header.getValue());
        }
        response1 = httpClient.execute(httpPost);
        httpResponseWrapper = new HttpResponseWrapper();
        httpResponseWrapper.setStatus(response1.getStatusLine().getStatusCode());
        HttpEntity entity1 = response1.getEntity();
        httpResponseWrapper.setBody(EntityUtils.toString(entity1));
        response1.close();
        return httpResponseWrapper;
    }

    /**
     * @param resource the url for the resource
     * @param headers  the headers that must be sent with the request
     * @return {@link HttpResponseWrapper} containing the response body and status
     * @throws IOException
     */
    public HttpResponseWrapper processDelete(String resource, List<HttpHeader> headers) throws IOException {

        CloseableHttpResponse response1 = null;
        HttpResponseWrapper httpResponseWrapper = null;

        HttpDelete httpDelete = new HttpDelete(resource);
        for (HttpHeader header : headers) {
            httpDelete.addHeader(header.getKey(), header.getValue());
        }
        response1 = httpClient.execute(httpDelete);
        httpResponseWrapper = new HttpResponseWrapper();
        httpResponseWrapper.setStatus(response1.getStatusLine().getStatusCode());
        HttpEntity entity1 = response1.getEntity();
        httpResponseWrapper.setBody(EntityUtils.toString(entity1));
        response1.close();

        return httpResponseWrapper;
    }

    public void close() throws IOException {
        httpClient.close();
    }

}
