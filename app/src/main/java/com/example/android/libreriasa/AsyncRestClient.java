package com.example.android.libreriasa;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Cotroc on 11/8/16.
 */

public class AsyncRestClient extends AsyncTask<Bundle, Void, Bundle> {

    private SimpleUpdatableActivity simpleUpdatableActivity;
    private static final String TAG = "AsyncRestClient";

    public AsyncRestClient(SimpleUpdatableActivity simpleUpdatableActivity) {
        this.simpleUpdatableActivity = simpleUpdatableActivity;
    }

    @Override
    protected Bundle doInBackground(Bundle... params) {
        Bundle[] par = params;
        Bundle salida = new Bundle();
        String flag = params[0].getString("flag0");
        HttpUrlConnectionClient httpUrlConnectionClient;
        switch (flag) {
            case "listar":
                String listar = params[0].getString("flag1");

                Log.i(TAG, "Iniciando llamada al servidor para: " + flag + listar);
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "lista" + listar);
                salida.putParcelableArrayList("lista" + listar, httpUrlConnectionClient.getListas(params[0].getString("url"), listar));
                break;
            case "insertar":

                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "insertado");
                salida.putString("resultado", httpUrlConnectionClient.sendPost(params[0].getString("url"), params[0].getString("book")));
                break;

            case "buscar":
                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "buscar");
                salida.putString("book", httpUrlConnectionClient.getLibro(params[0].getString("url")));
                break;

            case "editar":

                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "insertado");
                salida.putString("resultado", httpUrlConnectionClient.sendUpdate(params[0].getString("url"), params[0].getString("book")));
                break;

            case "eliminar":

                httpUrlConnectionClient = new HttpUrlConnectionClient();
                salida.putString("flag", "eliminar");
                salida.putString("resultado", httpUrlConnectionClient.sendDelete(params[0].getString("url")));
                break;
        }
        return salida;

    }

    @Override
    protected void onPostExecute(Bundle result) {
        if (null != result) {
            simpleUpdatableActivity.update(result);
        }
    }
}
