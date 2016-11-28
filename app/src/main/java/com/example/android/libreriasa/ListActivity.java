package com.example.android.libreriasa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements SimpleUpdatableActivity {

    public GridView listaLibros;
    public Button listar;
    public EditText etBuscar;
    private String ip;
    private ProgressDialog pDialog;
    private final static String HTTP = "http://";
    private final static String BOOK = "/v1/libro/";
    private final static String CAT = "/v1/categoria/";
    private static final String TAG = "ListActivity";
    private AsyncRestClient asyncRestClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        this.initComponents();
    }

    public void bookList(View v) {
        Log.i(TAG, "Enviando peticion");
        Bundle b = new Bundle();
        asyncRestClient = new AsyncRestClient(this);
        String id = etBuscar.getText().toString();
        String url = HTTP + ip + BOOK;

        if(id.isEmpty()) {
            b.putString("flag0", "Listar");
            b.putString("flag1", "Libros");
            b.putString("url", url);
            asyncRestClient.execute(b);
        } else {
            b.putString("flag0", "Buscar");
            b.putString("url", url + id);
            asyncRestClient.execute(b);
        }
    }

    public void initComponents() {
        listaLibros = (GridView) findViewById(R.id.listaLibros);
        listar = (Button) findViewById(R.id.btnList);
        etBuscar = (EditText) findViewById(R.id.etBuscar);
    }

    @Override
    public void update(Bundle b) {
        String flag = b.getString("flag");
        BookDto libro;
        ArrayList resu = new ArrayList();

        if(flag.matches("listaLibros")) {
            ArrayList results = b.getStringArrayList("listaLibros");
            for(int i = 0; i < results.size(); i ++) {
                libro = (BookDto)results.get(i);
                resu.add("ID: "+ libro.getId() + "\n Nombre: " + libro.getName() +
                        "\n Codigo: " + libro.getCod() + "\n Cantidad: " + libro.getCant() +
                        "\n Categoria: " + libro.getCatDto().getName() + "\n\n");

            }
        } if(flag.matches("buscar")) {
            try {
                JSONObject jsonLibro = new JSONObject(b.getString("book"));
                int  length = jsonLibro.length();
                if(!jsonLibro.isNull("nombre")) {
                    libro = Converter.toLibro(jsonLibro);
                    resu.add("ID: "+ libro.getId() + "\n Nombre: " + libro.getName() +
                            "\n Codigo: " + libro.getCod() + "\n Cantidad: " + libro.getCant() +
                            "\n Categoria: " + libro.getCatDto().getName() + "\n\n");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(resu.size() != 0){
            Adapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resu);
            listaLibros.setAdapter((ListAdapter) adaptador);
        } else {
            Toast.makeText(this, "No existe Libro", Toast.LENGTH_SHORT).show();
        }
       pDialog.dismiss();
    }

    @Override
    public void progress(String message) {
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("Procesando");
        pDialog.setMessage(message);
        pDialog.setCancelable(true);
        pDialog.setMax(100);
        pDialog.show();
    }

}
