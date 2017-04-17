package pes.gogreenapp.Handlers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
     * @param reqUrl is the url of the service
     * @return the response of the service called in String format.
     */
    public String makeServiceCall(String reqUrl, String method, HashMap<String, String> bodyParameters) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer {eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6XC9cLzEwLjQuNDEuMTQ1XC9hcGlcL3Nlc3Npb25cL25ldyIsImlhdCI6MTQ5MjQyMzIyNCwiZXhwIjoxNDkyNDI2ODI0LCJuYmYiOjE0OTI0MjMyMjQsImp0aSI6ImNRdVhEMzlmYXdKZ0huclkifQ.i3MBCG9Vgf_ShHUAulw5XI27Ty1VnW6KemOKNlYMxr0}");

            if ("POST".equals(method)) {
                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                writeStream(out, bodyParameters);
            }

            System.out.println(conn.getResponseCode());

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
}
