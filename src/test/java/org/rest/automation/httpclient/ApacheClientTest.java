package org.rest.automation.httpclient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.rest.automation.model.HttpHeader;
import org.rest.automation.model.HttpResponseWrapper;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class ApacheClientTest {

    private static Logger LOGGER = LogManager.getLogger(ApacheClientTest.class);


    @Test
    public void testHttpClientGetCall() {

        WireMockServer wireMockServer = null;
        try {
            ServerSocket socket = new ServerSocket(0);
            int result = socket.getLocalPort();
            socket.close();
            wireMockServer = new WireMockServer(wireMockConfig().port(result));

            wireMockServer.start();
            WireMock.configureFor("localhost", result);
            stubFor(get(urlEqualTo("/my/resource"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{\"name\":\"john\"}")));

            ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
            apacheHttpClient.initialize();
            HttpResponseWrapper responseWrapper = apacheHttpClient.processGetMethod("http://localhost:" + result + "/my/resource");
            Assert.assertEquals(responseWrapper.getStatus(), 200);
            Assert.assertEquals("{\"name\":\"john\"}", responseWrapper.getBody());


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            wireMockServer.stop();

        }


    }


    @Test
    public void testHttpClientPostCall() {

        WireMockServer wireMockServer = null;
        try {
            ServerSocket socket = new ServerSocket(0);
            int result = socket.getLocalPort();
            socket.close();
            wireMockServer = new WireMockServer(wireMockConfig().port(result));

            wireMockServer.start();
            WireMock.configureFor("localhost", result);
            stubFor(post(urlEqualTo("/my/resource")).withHeader("Content-Type", equalTo("application/json"))
                    .withRequestBody(equalTo("{\"name\":\"sai\"}"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{\"name\":\"sai\"}")));

            ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
            apacheHttpClient.initialize();
            HttpHeader httpHeader = new HttpHeader();
            httpHeader.setKey("Content-Type");
            httpHeader.setValue("application/json");
            List<HttpHeader> httpHeaders = new ArrayList<HttpHeader>();
            httpHeaders.add(httpHeader);
            HttpResponseWrapper responseWrapper = apacheHttpClient.processPost("http://localhost:" + result + "/my/resource", httpHeaders, "{\"name\":\"sai\"}");
            Assert.assertEquals(responseWrapper.getStatus(), 200);
            Assert.assertEquals("{\"name\":\"sai\"}", responseWrapper.getBody());


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            wireMockServer.stop();

        }


    }


    @Test
    public void testHttpClientDeleteCall() {

        WireMockServer wireMockServer = null;
        try {
            ServerSocket socket = new ServerSocket(0);
            int result = socket.getLocalPort();
            socket.close();
            wireMockServer = new WireMockServer(wireMockConfig().port(result));

            wireMockServer.start();
            WireMock.configureFor("localhost", result);
            stubFor(delete(urlEqualTo("/my/resource")).withHeader("Content-Type", equalTo("application/json"))
                    .willReturn(aResponse()
                            .withStatus(201)
                            .withHeader("Content-Type", "application/json")
                            .withBody("status:deleted")));

            ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
            apacheHttpClient.initialize();
            HttpHeader httpHeader = new HttpHeader();
            httpHeader.setKey("Content-Type");
            httpHeader.setValue("application/json");
            List<HttpHeader> httpHeaders = new ArrayList<HttpHeader>();
            httpHeaders.add(httpHeader);
            HttpResponseWrapper responseWrapper = apacheHttpClient.processDelete("http://localhost:" + result + "/my/resource", httpHeaders);
            Assert.assertEquals(responseWrapper.getStatus(), 201);
            Assert.assertEquals("status:deleted", responseWrapper.getBody());


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            wireMockServer.stop();

        }


    }


    @Test
    public void testHttpClientPutCall() {

        WireMockServer wireMockServer = null;
        try {
            ServerSocket socket = new ServerSocket(0);
            int result = socket.getLocalPort();
            socket.close();
            wireMockServer = new WireMockServer(wireMockConfig().port(result));

            wireMockServer.start();
            WireMock.configureFor("localhost", result);
            stubFor(put(urlEqualTo("/my/resource")).withHeader("Content-Type", equalTo("application/json"))
                    .withRequestBody(equalTo("{\"name\":\"sai\"}"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody("{\"name\":\"sai\"}")));

            ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
            apacheHttpClient.initialize();
            HttpHeader httpHeader = new HttpHeader();
            httpHeader.setKey("Content-Type");
            httpHeader.setValue("application/json");
            List<HttpHeader> httpHeaders = new ArrayList<HttpHeader>();
            httpHeaders.add(httpHeader);
            HttpResponseWrapper responseWrapper = apacheHttpClient.processPut("http://localhost:" + result + "/my/resource", httpHeaders, "{\"name\":\"sai\"}");
            Assert.assertEquals(responseWrapper.getStatus(), 200);
            Assert.assertEquals("{\"name\":\"sai\"}", responseWrapper.getBody());


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            wireMockServer.stop();

        }


    }


}
