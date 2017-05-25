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
 * @author Albert
 */
public class AsyncHttpHandler {

    private static final String BASE_URL = "http://10.4.41.145/api/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        setToken();
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        setToken();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        setToken();
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {

        return BASE_URL + relativeUrl;
    }

    private static void setToken() {

        client.addHeader("Authorization", "Bearer {" + SessionManager.getInstance().getToken() + "}");
    }
}
