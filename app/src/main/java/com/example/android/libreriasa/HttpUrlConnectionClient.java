package com.example.android.libreriasa;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Cotroc on 11/8/16.
 */

public class HttpUrlConnectionClient {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final String TAG = "HttpUrlConnectionClient";

    public ArrayList getBookList(String urlServer, String s) {
        URL url;

        ArrayList bookList = new ArrayList();

        try {
            url = new URL(urlServer);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(GET);
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;

            while (null != (inputLine = in.readLine())) {
                Log.i(TAG, "Llegando " + inputLine);
                JSONArray jsonResu = new JSONArray(inputLine);

                if(s == "Libros") {
                    BookDto bookDto;
                    for (int i = 0; i < jsonResu.length(); i++) {
                        JSONObject jsonObject = jsonResu.getJSONObject(i);
                        bookDto = Converter.toLibro(jsonObject);
                        bookList.add(bookDto);
                    }
                }
                else {
                    if(s == "Cat"){
                        CatDto catDto;
                        for(int i = 0; i < jsonResu.length(); i++) {
                            JSONObject jsonObject = jsonResu.getJSONObject(i);
                            catDto = Converter.toCategoria(jsonObject);
                            bookList.add(catDto);
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public String getBook(String urlServer) {
        String book = "";
        URL url;

        try{
            url = new URL(urlServer);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(GET);
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;

            while (null != (inputLine = in.readLine())){
                book = inputLine;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return book;
    }

    public String sendPost(String urlServer, String args) {
        StringBuffer result = new StringBuffer();
        String inputLine;

        try {
            URL url = new URL(urlServer);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(POST);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("charset", "utf-8");
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write("libro=" + args);
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            while (null!=(inputLine = in.readLine())) {
                result.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String sendUpdate(String urlServer, String args) {
        StringBuffer result = new StringBuffer();
        String inputLine;

        try{
            URL url = new URL(urlServer);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(PUT);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("charset", "utf-8");
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write("libro=" +args);
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            while (null!=(inputLine = in.readLine())) {
                result.append(inputLine);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String sendDelete(String urlServer){
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(urlServer);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod(DELETE);
            result.append(httpURLConnection.getResponseCode());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result.toString();
    }
}
