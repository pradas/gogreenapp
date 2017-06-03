/*
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

package pes.gogreenapp.Utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Simple class to do the Asynchronous HTTP petitions to the API
 *
 * @author Albert
 */
public class AsyncHttpHandler {

    private static final String BASE_URL = "http://10.4.41.145/api/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * Perform a GET petition to the API
     *
     * @param url             url + BASE_URL are the uri HTTP petition
     * @param params          body params of the petition, it can be null in case of empty body
     * @param responseHandler AsyncHttpResponseHandler to create a thread
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        setToken();
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Perform a POST petition to the API
     *
     * @param url             url + BASE_URL are the uri HTTP petition
     * @param params          body params of the petition, it can be null in case of empty body
     * @param responseHandler AsyncHttpResponseHandler to create a thread
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        setToken();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Perform a PUT petition to the API
     *
     * @param url             url + BASE_URL are the uri HTTP petition
     * @param params          body params of the petition, it can be null in case of empty body
     * @param responseHandler AsyncHttpResponseHandler to create a thread
     */
    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        setToken();
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Join the BASE_URL and relativeUrl
     *
     * @param relativeUrl url relative to the BASE_URL
     *
     * @return a String resulting of the concatenation of BASE_URL and relativeUrl
     */
    private static String getAbsoluteUrl(String relativeUrl) {

        return BASE_URL + relativeUrl;
    }

    /**
     * Set the token authentication to the client
     */
    private static void setToken() {

        client.addHeader("Authorization", "Bearer {" + SessionManager.getInstance().getToken() + "}");
    }
}
