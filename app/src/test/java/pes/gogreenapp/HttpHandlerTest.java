package pes.gogreenapp;

import org.junit.Test;

import pes.gogreenapp.Handlers.HttpHandler;

import static org.junit.Assert.*;

/**
 * Created by Albert on 28/03/2017.
 */

public class HttpHandlerTest {
    /**
     * Test for check if the petitions Http Get are operative or not.
     *
     * @throws Exception in case of fail in the assert
     */
    @Test
    public void httpGet() throws Exception {
        HttpHandler httpHandler = new HttpHandler();
        assertNotNull(httpHandler.makeServiceCall("http://10.4.41.145/api/rewards"));
    }
}
