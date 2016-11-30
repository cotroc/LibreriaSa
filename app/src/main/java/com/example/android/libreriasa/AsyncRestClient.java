package com.example.android.libreriasa;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Cotroc on 11/8/16.
 */

public class AsyncRestClient extends AsyncTask<Bundle, String, Bundle> {

    private SimpleUpdatableActivity simpleUpdatableActivity;
    private static final String TAG = "AsyncRestClient";

    public AsyncRestClient(SimpleUpdatableActivity simpleUpdatableActivity) {
        this.simpleUpdatableActivity = simpleUpdatableActivity;
    }

    /*

     */
    @Override
    protected Bundle doInBackground(Bundle... params) {

        Bundle output = new Bundle();
        String flag = params[0].getString("flag0");
        String list = params[0].getString("flag1");
        HttpUrlConnectionClient httpUrlConnectionClient;

        /*
        Este chequeo me evita tener que llamar
        el metodo publishProgress(flag) en
        cada case del Switch
         */
        if(list == null || ( list.matches("Libros") )) {
            publishProgress(flag);
        }

        switch (flag) {
            case "Listar":

                /*
                if(listServer.matches("Libros")){
                publishProgress(flag);
                }
                */
                Log.i(TAG, "Iniciando llamada al servidor para: " + flag + list);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                output.putString("flag", "lista" + list);
                output.putParcelableArrayList("lista" + list, httpUrlConnectionClient.getBookList(params[0].getString("url"), list));

                break;
            case "Nuevo":
                //publishProgress(flag);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                output.putString("flag", "insertado");
                output.putString("resultado", httpUrlConnectionClient.sendPost(params[0].getString("url"), params[0].getString("book")));
                break;

            case "Buscar":
                //publishProgress(flag);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                output.putString("flag", "buscar");
                output.putString("book", httpUrlConnectionClient.getBook(params[0].getString("url")));
                break;

            case "Editar":
                //publishProgress(flag);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                output.putString("flag", "insertado");
                output.putString("resultado", httpUrlConnectionClient.sendUpdate(params[0].getString("url"), params[0].getString("book")));
                break;

            case "Eliminar":
                //publishProgress(flag);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                output.putString("flag", "eliminar");
                output.putString("resultado", httpUrlConnectionClient.sendDelete(params[0].getString("url")));
                break;
        }
        return output;

    }

    @Override
    protected void onProgressUpdate(String... value){
        simpleUpdatableActivity.progress(value[0]);
    }

    @Override
    protected void onPostExecute(Bundle result) {
        if (null != result) {
            simpleUpdatableActivity.update(result);
        }
    }
}
