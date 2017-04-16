package pes.gogreenapp.Handlers;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Albert on 27/03/2017.
 */

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    /**
     * Method to do a Http GET petition.
     *
     * @param reqUrl is the url of the service
     * @return the response of the service called in String format.
     */
    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            /* Read the response */
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }
    /**
     * Method to do a Http GET/POST/PUT/DELETE petition with params.
     *
     * @param reqUrl is the url of the service
     * @param method is the HTTP request method, supporting GET, POST, PUT, UPDATE and DELETE
     * @param param is a HashMap of the data to send to make the petition run
     * @return the response of the service called in String format.
     */
    public String makeServiceCall(String reqUrl, String method, HashMap<String,String> param) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // lee la response
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String params = hashMaptoString(param);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(params);
            writer.flush();
            writer.close();
            os.close();
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                return "Error "+conn.getResponseCode();
            }
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
            Log.d("err1","err1");
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    /**
     * Convert the InputStream into String
     *
     * @param is InputStream from a previous service call
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
     * Convert a hashMap into an url encoded String
     *
     * @param hmap hashMap from a previous service call
     * @return the hashMap transformed into an url encoded String format
     */
    @NonNull
    private String hashMaptoString(HashMap<String, String> hmap) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String,String> pair: hmap.entrySet()){
            if(first) first = false;
            else result.append("&");
            result.append(URLEncoder.encode(pair.getKey(),"UTF8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(),"UTF8"));
        }
        return result.toString();
    }
}
