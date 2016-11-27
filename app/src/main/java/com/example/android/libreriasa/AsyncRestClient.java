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

    @Override
    protected Bundle doInBackground(Bundle... params) {

        Bundle salida = new Bundle();
        String flag = params[0].getString("flag0");
        HttpUrlConnectionClient httpUrlConnectionClient;
        switch (flag) {
            case "Listar":
                String listar = params[0].getString("flag1");
                Log.i(TAG, "Iniciando llamada al servidor para: " + flag + listar);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "lista" + listar);
                salida.putParcelableArrayList("lista" + listar, httpUrlConnectionClient.getBookList(params[0].getString("url"), listar));
                if(listar.matches("Libros")){
                    publishProgress(flag);
                }

                break;
            case "Nuevo":
                publishProgress(flag);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "insertado");
                salida.putString("resultado", httpUrlConnectionClient.sendPost(params[0].getString("url"), params[0].getString("book")));
                break;

            case "Buscar":
                publishProgress(flag);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "buscar");
                salida.putString("book", httpUrlConnectionClient.getBook(params[0].getString("url")));
                break;

            case "Editar":
                publishProgress(flag);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "insertado");
                salida.putString("resultado", httpUrlConnectionClient.sendUpdate(params[0].getString("url"), params[0].getString("book")));
                break;

            case "Eliminar":
                publishProgress(flag);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "eliminar");
                salida.putString("resultado", httpUrlConnectionClient.sendDelete(params[0].getString("url")));
                break;
        }
        return salida;

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
