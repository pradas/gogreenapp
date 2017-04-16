package pes.gogreenapp;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import pes.gogreenapp.Handlers.HttpHandler;

import static org.junit.Assert.*;

public class HttpHandlerTest {
    /**
     * Test for check if the petitions Http Get are operative or not.
     *
     * @throws Exception in case of fail in the assert
     */
    @Test
    public void httpGet() throws Exception {
        HttpHandler httpHandler = new HttpHandler();
        assertNotNull(httpHandler.makeServiceCall(
                "http://10.4.41.145/api/rewards",
                "GET",
                new HashMap<>()));
    }

    @Test
    public void httpPost() throws Exception {
        HttpHandler httpHandler = new HttpHandler();
        HashMap<String, String> bodyParameters = new HashMap<>();
        bodyParameters.put("user", "admin");
        bodyParameters.put("password", "Password12");
        assertNotNull(httpHandler.makeServiceCall(
                "http://10.4.41.145/api/session/new",
                "POST",
                bodyParameters));
    }
    @Test
    public void httpPut() {
        // TODO test
    }

    @Test
    public void httpDelete () {
        // TODO test
    }
}
