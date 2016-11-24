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

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    private static final String TAG = "HttpUrlConnectionClient";

    public ArrayList getListas(String urlServer, String s) {
        URL url;

        ArrayList lista = new ArrayList();

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
                    RestDataLibroDto restDataLiDto;
                    for (int i = 0; i < jsonResu.length(); i++) {
                        JSONObject jsonObject = jsonResu.getJSONObject(i);
                        restDataLiDto = Converter.toLibro(jsonObject);
                        lista.add(restDataLiDto);
                    }
                }
                else {
                    if(s == "Cat"){
                        RestDataCatDto restDataCatDto;
                        for(int i = 0; i < jsonResu.length(); i++) {
                            JSONObject jsonObject = jsonResu.getJSONObject(i);
                            restDataCatDto = Converter.toCategoria(jsonObject);
                            lista.add(restDataCatDto);
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
        return lista;
    }

    public String getLibro(String urlServer) {
        String libro = "";
        URL url;

        try{
            url = new URL(urlServer);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(GET);
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;

            while (null != (inputLine = in.readLine())){
                JSONObject jsonLibro = new JSONObject(inputLine);
                //libro = jsonLibro.toString();
                libro = inputLine;

            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return libro;
    }
    /*Ejemplo de llamada:
    JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id","15");
            jsonObject.put("nombre","Marc");
            jsonObject.put("email","marc@test.com");
            jsonObject.put("edad","47");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpUrlConnectionClient.sendPost("http://192.168.1.46:8080/v1/libro/",jsonObject.toString());*/
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

            //JSONObject jsonLibro = new JSONObject(this.getLibro(urlServer));

            URL url = new URL(urlServer);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
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
        }/* catch (JSONException e) {
            e.printStackTrace();
        }*/

        return result.toString();
    }

    public String sendDelete(String urlServer){

        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(urlServer);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("DELETE");
            result.append(httpURLConnection.getResponseCode());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result.toString();
    }
}
