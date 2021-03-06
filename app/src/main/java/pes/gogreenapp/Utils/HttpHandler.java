package pes.gogreenapp.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {

    }

    /**
     * Method to do a Http GET petition.
     *
     * @param reqUrl         is the url of the service
     * @param method
     * @param bodyParameters
     * @param token
     *
     * @return the response of the service called in String format.
     */
    public String makeServiceCall(String reqUrl, String method, HashMap<String, String> bodyParameters, String token) {

        String response;
        String resCode = "300";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer {" + token + "}");

            if ("POST".equals(method) || "PUT".equals(method)) {
                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                writeStream(out, bodyParameters);
            }
            resCode = String.valueOf(conn.getResponseCode());
            Log.i(TAG, resCode);

            /* Read the response */
            if (resCode.equals("200")) {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
                if (response.isEmpty()) response = "200";
            } else {
                response = resCode;
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
            return resCode;
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
            return resCode;
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            return resCode;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return resCode;
        }
        return response;
    }

    /**
     * Convert the InputStream into String
     *
     * @param is InputStream from a previous service call
     *
     * @return the InputStream transformed into a String format
     */
    @NonNull
    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Write the Stream send it to the Web Server for POST and PUT request.
     *
     * @param out            Output Stream that will be sended to the Web Server.
     * @param bodyParameters parameters of the body request.
     */
    private void writeStream(OutputStream out, HashMap<String, String> bodyParameters) {

        String output;
        try {
            output = new ObjectMapper().writeValueAsString(bodyParameters);
            out.write(output.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static final String BASE_URL = "http://10.4.41.145/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {

        return BASE_URL + relativeUrl;
    }

}
